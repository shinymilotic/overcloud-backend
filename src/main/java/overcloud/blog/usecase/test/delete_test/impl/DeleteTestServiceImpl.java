package overcloud.blog.usecase.test.delete_test.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.TestEntity;
import overcloud.blog.repository.IPracticeRepository;
import overcloud.blog.repository.jparepository.JpaTestRepository;
import overcloud.blog.usecase.test.delete_test.DeleteTestService;

import java.util.Optional;

@Service
public class DeleteTestServiceImpl implements DeleteTestService {

    private final JpaTestRepository testRepository;

    private final IPracticeRepository practiceRepository;

    DeleteTestServiceImpl(
            JpaTestRepository testRepository,
            IPracticeRepository practiceRepository) {
        this.testRepository = testRepository;
        this.practiceRepository = practiceRepository;
    }

    @Override
    @Transactional
    public void deleteTest(String slug) {
        Optional<TestEntity> test = testRepository.findBySlug(slug);

        if (!test.isPresent()) {

        }

        practiceRepository.deleteByTestId(test.get().getId());
        testRepository.deleteById(test.get().getId());
    }

}
