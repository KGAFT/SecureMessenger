package com.kgaft.securemessengerappandroid.Services.EncryptionUtil;

import com.kgaft.securemessengerappandroid.Database.TableColumn;

import java.util.List;

public interface IEncrypted {
    List<TableColumn> getFieldsToEncryptDecrypt();
    void initWithValues(List<TableColumn> fields);
}
