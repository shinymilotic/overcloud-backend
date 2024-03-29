package overcloud.blog.usecase.auth.follow.make_unfollow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaFollowRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.follow.core.FollowEntity;
import overcloud.blog.usecase.auth.follow.core.utils.FollowUtils;

import java.util.List;

@Service
public class UnfollowService {

    private final JpaUserRepository userRepository;

    private final SpringAuthenticationService authenticationService;

    private final JpaFollowRepository followRepository;

    private final FollowUtils followUtils;

    public UnfollowService(JpaUserRepository userRepository,
                           SpringAuthenticationService authenticationService,
                           JpaFollowRepository followRepository,
                           FollowUtils followUtils) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.followRepository = followRepository;
        this.followUtils = followUtils;
    }

    @Transactional
    public UnfollowResponse unfollowUser(String username) {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        UserEntity followee = userRepository.findByUsername(username);

        List<FollowEntity> followEntity = followRepository.getFollowing(currentUser.getUsername(), followee.getUsername());
        followRepository.delete(followEntity.get(0));

        return toUnfollowResponse(followee);
    }

    public UnfollowResponse toUnfollowResponse(UserEntity followee) {
        return UnfollowResponse.builder()
                .username(followee.getUsername())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(false)
                .build();
    }
}
