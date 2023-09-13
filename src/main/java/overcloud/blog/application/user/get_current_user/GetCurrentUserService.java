package overcloud.blog.application.user.get_current_user;

import org.springframework.stereotype.Service;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserError;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.UserResponseMapper;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;

@Service
public class GetCurrentUserService {

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;

    private final JwtUtils jwtUtils;

    public GetCurrentUserService(SpringAuthenticationService authenticationService,
                                 UserResponseMapper userResponseMapper,
                                 JwtUtils jwtUtils) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
        this.jwtUtils = jwtUtils;
    }

    public UserResponse getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        return userResponseMapper.toUserResponse(currentUser);
    }
}
