package overcloud.blog.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.PracticeEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.get_followers.FollowerListResposne;
import overcloud.blog.usecase.auth.get_followers.FollowerResponse;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final EntityManager entityManager;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository, EntityManager entityManager) {
        this.jpaUserRepository = jpaUserRepository;
        this.entityManager = entityManager;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return jpaUserRepository.save(entity);
    }

    @Override
    public void enableUser(String confirmToken) {
        jpaUserRepository.enableUser(confirmToken);
    }

    @Override
    public FollowerListResposne getFollowers(UUID userId) {
        FollowerListResposne resposne = new FollowerListResposne();
        Query query = entityManager
                .createNativeQuery("select u.id, u.email, u.username, u.bio, u.image  " +
                                "from users u " +
                                "inner join follows f " +
                                "on u.id = f.followee_id " +
                                "where f.follower_id = :userId ",
                        Tuple.class)
                .setParameter("userId", userId);

        List<Tuple> results =  query.getResultList();

        for (Tuple row : results) {
            UserResponse follower = new UserResponse(
                    (UUID) row.get("id"),
                    (String) row.get("email"),
                    (String) row.get("username"),
                    (String) row.get("bio"),
                    (String) row.get("image"));
            resposne.getFollowers().add(follower);
        }

        return resposne;
    }
}
