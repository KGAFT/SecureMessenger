package com.kgaft.SecureMessengerPCClient.BackEnd.Database;

public class Value {
    private Class<?> type;
    private String value;
    private String valueName;

    public Value(Class<?> type, String value, String valueName) {
        this.type = type;
        this.value = value;
        this.valueName = valueName;
    }

    public Value() {
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
