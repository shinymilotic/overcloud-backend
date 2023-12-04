package overcloud.blog.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.TestEntity;

import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID> {
    @Query("SELECT t FROM TestEntity t WHERE t.slug = :slug")
    TestEntity findBySlug(@Param("slug") String slug);

    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.slug = :slug")
    void deleteBySlug(@Param("slug") String slug);
}

