package overcloud.blog.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import overcloud.blog.entity.EssayAnswerEntity;

@Repository
public interface EssayAnswerRepository extends JpaRepository<EssayAnswerEntity, UUID> {
    
}
