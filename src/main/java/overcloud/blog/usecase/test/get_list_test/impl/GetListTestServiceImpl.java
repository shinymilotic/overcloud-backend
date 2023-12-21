package overcloud.blog.usecase.test.get_list_test.impl;

import org.springframework.stereotype.Service;

import overcloud.blog.entity.AnswerEntity;
import overcloud.blog.entity.QuestionEntity;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.TestRepository;
import overcloud.blog.usecase.test.common.Answer;
import overcloud.blog.usecase.test.common.ChoiceQuestion;
import overcloud.blog.usecase.test.common.EssayQuestion;
import overcloud.blog.usecase.test.common.Question;
import overcloud.blog.usecase.test.common.TestListResponse;
import overcloud.blog.usecase.test.common.TestResponse;
import overcloud.blog.usecase.test.get_list_test.GetListTestService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GetListTestServiceImpl implements GetListTestService {

    private final TestRepository testRepository;

    public GetListTestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestListResponse getListTest() {
        List<TestEntity> testEntities = testRepository.findAll();
        List<TestResponse> responses = new ArrayList<>();
        for (TestEntity testEntity: testEntities) {
            List<QuestionEntity> questionEntities = testEntity.getQuestions();
            String title = testEntity.getTitle();
            String slug = testEntity.getSlug();
            List<Question> questions = getQuestions(questionEntities);
            TestResponse testResponse = TestResponse.testResponseFactory(title, slug, questions);
            responses.add(testResponse);
        }

        return TestListResponse.testListResponseFactory(responses);
    }

    public List<Answer> getAnswers(List<AnswerEntity> answerEntities) {
        List<Answer> answers = new ArrayList<>();
        for (AnswerEntity answerEntity : answerEntities) {
            UUID id = answerEntity.getId();
            String answer = answerEntity.getAnswer();
            boolean truth = answerEntity.isTruth();
            answers.add(Answer.answerFactory(id.toString(), answer, truth));
        }

        return answers;
    }

    public List<Question> getQuestions(List<QuestionEntity> questionEntities) {
        List<Question> questions = new ArrayList<>();

        for (QuestionEntity questionEntity: questionEntities) {
            if (questionEntity.getQuestionType() == 1) {
                ChoiceQuestion question = ChoiceQuestion.questionFactory(
                    questionEntity.getId().toString(),
                    questionEntity.getQuestion(),
                    getAnswers(questionEntity.getAnswers()));
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

}