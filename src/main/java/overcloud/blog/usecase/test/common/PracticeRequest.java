package overcloud.blog.usecase.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@JsonTypeName("practice")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class PracticeRequest {
    @JsonProperty("slug")
    private String slug;

    @JsonProperty("choiceAnswers")
    private List<ChoiceAnswer> choiceAnswers;

    @JsonProperty("essayAnswers")
    private List<EssayAnswer> essayAnswers;
}
