package overcloud.blog.application.article.service;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.dto.get.multiple.GetArticlesSingleResponse;
import overcloud.blog.application.article.dto.get.multiple.GetArticlesAuthorResponse;
import overcloud.blog.application.article.dto.get.multiple.GetArticlesResponse;
import overcloud.blog.application.article.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.user.follow.utils.FollowUtils;
import overcloud.blog.application.article.ArticleEntity;
import overcloud.blog.application.user.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GetArticleListService {

    private final FollowUtils followUtils;

    private final FavoriteUtils favoriteUtils;

    private final ArticleRepository articleRepository;

    private final SpringAuthenticationService authenticationService;

    public GetArticleListService(FollowUtils followUtils,
                                 FavoriteUtils favoriteUtils,
                                 ArticleRepository articleRepository,
                                 SpringAuthenticationService authenticationService) {
        this.followUtils = followUtils;
        this.favoriteUtils = favoriteUtils;
        this.articleRepository = articleRepository;
        this.authenticationService = authenticationService;
    }

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, int page, String searchParam) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(tag, author, favorited, limit, page, searchParam);

        Optional<SecurityUser> currentUser = authenticationService.getCurrentUser();
        Optional<UserEntity> currentUserEntity = Optional.empty();
        if (currentUser.isPresent()) {
            currentUserEntity = currentUser.filter(Objects::nonNull)
                    .map(SecurityUser::getUser).get();
        }

        for (ArticleEntity article: articleEntities) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article, currentUserEntity);
            getArticlesResponse.getArticles().add(singleResponse);
        }

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticlesFeed(int size, int page, String searchParam) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(null, null, null, size, page, searchParam);

        Optional<SecurityUser> currentUser = authenticationService.getCurrentUser();
        Optional<UserEntity> currentUserEntity = Optional.empty();
        if (currentUser.isPresent()) {
            currentUserEntity = currentUser.filter(Objects::nonNull)
                    .map(SecurityUser::getUser).get();
        }

        for (ArticleEntity article: articleEntities) {
            GetArticlesSingleResponse singleResponse = toGetArticlesSingleResponse(article, currentUserEntity);
            getArticlesResponse.getArticles().add(singleResponse);
        }

        return getArticlesResponse;
    }

    private GetArticlesSingleResponse toGetArticlesSingleResponse(ArticleEntity article, Optional<UserEntity> currentUser) {
        return GetArticlesSingleResponse.builder()
                .id(article.getId().toString())
                .title(article.getTitle())
                .body(article.getBody())
                .description(article.getDescription())
                .slug(article.getSlug())
                .author(toGetArticleAuthorResponse(currentUser.get(), article.getAuthor()))
                .favorited(favoriteUtils.isFavorited(currentUser.get(), article))
                .favoritesCount(article.getFavorites().size())
                .tagList(article.getTagNameList())
                .createdAt(article.getCreateAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private GetArticlesAuthorResponse toGetArticleAuthorResponse(UserEntity currentUser, UserEntity author) {
        return GetArticlesAuthorResponse.builder()
                .username(author.getUsername())
                .bio(author.getBio())
                .image(author.getImage())
                .following(followUtils.isFollowing(currentUser, author))
                .followersCount(followUtils.getFollowingCount(author))
                .build();
    }
}