package com.kgaft.securemessengerserver.DataBase.Repositories;

import com.kgaft.securemessengerserver.DataBase.Entities.MessageEntity;
import com.kgaft.securemessengerserver.DataBase.Entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface MessageRepo extends CrudRepository<MessageEntity, Long> {
    @Query("FROM MessageEntity WHERE sender=:sender and time>=:date")
    public Iterable<MessageEntity> findMessageBySender(@Param("sender") String sender, @Param("date") Timestamp date);
    @Query("FROM MessageEntity WHERE receiver=:receiver and time>=:date")
    public Iterable<MessageEntity> findMessageByReceiver(@Param("receiver") String receiver, @Param("date") Timestamp date);
    @Query("FROM MessageEntity WHERE (receiver=:userLogin or sender=:userLogin) and time>=:date")
    public Iterable<MessageEntity> findMessageByReceiverOrSender(@Param("userLogin") String userLogin, @Param("date") Timestamp date);

}
