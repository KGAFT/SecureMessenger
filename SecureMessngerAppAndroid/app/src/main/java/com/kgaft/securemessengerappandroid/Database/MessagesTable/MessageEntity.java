package com.kgaft.securemessengerappandroid.Database.MessagesTable;

import com.kgaft.securemessengerappandroid.Database.TableColumn;
import com.kgaft.securemessengerappandroid.Database.TableInterface;
import com.kgaft.securemessengerappandroid.Services.EncryptionUtil.IEncrypted;

import java.util.ArrayList;
import java.util.List;

public class MessageEntity implements TableInterface, IEncrypted {
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

    @Override
    public List<TableColumn> getAllValues() {
        List<TableColumn> values = new ArrayList<>();
        values.add(new TableColumn("messageId", "BIGINT", String.valueOf(messageId)));
        values.add(new TableColumn("sender", "TEXT", sender));
        values.add(new TableColumn("receiver", "TEXT", receiver));
        values.add(new TableColumn("messageText", "TEXT", messageText));
        values.add(new TableColumn("time", "BIGINT", String.valueOf(time)));
        StringBuilder contentId = new StringBuilder();
        for (long l : this.contentId) {
            contentId.append(l).append(";");
        }
        values.add(new TableColumn("contentId", "TEXT", contentId.toString()));
        return values;
    }

    @Override
    public List<TableColumn> getFieldsToEncryptDecrypt() {
        List<TableColumn> fields = new ArrayList<>();
        fields.add(new TableColumn("messageText", "TEXT", messageText));
        return fields;
    }

    @Override
    public void initWithValues(List<TableColumn> values) {
        for (TableColumn value : values) {
            switch (value.getColumnName()){
                case "messageId":
                    this.messageId = Long.parseLong(value.getColumnValue());
                    break;
                case "sender":
                    this.sender = value.getColumnValue();
                    break;
                case "receiver":
                    this.receiver = value.getColumnValue();
                    break;
                case "messageText":
                    this.messageText = value.getColumnValue();
                    break;
                case "time":
                    this.time = Long.parseLong(value.getColumnValue());
                    break;
                case "contentId":
                    String[] contents = value.getColumnValue().split(";");
                    contentId = new long[contents.length];
                    for (int i = 0; i < contents.length; i++) {
                        contentId[i] = Long.parseLong(contents[i]);
                    }
                    break;
            }
        }
    }

    @Override
    public TableColumn getIDField() {
        return new TableColumn("messageId", "BIGINT", String.valueOf(messageId));
    }
}
