package overcloud.blog.application.article.api.controller;


import overcloud.blog.application.article.api.dto.create.CreateArticleRequest;
import overcloud.blog.application.article.api.dto.create.CreateArticleResponse;
import overcloud.blog.application.article.api.dto.delete.DeleteArticleResponse;
import overcloud.blog.application.article.api.dto.get.GetArticlesResponse;
import overcloud.blog.application.article.api.dto.update.UpdateArticleRequest;
import overcloud.blog.application.article.api.dto.update.UpdateArticleResponse;
import overcloud.blog.application.article.api.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    public CreateArticleResponse createArticle(@Valid @RequestBody CreateArticleRequest createArticleRequest) {
        return articleService.createArticle(createArticleRequest);
    }

    @PutMapping("/articles/{slug}")
    public UpdateArticleResponse updateArticle(@Valid @RequestBody UpdateArticleRequest updateArticleRequest) {
        return articleService.updateArticle(updateArticleRequest);
    }

    @DeleteMapping("/articles/{id}")
    public DeleteArticleResponse deleteArticle(@PathVariable("id") String id ) {
        return articleService.deleteArticle(id);
    }

    @GetMapping("/articles/{slug}")
    public GetArticlesResponse getArticle(@PathVariable("slug") String slug ) {
        return articleService.getArticle(slug);
    }

    @GetMapping("/articles")
    public GetArticlesResponse getArticles(@RequestParam(value = "tag", required = false) String tag,
                                           @RequestParam(value = "author", required = false) String author,
                                           @RequestParam(value = "favorited", required = false) String favorited,
                                           @RequestParam(value = "limit", defaultValue = "20") int limit,
                                           @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return articleService.getArticles(tag, author, favorited, limit, offset);
    }

}
