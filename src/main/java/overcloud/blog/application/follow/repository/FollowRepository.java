package overcloud.blog.application.follow.repository;


import overcloud.blog.domain.user.follow.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, UUID> {

    @Query(" SELECT f FROM FollowEntity f WHERE" +
            " f.follower.username = :currentUsername" +
            " AND f.followee.username = :followingUsername ")
    List<FollowEntity> getFollowing(String currentUsername, String followingUsername);

}
