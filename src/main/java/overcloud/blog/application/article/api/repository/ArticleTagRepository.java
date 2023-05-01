package overcloud.blog.application.article.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import overcloud.blog.domain.article.ArticleTag;
import overcloud.blog.domain.article.ArticleTagId;

import java.util.UUID;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    @Modifying
    @Query("DELETE FROM ArticleTag articleTag" +
            " WHERE articleTag.article.id = :articleId AND articleTag.tag.id = :tagId ")
    void deleteArticleTags(UUID articleId, UUID tagId);

}
