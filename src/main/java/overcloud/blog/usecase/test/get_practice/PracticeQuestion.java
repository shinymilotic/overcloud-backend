package overcloud.blog.usecase.test.get_practice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PracticeQuestionDeserializer.class)
public interface PracticeQuestion {
    String getId();
    void setId(String id);

    String getQuestion();
    void setQuestion(String question);

    int getQuestionType();
    void setQuestionType(int questionType);
}