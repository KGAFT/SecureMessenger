package com.kgaft.SecureMessengerPCClient.BackEnd.MessageService.MessageReceiverManager.Entities;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.Interfaces.IEncrypted;

import java.util.ArrayList;
import java.util.List;

public class MessageEntity implements SQLTableEntityI, IEncrypted {
    private long messageId;
    private String sender;
    private String receiver;
    private String messageText;
    private long time;
    private long[] contentId;

    public MessageEntity(long messageId, String sender, String receiver, String messageText, long time, long[] contentId) {
        this.messageId = messageId;
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.time = time;
        this.contentId = contentId;
    }

    public MessageEntity() {
    }

    @Override
    public List<Value> getAllValues() {
        List<Value> values = new ArrayList<>();
        values.add(new Value(Long.class, String.valueOf(messageId), "messageId"));
        values.add(new Value(String.class, sender, "sender"));
        values.add(new Value(String.class, receiver, "receiver"));
        values.add(new Value(String.class, messageText, "messageText"));
        values.add(new Value(Long.class, String.valueOf(time), "time"));
        StringBuilder contents = new StringBuilder();
        for (long content : contentId) {
            contents.append(content).append(";");
        }
        values.add(new Value(String.class, contents.toString(), "contentId"));
        return null;
    }

    @Override
    public void initWithValues(List<Value> values) {
        values.forEach(value->{
            switch (value.getValueName()){
                case "messageId":
                    messageId = Long.parseLong(value.getValue());
                    break;
                case "receiver":
                    receiver = value.getValue();
                    break;
                case "sender":
                    sender = value.getValue();
                    break;
                case "messageText":
                    messageText = value.getValue();
                    break;
                case "time":
                    time = Long.parseLong(value.getValue());
                    break;
                case "contentId":
                    String[] contents = value.getValue().split(";");
                    contentId = new long[contents.length];
                    for(int counter = 0; counter<contents.length; counter++){
                        contentId[counter] = Long.parseLong(contents[counter]);
                    }
                    break;
            }
        });
    }

    @Override
    public String getTableName() {
        return "messages";
    }
    @Override
    public List<Value> getFieldsToEncryptDecrypt() {
        List<Value> fieldsToEncrypt = new ArrayList<>();
        fieldsToEncrypt.add(new Value(String.class, messageText, "messageText"));
        return fieldsToEncrypt;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long[] getContentId() {
        return contentId;
    }

    public void setContentId(long[] contentId) {
        this.contentId = contentId;
    }




}
