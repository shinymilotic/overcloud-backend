package overcloud.blog.application.user.dto.login;

import overcloud.blog.application.user.dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    @JsonProperty("user")
    private UserResponse userResponse;

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }
}
