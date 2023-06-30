package overcloud.blog.application.role.manage_role;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import overcloud.blog.application.article.core.exception.InvalidDataException;
import overcloud.blog.application.role.RolesRequest;
import overcloud.blog.application.role.core.RoleDto;
import overcloud.blog.application.role.core.RoleEntity;
import overcloud.blog.application.role.core.RoleError;
import overcloud.blog.application.role.core.RoleRepository;
import overcloud.blog.infrastructure.UpdateFlg;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ManageRoleService {

    private final RoleRepository roleRepository;

    public ManageRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ManageRoleResponse manageRole(ManageRoleRequest request) {
        validate(request);
        List<ManageRoleDto> roleList = request.getRoles();

        int rowAffected = 0;
        for (ManageRoleDto role : roleList) {
            UpdateFlg updateFlg = UpdateFlg.fromInt(role.getUpdateFlg());

            switch (updateFlg) {
                case NEW ->  {
                    Optional<RoleEntity> savedRole = saveRole(role);
                    if(savedRole.isPresent()) {
                        rowAffected++;
                    }
                }
                case UPDATE -> {
                    int updatedRowsCount = updateRole(role);
                    rowAffected += updatedRowsCount;
                }
                case DELETE -> {
                    int deleteRowsCount = deleteRole(role);
                    rowAffected += deleteRowsCount;
                }
                case NO_CHANGE -> {}
            }
        }

        return toRoleResponse(rowAffected);
    }

    private Optional<RoleEntity> saveRole(ManageRoleDto role) {
        try {
            RoleEntity roleEntity = RoleEntity.builder()
                    .name(role.getRoleName()).build();
                return Optional.of(roleRepository.saveAndFlush(roleEntity));
        } catch (Exception e) {
            throw new InvalidDataException(ApiError.from(RoleError.ROLE_EXISTED));
        }
    }

    private int updateRole(ManageRoleDto role) {
        try {
            String currentRoleName = role.getCurrentRoleName();
            String updateRoleName = role.getRoleName();
            return roleRepository.updateRoleByName(currentRoleName, updateRoleName);
        } catch (Exception e) {
            throw new InvalidDataException(ApiError.from(RoleError.ROLE_EXISTED));
        }
    }

    private int deleteRole(ManageRoleDto roleDto) {
        String deleteRoleName = roleDto.getRoleName();
        return roleRepository.deleteRoleByName(deleteRoleName);
    }

    private void validate(ManageRoleRequest request) {
        if(request.getRoles() == null) {
            throw new InvalidDataException(ApiError.from(RoleError.ROLE_LIST_NOT_EMPTY));
        }

        for (ManageRoleDto role : request.getRoles()) {
            if(role == null) {
                throw new InvalidDataException(ApiError.from(RoleError.ROLENAME_SIZE));
            }
        }
    }

    public ManageRoleResponse toRoleResponse(int rowAffected) {
        return ManageRoleResponse.builder()
                .rowAffected(rowAffected)
                .build();
    }
}
