package com.kgaft.securemessengerserver.DataBase.Repositories;

import com.kgaft.securemessengerserver.DataBase.Entities.AuthorizedUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthorizedUsersRepo extends JpaRepository<AuthorizedUserEntity, Long> {
    @Query("FROM AuthorizedUserEntity WHERE userId=:userId")
    public Optional<AuthorizedUserEntity> getUserByUserId(@Param("userId")long userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM AuthorizedUserEntity WHERE userId=:userId")
    public void deleteByUserId(@Param("userId") long userId);
}
