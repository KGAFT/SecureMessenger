package com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable;

import com.kgaft.securemessengerappandroid.Database.TableColumn;
import com.kgaft.securemessengerappandroid.Database.TableInterface;
import com.kgaft.securemessengerappandroid.Services.EncryptionUtil.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;

public class EncryptionKey implements TableInterface {
    private String receiver;
    private byte[] encryptionKey;
    @Override
    public List<TableColumn> getAllValues() {
        List<TableColumn> values = new ArrayList<>();
        values.add(new TableColumn("receiver", "TEXT", receiver));
        values.add(new TableColumn("encryptionKey", "TEXT", EncryptionUtil.keyToString(encryptionKey)));
        return values;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public byte[] getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @Override
    public void initWithValues(List<TableColumn> values) {
        for (TableColumn value : values) {
            switch (value.getColumnName()){
                case "receiver":
                    this.receiver = value.getColumnValue();
                    break;
                case "encryptionKey":
                    this.encryptionKey = EncryptionUtil.encryptedStringToByteArray(value.getColumnValue());
                    break;
            }
        }
    }

    @Override
    public TableColumn getIDField() {
        return new TableColumn("receiver", "TEXT", receiver);
    }
}
