package overcloud.blog.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "favorites", schema = "public")
public class FavoriteEntity {
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("articleId")
    private ArticleEntity article;

    @ManyToOne
    @MapsId("userId")
    private UserEntity user;
}