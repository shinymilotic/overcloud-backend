package overcloud.blog.usecase.auth.manage_role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.usecase.auth.common.RoleError;
import overcloud.blog.usecase.auth.common.UpdateFlg;

import java.util.List;
import java.util.Optional;

@Service
public class ManageRoleService {

    private final JpaRoleRepository roleRepository;

    public ManageRoleService(JpaRoleRepository roleRepository) {
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
                case NEW -> {
                    Optional<RoleEntity> savedRole = saveRole(role);
                    if (savedRole.isPresent()) {
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
                case NO_CHANGE -> {
                }
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
            String currentRoleName = role.getRoleName();
            String updateRoleName = role.getUpdateRoleName();
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
        if (request.getRoles() == null) {
            throw new InvalidDataException(ApiError.from(RoleError.ROLE_LIST_NOT_EMPTY));
        }

        for (ManageRoleDto role : request.getRoles()) {
            if (role == null) {
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
