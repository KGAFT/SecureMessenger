package com.kgaft.securemessengerserver.DataBase.Repositories;

import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserLoginRepo extends JpaRepository<UserEntity, Long> {
    @Query("FROM UserEntity WHERE login=:login")
    public Iterable<UserEntity> findByLogin(@Param("login") String login);
    @Transactional
    @Modifying
    @Query("delete from UserEntity b where b.login=:login")
    public void deleteUserByLogin(@Param("login")String login);
}
