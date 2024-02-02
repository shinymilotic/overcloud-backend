package overcloud.blog.usecase.test.get_test.impl;

import org.springframework.stereotype.Service;
import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.common.*;
import overcloud.blog.usecase.test.get_test.GetTestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetTestServiceImpl implements GetTestService {

    private final JpaTestRepository testRepository;

    public GetTestServiceImpl(JpaTestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestResponse getTest(String slug) {
        Optional<TestEntity> testEntity = this.testRepository.findBySlug(slug);

        if (!testEntity.isPresent()) {
            // do something...
        }

        String titleDB = testEntity.get().getTitle();
        String slugDB = testEntity.get().getSlug();

        return TestResponse.testResponseFactory(
                titleDB,
                slugDB,
                getQuestions(testEntity.get().getQuestions()));
    }

    public List<Question> getQuestions(List<QuestionEntity> questionEntities) {
        List<Question> questions = new ArrayList<>();

        for (QuestionEntity questionEntity : questionEntities) {
            if (questionEntity.getQuestionType() == 1) {
                ChoiceQuestion question = ChoiceQuestion.questionFactory(
                        questionEntity.getId().toString(),
                        questionEntity.getQuestion(),
                        getAnswers(questionEntity));
                questions.add(question);
            } else if (questionEntity.getQuestionType() == 2) {
                EssayQuestion question = EssayQuestion.questionFactory(
                        questionEntity.getId().toString(),
                        questionEntity.getQuestion());
                questions.add(question);
            }

        }

        return questions;
    }


    public List<Answer> getAnswers(QuestionEntity questionEntity) {
        List<AnswerEntity> answerEntities = questionEntity.getAnswers();
        List<Answer> answers = new ArrayList<>();
        for (AnswerEntity answerEntity : answerEntities) {
            UUID id = answerEntity.getId();
            String answerString = answerEntity.getAnswer();
            boolean truth = answerEntity.isTruth();
            Answer answer = Answer.answerFactory(id.toString(), answerString, truth);
            answers.add(answer);
        }

        return answers;
    }

}
