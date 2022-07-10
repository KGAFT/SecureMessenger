package com.kgaft.SecureMessengerPCClient.BackEnd.Utils.Interfaces;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Value;

import java.util.List;

public interface IEncrypted {
    List<Value> getFieldsToEncryptDecrypt();
    void initWithValues(List<Value> fields);
}
