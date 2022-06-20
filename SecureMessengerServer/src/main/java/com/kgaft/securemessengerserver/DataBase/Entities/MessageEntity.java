package com.kgaft.securemessengerserver.DataBase.Entities;

import com.google.gson.Gson;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "usersmessagesdata")
public class  MessageEntity implements IJsonObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long messageId;
    private String sender;
    private String receiver;
    private String messageText;
    @Column(columnDefinition = "bigint[]")
    @Type(type = "com.vladmihalcea.hibernate.type.array.LongArrayType")
    private long[] contentId = new long[]{0};

    public long time;

    public MessageEntity() {
    }

    public MessageEntity(long id, String sender, String receiver, String text, long[] contentId) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = text;
        this.contentId = contentId;
        this.messageId = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return messageText ;
    }

    public void setText(String text) {
        this.messageText  = text;
    }

    @Override
    public String toJson() {

        return new Gson().toJson(this);
    }

    public long[] getContentId() {
        return contentId;
    }

    public void setContentId(long[] contentId) {
        this.contentId = contentId;
    }
}
