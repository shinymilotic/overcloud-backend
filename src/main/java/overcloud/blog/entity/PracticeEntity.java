package overcloud.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "practice", schema = "public")
public class PracticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tester_id")
    private UUID testerId;

    @Column(name = "test_id")
    private UUID testId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "test_id", insertable = false, updatable = false)
    private TestEntity test;

    @OneToMany(mappedBy = "practice", orphanRemoval = true)
    private List<PracticeChoiceEntity> choices;

    @OneToMany(mappedBy = "practice", orphanRemoval = true)
    private List<EssayAnswerEntity> essayAnswers;
}
