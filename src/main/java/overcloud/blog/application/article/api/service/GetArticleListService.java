package overcloud.blog.application.article.api.service;

import org.springframework.stereotype.Service;
import overcloud.blog.application.article.api.dto.get.ArticleResponse;
import overcloud.blog.application.article.api.dto.get.GetArticleAuthorResponse;
import overcloud.blog.application.article.api.dto.get.GetArticlesResponse;
import overcloud.blog.application.article.api.repository.ArticleRepository;
import overcloud.blog.application.article.favorite.utils.FavoriteUtils;
import overcloud.blog.application.follow.utils.FollowUtils;
import overcloud.blog.domain.ArticleTag;
import overcloud.blog.domain.article.ArticleEntity;
import overcloud.blog.domain.user.UserEntity;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

import java.util.ArrayList;
import java.util.List;
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

    public GetArticlesResponse getArticles(String tag, String author, String favorited, int limit, int page) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(tag, author, favorited, limit, page);
        Optional<UserEntity> securityUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .map(Optional::get);

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            if(securityUser.isPresent()) {
                articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser.get(), article));
            }
            articleResponse.setFavoritesCount(article.getFavorites().size());
            List<ArticleTag> articleTagList = article.getArticleTags();
            List<String> tagList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                tagList.add(articleTag.getTag().getName());
            }
            articleResponse.setTagList(tagList);
            articleResponse.setTitle(article.getTitle());
            articleResponse.setCreatedAt(article.getCreateAt());
            articleResponse.setUpdatedAt(article.getUpdatedAt());

            GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());
            if(securityUser.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser.get(), authorEntity));
                articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            }
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }

    public GetArticlesResponse getArticlesFeed(int size, int page) {
        GetArticlesResponse getArticlesResponse = new GetArticlesResponse();
        getArticlesResponse.setArticles(new ArrayList<>());
        List<ArticleEntity> articleEntities = articleRepository.findByCriteria(null, null, null, size, page);
        Optional<UserEntity> securityUser = authenticationService.getCurrentUser()
                .map(SecurityUser::getUser)
                .map(Optional::get);

        for (ArticleEntity article: articleEntities) {
            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setId(article.getId().toString());
            articleResponse.setBody(article.getBody());
            articleResponse.setDescription(article.getDescription());
            articleResponse.setSlug(article.getSlug());
            if(securityUser.isPresent()) {
                articleResponse.setFavorited(favoriteUtils.isFavorited(securityUser.get(), article));
            }
            articleResponse.setFavoritesCount(article.getFavorites().size());
            List<ArticleTag> articleTagList = article.getArticleTags();
            List<String> tagList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                tagList.add(articleTag.getTag().getName());
            }
            articleResponse.setTagList(tagList);
            articleResponse.setTitle(article.getTitle());
            articleResponse.setCreatedAt(article.getCreateAt());
            articleResponse.setUpdatedAt(article.getUpdatedAt());

            GetArticleAuthorResponse articleAuthorResponse = new GetArticleAuthorResponse();
            UserEntity authorEntity = article.getAuthor();
            articleAuthorResponse.setUsername(authorEntity.getUsername());
            if(securityUser.isPresent()) {
                articleAuthorResponse.setFollowing(followUtils.isFollowing(securityUser.get(), authorEntity));
                articleAuthorResponse.setFollowersCount(followUtils.getFollowingCount(authorEntity));
            }
            articleAuthorResponse.setBio(authorEntity.getBio());
            articleAuthorResponse.setImage(authorEntity.getImage());

            articleResponse.setAuthor(articleAuthorResponse);
            getArticlesResponse.getArticles().add(articleResponse);

        }

        return getArticlesResponse;
    }
}
