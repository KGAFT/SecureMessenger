package com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.Entities;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;
import com.kgaft.SecureMessengerPCClient.BackEnd.Utils.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;

public class EncryptionKeyEntity implements SQLTableEntityI {
    private byte[] encryptionKey;
    private String receiver;

    public EncryptionKeyEntity() {
    }

    public EncryptionKeyEntity(byte[] encryptionKey, String receiver) {
        this.encryptionKey = encryptionKey;
        this.receiver = receiver;
    }
    public EncryptionKeyEntity(String encryptionKey, String receiver) {
        this.encryptionKey = EncryptionUtil.encryptedStringToByteArray(encryptionKey);
        this.receiver = receiver;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
    public void setEncryptionKey(String key){
        encryptionKey = EncryptionUtil.encryptedStringToByteArray(key);
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public List<Value> getAllValues() {
        List<Value> values = new ArrayList<>();
        values.add(new Value(String.class, receiver, "receiver"));
        try{
            values.add(new Value(String.class, EncryptionUtil.byteArrayToString(encryptionKey), "encryptionKey"));
        }catch (Exception e){
            values.add(new Value(String.class, "0;", "encryptionKey"));
        }

        return values;
    }

    @Override
    public void initWithValues(List<Value> values) {
        values.forEach(value -> {
            switch (value.getValueName()){
                case "receiver":
                    receiver = value.getValue();
                    break;
                case "encryptionKey":
                    encryptionKey = EncryptionUtil.encryptedStringToByteArray(value.getValue());
                    break;
            }
        });
    }

    @Override
    public String getTableName() {
        return "encryptionKeys";
    }
}
