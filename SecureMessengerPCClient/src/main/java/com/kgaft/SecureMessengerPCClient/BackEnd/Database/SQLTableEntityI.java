package com.kgaft.SecureMessengerPCClient.BackEnd.Database;

import java.util.List;

public interface SQLTableEntityI {
    List<Value> getAllValues();
    void initWithValues(List<Value> values);
    String getTableName();
}
