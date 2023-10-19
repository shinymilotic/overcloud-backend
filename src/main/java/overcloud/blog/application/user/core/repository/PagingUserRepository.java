package overcloud.blog.application.user.core.repository;

import overcloud.blog.entity.UserEntity;

import java.util.List;

public interface PagingUserRepository {
    List<UserEntity> findAll(int page, int size);
}
