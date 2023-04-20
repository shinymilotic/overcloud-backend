package overcloud.blog.application.article.api.dto.get.multiple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import overcloud.blog.application.article.api.dto.get.multiple.GetArticlesAuthorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetArticlesSingleResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("body")
    private String body;

    @JsonProperty("tagList")
    private List<String> tagList;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("favorited")
    private boolean favorited;

    @JsonProperty("favoritesCount")
    private int favoritesCount;

    @JsonProperty("author")
    private GetArticlesAuthorResponse author;
}