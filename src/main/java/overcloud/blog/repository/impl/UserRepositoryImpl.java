package overcloud.blog.repository.impl;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }
}