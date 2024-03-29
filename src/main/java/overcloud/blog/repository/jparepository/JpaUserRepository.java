package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.PagingUserRepository;

import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID>, PagingUserRepository {
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username ")
    UserEntity findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u LEFT JOIN fetch u.roles WHERE  u.email = :email ")
    UserEntity findByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN fetch u.roles WHERE u.username = :username ")
    UserEntity findRolesByUsernname(@Param("username") String username);

    @Modifying
    @Query(value = "UPDATE users SET enable = true FROM refresh_token e WHERE users.id = e.user_id and e.refresh_token  = :refreshToken", nativeQuery = true)
    void enableUser(@Param("refreshToken") String refreshToken);
}
