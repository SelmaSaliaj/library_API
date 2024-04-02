package com.project.repository;

import com.project.domain.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity,Integer> {

    UserEntity findByUsername(String username);

}
