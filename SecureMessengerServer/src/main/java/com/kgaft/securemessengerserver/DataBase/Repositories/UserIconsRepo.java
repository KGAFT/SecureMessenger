package com.kgaft.securemessengerserver.DataBase.Repositories;


import com.kgaft.securemessengerserver.DataBase.Entities.UserIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserIconsRepo extends JpaRepository<UserIcon, Long> {
    @Query("FROM UserIcon WHERE userLogin=:login")
    public UserIcon getIconByLogin(@Param("login") String login);
    @Transactional
    @Modifying
    @Query("DELETE FROM UserIcon WHERE userLogin=:login")
    public void deleteIconByLogin(@Param("login")String login);
}
