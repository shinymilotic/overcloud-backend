package overcloud.blog.usecase.auth.get_current_user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.auth.common.UserError;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.common.UserResponseMapper;

@Service
public class GetCurrentUserService {

    private final SpringAuthenticationService authenticationService;

    private final UserResponseMapper userResponseMapper;


    public GetCurrentUserService(SpringAuthenticationService authenticationService,
                                 UserResponseMapper userResponseMapper) {
        this.authenticationService = authenticationService;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        UserEntity currentUser = authenticationService.getCurrentUser()
                .orElseThrow(() -> new InvalidDataException(ApiError.from(UserError.USER_NOT_FOUND)))
                .getUser();

        return userResponseMapper.toUserResponse(currentUser);
    }
}
