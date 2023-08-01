package overcloud.blog.article.update;

import org.checkerframework.checker.units.qual.A;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import overcloud.blog.application.article.core.ArticleEntity;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.article.core.repository.ArticleRepository;
import overcloud.blog.application.article.create_article.ArticleRequest;
import overcloud.blog.application.article.favorite.core.FavoriteEntity;
import overcloud.blog.application.article.favorite.core.utils.FavoriteUtils;
import overcloud.blog.application.article.update_article.UpdateArticleRequest;
import overcloud.blog.application.article.update_article.UpdateArticleResponse;
import overcloud.blog.application.article.update_article.UpdateArticleService;
import overcloud.blog.application.tag.core.TagEntity;
import overcloud.blog.application.tag.core.repository.TagRepository;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.article.ArticleEntityFactory;
import overcloud.blog.article.ArticleRequestFactory;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateArticleTest {
    @Mock
    private UpdateArticleService updateArticleRequest;
    @Mock
    private SpringAuthenticationService authenticationService;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FavoriteUtils favoriteUtils;
    @Mock
    private ObjectsValidator<UpdateArticleRequest> validator;
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    private void setAuthentication() {
        // add principal object to SecurityContextHolder
        UserEntity user = UserEntity.builder()
                        .id(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"))
                        .email("trungtin.mai1412@gmail.com")
                        .username("thepianist00")
                        .bio("A pragmatddsdsadsaic programmerss")
                        .image("https://avatars.githubusercontent.com/u/19252712?s=100&v=100")
                        .password("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a").build();

        UserDetails secureUser = new SecurityUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(secureUser,secureUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @BeforeEach
    void initUseCase() {
        validator = new ObjectsValidator<UpdateArticleRequest>(messageSource());
        authenticationService = new SpringAuthenticationService(userRepository,new BCryptPasswordEncoder());
        updateArticleRequest = new UpdateArticleService(authenticationService, articleRepository, tagRepository, favoriteUtils, validator);
    }

    private UpdateArticleResponse updateArticle(UpdateArticleRequest articleRequest, String currentSlug) throws Exception {
        when(articleRepository.findBySlug(anyString())).thenAnswer(invocation -> {
            String slug = invocation.getArgument(0);
            List<ArticleEntity> articleEntities = new ArrayList<>();
            ArticleEntity article = ArticleEntityFactory.createArticleBySlug();
            article.setSlug(slug);
            articleEntities.add(article);
            return articleEntities;
        });

        when(articleRepository.save(any(ArticleEntity.class))).thenAnswer(invocation -> {
            ArticleEntity article = invocation.getArgument(0);
            ArticleEntity savedArticle = new ArticleEntity();
            savedArticle.setId(article.getId());
            savedArticle.setTitle(article.getTitle());
            savedArticle.setSlug(article.getSlug());
            savedArticle.setBody(article.getBody());
            savedArticle.setAuthor(article.getAuthor());
            savedArticle.setCreatedAt(article.getCreatedAt());
            savedArticle.setUpdatedAt(article.getUpdatedAt());

            return savedArticle;
        });

        return updateArticleRequest.updateArticle(articleRequest, currentSlug);
    }

    private UpdateArticleResponse updateArticleError(UpdateArticleRequest articleRequest, String currentSlug) throws Exception {


        return updateArticleRequest.updateArticle(articleRequest, currentSlug);
    }

    @Test
    public void test_updateArticle() throws Exception {
        setAuthentication();
        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .body("dsadasdsads").build();
        String slug = "contrasting-three-projects";

        UpdateArticleResponse articleResponse = updateArticle(request, slug);

        Assertions.assertEquals(request.getBody(), articleResponse.getBody());
    }

    @Test
    public void test_updateArticleBlankBody() throws Exception {
        setAuthentication();
        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .body("")
                .build();
        String slug = "contrasting-three-projects";
        ApiError apiError = null;

        try {
            UpdateArticleResponse articleResponse = updateArticleError(request, slug);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "article.body.not-blank", "Article body must be specified");
    }

    @Test
    public void test_updateArticleNoLogin() throws Exception {
        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .body("")
                .build();
        String slug = "contrasting-three-projects";
        ApiError apiError = null;

        try {
            UpdateArticleResponse articleResponse = updateArticleError(request, slug);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.get-current-user.not-found", "You have to sign in first!");
    }

    @Test
    public void test_updateArticleNullBody() throws Exception {
        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .build();
        String slug = "contrasting-three-projects";
        ApiError apiError = null;

        try {
            UpdateArticleResponse articleResponse = updateArticleError(request, slug);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "article.body.not-blank", "Article body must be specified");
    }

    public void assertApiError(ApiError apiError, String id, String message) {
        List<ApiErrorDetail> apiErrorDetail = apiError.getApiErrorDetails();

        for (ApiErrorDetail detail : apiErrorDetail) {
            if(detail.getId().equals(id)) {
                assertEquals(detail.getMessage(), message);
                return;
            }
        }

        assertFalse(false);
    }
}