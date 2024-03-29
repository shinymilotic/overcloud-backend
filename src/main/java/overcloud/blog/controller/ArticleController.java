package overcloud.blog.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.blog.create_article.ArticleRequest;
import overcloud.blog.usecase.blog.create_article.ArticleResponse;
import overcloud.blog.usecase.blog.create_article.CreateArticleService;
import overcloud.blog.usecase.blog.delete_article.DeleteArticleResponse;
import overcloud.blog.usecase.blog.delete_article.DeleteArticleService;
import overcloud.blog.usecase.blog.get_article.GetArticleResponse;
import overcloud.blog.usecase.blog.get_article.GetArticleService;
import overcloud.blog.usecase.blog.get_article_list.GetArticleListService;
import overcloud.blog.usecase.blog.get_article_list.GetArticlesResponse;
import overcloud.blog.usecase.blog.update_article.UpdateArticleRequest;
import overcloud.blog.usecase.blog.update_article.UpdateArticleResponse;
import overcloud.blog.usecase.blog.update_article.UpdateArticleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ArticleController {
    private final CreateArticleService createArticleService;

    private final UpdateArticleService updateArticleService;

    private final GetArticleService getArticleService;

    private final GetArticleListService getArticleListService;

    private final DeleteArticleService deleteArticleService;


    public ArticleController(CreateArticleService createArticleService,
                             UpdateArticleService updateArticleService,
                             GetArticleService getArticleService,
                             GetArticleListService getArticleListService,
                             DeleteArticleService deleteArticleService) {
        this.createArticleService = createArticleService;
        this.updateArticleService = updateArticleService;
        this.getArticleService = getArticleService;
        this.getArticleListService = getArticleListService;
        this.deleteArticleService = deleteArticleService;
    }

    @Secured({"ADMIN", "USER"})
    @PostMapping(ApiConst.ARTICLES)
    public ArticleResponse createArticle(@RequestBody ArticleRequest createArticleRequest) throws JsonProcessingException {
        return createArticleService.createArticle(createArticleRequest);
    }

    @Secured({"ADMIN", "USER"})
    @PutMapping(ApiConst.ARTICLES_SLUG)
    public UpdateArticleResponse updateArticle(@RequestBody UpdateArticleRequest updateArticleRequest,
                                               @PathVariable("slug") String currentSlug) {
        return updateArticleService.updateArticle(updateArticleRequest, currentSlug);
    }

    @Secured({"ADMIN", "USER"})
    @DeleteMapping(ApiConst.ARTICLES_SLUG)
    public DeleteArticleResponse deleteArticle(@PathVariable String slug) {
        return deleteArticleService.deleteArticle(slug);
    }

    @GetMapping(ApiConst.ARTICLES_SLUG)
    public GetArticleResponse getArticle(@PathVariable String slug) {
        return getArticleService.getArticle(slug);
    }

    @GetMapping(ApiConst.ARTICLES)
    public GetArticlesResponse getArticles(@RequestParam(required = false) String tag,
                                           @RequestParam(required = false) String author,
                                           @RequestParam(required = false) String favorited,
                                           @RequestParam(value = "size", defaultValue = "20") int limit,
                                           @RequestParam(defaultValue = "") String lastArticleId) {
        return getArticleListService.getArticles(tag, author, favorited, limit, lastArticleId);
    }
}
