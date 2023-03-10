package overcloud.blog.application.user.repository;

import overcloud.blog.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    UserEntity findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE  u.email = :email")
    UserEntity findByEmail(@Param("email") String email);
}
