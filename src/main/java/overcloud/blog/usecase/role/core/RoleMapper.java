package overcloud.blog.usecase.role.core;

import org.springframework.stereotype.Component;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.usecase.role.RoleListResponse;

import java.util.Collection;
import java.util.List;

@Component
public class RoleMapper {

    public RoleListResponse toRoleListResponse(Collection<RoleEntity> roles) {
        List<RoleResponse> roleResponseList = roles.stream()
                .map(this::toRoleResponse)
                .toList();

        return RoleListResponse.builder()
                .roleList(roleResponseList)
                .build();
    }

    public RoleResponse toRoleResponse(RoleEntity roleEntity) {
        return RoleResponse.builder()
                .roleId(roleEntity.getId().toString())
                .roleName(roleEntity.getName())
                .build();
    }

}