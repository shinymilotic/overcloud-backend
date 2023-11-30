package overcloud.blog.application.test.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Answer {

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("truth")
    private boolean truth;

    public static Answer answerFactory(String answer, boolean truth) {
        return Answer.builder()
                .answer(answer)
                .truth(truth)
                .build();
    }
}