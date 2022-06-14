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
    private long messageid;
    private String sender;
    private String receiver;
    private String messagetext;
    @Column(columnDefinition = "bigint[]")
    @Type(type = "com.vladmihalcea.hibernate.type.array.LongArrayType")
    private long[] contentid = new long[]{0};

    public long time;

    public MessageEntity() {
    }

    public MessageEntity(long id, String sender, String receiver, String text, long[] contentId) {
        this.sender = sender;
        this.receiver = receiver;
        this.messagetext = text;
        this.contentid = contentId;
        this.messageid = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMessageId() {
        return messageid;
    }

    public void setMessageId(long messageId) {
        this.messageid = messageId;
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
        return messagetext ;
    }

    public void setText(String text) {
        this.messagetext  = text;
    }

    @Override
    public String toJson() {

        return new Gson().toJson(this);
    }

    public long[] getContentId() {
        return contentid;
    }

    public void setContentId(long[] contentId) {
        this.contentid = contentId;
    }
}
