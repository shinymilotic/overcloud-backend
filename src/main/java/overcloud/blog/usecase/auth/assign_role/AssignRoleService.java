package overcloud.blog.usecase.auth.assign_role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.entity.UserRole;
import overcloud.blog.entity.UserRoleId;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.jparepository.JpaRoleRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRoleRepository;
import overcloud.blog.usecase.auth.common.RoleDto;
import overcloud.blog.usecase.auth.common.RoleError;
import overcloud.blog.usecase.auth.common.RolesRequest;
import overcloud.blog.usecase.auth.common.UpdateFlg;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AssignRoleService {

    private final JpaUserRoleRepository userRoleRepository;

    private final JpaUserRepository userRepository;

    private final JpaRoleRepository roleRepository;

    public AssignRoleService(JpaUserRoleRepository userRoleRepository,
                             JpaUserRepository userRepository,
                             JpaRoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleAssignmentResponse assignRole(RolesRequest request, String username) {
        List<RoleDto> roles = request.getRoles();
        UserEntity userEntity = userRepository.findByUsername(username);
        List<String> roleNames = roles.stream()
                .map(RoleDto::getRoleName)
                .collect(Collectors.toList());
        List<RoleEntity> roleEntities = roleRepository.findAllByNames(roleNames);
        Map<String, RoleEntity> roleEntitiesMap = roleEntities.stream()
                .collect(Collectors.toMap(RoleEntity::getName, Function.identity()));

        for (RoleDto role : roles) {
            UpdateFlg updateFlg = UpdateFlg.fromInt(role.getUpdateFlg());
            RoleEntity roleEntity = roleEntitiesMap.get(role.getRoleName());

            switch (updateFlg) {
                case NEW -> {
                    Optional<UserRole> userRole = assignNewRole(roleEntity, userEntity);
                    if (userRole.isEmpty()) {
                        throw new InvalidDataException(ApiError.from(RoleError.ROLE_ASSIGNMENT_FAILED));
                    }
                }
                case DELETE -> {
                    deleteAssigedRole(roleEntity, userEntity);
                }
                default -> {
                }
            }
        }

        return toRoleAssignmentResponse(username);
    }

    private RoleAssignmentResponse toRoleAssignmentResponse(String username) {
        return RoleAssignmentResponse.builder()
                .username(username)
                .build();
    }

    private Optional<UserRole> assignNewRole(RoleEntity roleEntity, UserEntity userEntity) {
        try {
            UserRole userRole = UserRole.builder()
                    .id(new UserRoleId())
                    .role(roleEntity)
                    .user(userEntity)
                    .build();
            UserRole savedEntity = userRoleRepository.saveAndFlush(userRole);
            return Optional.of(savedEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void deleteAssigedRole(RoleEntity roleEntity, UserEntity userEntity) {
        UserRoleId id = new UserRoleId();
        id.setUserId(userEntity.getId());
        id.setRoleId(roleEntity.getId());
        UserRole userRole = new UserRole();
        userRole.setId(id);

        userRoleRepository.delete(userRole);
    }
}
