package com.kgaft.securemessengerserver.DataBase.Repositories;

import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserLoginRepo extends CrudRepository<UserEntity, Long> {
    @Query("FROM UserEntity WHERE login=:login")
    public Iterable<UserEntity> findByLogin(@Param("login") String login);
}
