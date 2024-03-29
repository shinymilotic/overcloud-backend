package overcloud.blog.usecase.auth.assign_role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleAssignmentResponse {
    @JsonProperty("username")
    private String username;
}
