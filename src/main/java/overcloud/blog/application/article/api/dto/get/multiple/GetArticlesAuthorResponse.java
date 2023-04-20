package overcloud.blog.application.article.api.dto.get.multiple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetArticlesAuthorResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("image")
    private String image;

    @JsonProperty("following")
    private boolean following;

    @JsonProperty("followersCount")
    private int followersCount;
}