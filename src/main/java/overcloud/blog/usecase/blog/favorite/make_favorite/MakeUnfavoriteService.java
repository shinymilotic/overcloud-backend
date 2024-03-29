package overcloud.blog.usecase.blog.favorite.make_favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.ArticleEntity;
import overcloud.blog.entity.ArticleTag;
import overcloud.blog.entity.FavoriteId;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaArticleRepository;
import overcloud.blog.repository.jparepository.JpaFavoriteRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.blog.common.AuthorResponse;
import overcloud.blog.usecase.blog.create_article.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class MakeUnfavoriteService {

    private final JpaFavoriteRepository favoriteRepository;

    private final SpringAuthenticationService authenticationService;

    private final JpaArticleRepository articleRepository;


    public MakeUnfavoriteService(JpaFavoriteRepository favoriteRepository,
                                 SpringAuthenticationService authenticationService,
                                 JpaArticleRepository articleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.authenticationService = authenticationService;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public ArticleResponse makeUnfavorite(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug).get(0);
        UserEntity author = articleEntity.getAuthor();
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        ArticleResponse articleResponse = new ArticleResponse();
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setUsername(author.getUsername());
        authorResponse.setBio(author.getBio());
        authorResponse.setImage(author.getImage());

        articleResponse.setAuthor(authorResponse);
        articleResponse.setFavoritesCount(articleEntity.getFavorites().size() - 1);
        articleResponse.setFavorited(false);
        articleResponse.setBody(articleEntity.getBody());
        List<ArticleTag> articleTagList = articleEntity.getArticleTags();
        List<String> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagList.add(articleTag.getTag().getName());
        }
        articleResponse.setTagList(tagList);

        articleResponse.setDescription(articleEntity.getDescription());
        articleResponse.setId(articleEntity.getId().toString());
        articleResponse.setSlug(articleEntity.getSlug());
        articleResponse.setTitle(articleEntity.getTitle());

        FavoriteId favoritePk = new FavoriteId();
        favoritePk.setUserId(currentUser.getId());
        favoritePk.setArticleId(articleEntity.getId());
        favoriteRepository.deleteById(favoritePk);
        return articleResponse;
    }
}
