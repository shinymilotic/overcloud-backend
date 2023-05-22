package overcloud.blog.application.user.login;

import lombok.NoArgsConstructor;
import overcloud.blog.application.user.core.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
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
