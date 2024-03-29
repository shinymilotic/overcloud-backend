package overcloud.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import overcloud.blog.usecase.auth.common.UserListResponse;
import overcloud.blog.usecase.auth.common.UserResponse;
import overcloud.blog.usecase.auth.confirm_email.ConfirmEmailRequest;
import overcloud.blog.usecase.auth.confirm_email.ConfirmEmailService;
import overcloud.blog.usecase.auth.get_current_user.GetCurrentUserService;
import overcloud.blog.usecase.auth.get_followers.FollowerListResposne;
import overcloud.blog.usecase.auth.get_followers.GetFollowers;
import overcloud.blog.usecase.auth.get_profile.GetProfileResponse;
import overcloud.blog.usecase.auth.get_profile.GetProfileService;
import overcloud.blog.usecase.auth.get_roles_user.GetRolesUserService;
import overcloud.blog.usecase.auth.get_roles_user.UserRoleListResponse;
import overcloud.blog.usecase.auth.get_users.GetUserListService;
import overcloud.blog.usecase.auth.login.LoginRequest;
import overcloud.blog.usecase.auth.login.LoginService;
import overcloud.blog.usecase.auth.logout.LogoutService;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenResponse;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenService;
import overcloud.blog.usecase.auth.register.RegisterRequest;
import overcloud.blog.usecase.auth.register.RegisterService;
import overcloud.blog.usecase.auth.update_user.UpdateUserRequest;
import overcloud.blog.usecase.auth.update_user.UpdateUserService;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    private final RegisterService registerService;
    private final UpdateUserService updateUserService;
    private final LogoutService logoutService;
    private final LoginService loginService;
    private final GetProfileService getProfileService;
    private final GetCurrentUserService getCurrentUserService;
    private final RefreshTokenService refreshTokenService;
    private final GetUserListService getUserListService;
    private final GetRolesUserService getRolesUserService;

    private final ConfirmEmailService confirmEmailService;

    private final GetFollowers getFollowers;


    public UserController(RegisterService registerService,
                          UpdateUserService updateUserService,
                          LogoutService logoutService,
                          LoginService loginService,
                          GetProfileService getProfileService,
                          GetCurrentUserService getCurrentUserService,
                          RefreshTokenService refreshTokenService,
                          GetUserListService getUserListService,
                          GetRolesUserService getRolesUserService, ConfirmEmailService confirmEmailService,
                          GetFollowers getFollowers) {
        this.registerService = registerService;
        this.updateUserService = updateUserService;
        this.logoutService = logoutService;
        this.loginService = loginService;
        this.getProfileService = getProfileService;
        this.getCurrentUserService = getCurrentUserService;
        this.refreshTokenService = refreshTokenService;
        this.getUserListService = getUserListService;
        this.getRolesUserService = getRolesUserService;
        this.confirmEmailService = confirmEmailService;
        this.getFollowers = getFollowers;
    }

    @PostMapping(ApiConst.USERS)
    public UserResponse register(@RequestBody RegisterRequest registrationDto, HttpServletResponse response) throws Exception {
        return registerService.registerUser(registrationDto, response);
    }

    @PutMapping(ApiConst.USERS)
    public UserResponse update(@RequestBody UpdateUserRequest updateUserDto) {
        return updateUserService.updateUser(updateUserDto);
    }

    @PostMapping(ApiConst.USERS_LOGIN)
    public UserResponse login(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGIN_ADMIN)
    public UserResponse loginAdmin(@RequestBody LoginRequest loginDto, HttpServletResponse response) {
        return loginService.login(loginDto, response);
    }

    @PostMapping(ApiConst.USERS_LOGOUT)
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @GetMapping(ApiConst.USERS)
    public UserResponse getCurrentUser() {
        return getCurrentUserService.getCurrentUser();
    }

    @GetMapping(ApiConst.PROFILES_USERNAME)
    public GetProfileResponse getProfile(@PathVariable String username) throws Exception {
        return getProfileService.getProfile(username);
    }

    @PostMapping(ApiConst.USERS_REFRESHTOKEN)
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return refreshTokenService.refreshToken(request, response);
    }

    @GetMapping(ApiConst.USER_LIST)
    public UserListResponse getUsers(int page, int size) throws Exception {
        return getUserListService.getUsers(page, size);
    }

    @GetMapping(ApiConst.ROLES_USERNAME)
    public UserRoleListResponse getRolesUser(@PathVariable String username) {
        return getRolesUserService.getRolesUser(username);
    }

    @PostMapping(ApiConst.CONFIRM_EMAIL)
    public boolean confirmEmail(@RequestBody  ConfirmEmailRequest confirmToken) {
        return confirmEmailService.confirmEmail(confirmToken);
    }

    @GetMapping(ApiConst.FOLLOWERS)
    public FollowerListResposne getFollowers(@PathVariable UUID userId) {
        return getFollowers.getFollowers(userId);
    }
}
