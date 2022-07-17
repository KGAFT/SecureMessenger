package com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys;

import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Database;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;
import com.kgaft.SecureMessengerPCClient.BackEnd.EncryptionKeys.Entities.EncryptionKeyEntity;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EncryptionNativeCalls {
    public EncryptionNativeCalls(){
        try {
            Database.createAndRegisterTableClass(new EncryptionKeyEntity());
        } catch (Exception e) {

        }
    }
    public EncryptionKeyEntity getEncryptionKeyForReceiver(String receiver) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<SQLTableEntityI> selectionResults = new ArrayList<>();
        EncryptionKeyEntity key = new EncryptionKeyEntity();
        Database.selectQuery("SELECT * FROM " + key.getTableName()+" WHERE cast(receiver as CHAR(128))='"+receiver+"'", selectionResults, key);
        key = (EncryptionKeyEntity) selectionResults.get(0);
        return key;
    }
    public void insertEncryptionKey(EncryptionKeyEntity key) throws SQLException {
        try{
            Database.deleteQuery("DELETE FROM "+key.getTableName()+" WHERE CAST(receiver AS CHAR(128))='"+key.getReceiver()+"'");
        }catch (Exception e){

        }
        Database.insertEntity(key);
    }
    public List<String> getAvailableEncryptionKeysForReceivers() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<SQLTableEntityI> results = new ArrayList<>();
        EncryptionKeyEntity example = new EncryptionKeyEntity();
        Database.selectQuery("SELECT * FROM "+example.getTableName(), results, example);
        ArrayList<String> receivers = new ArrayList<>();
        results.forEach(key->{
            receivers.add(((EncryptionKeyEntity)key).getReceiver());
        });
        return receivers;
    }
    public boolean isKeyExists(String receiver) {
        List<SQLTableEntityI> results = new ArrayList<>();
        EncryptionKeyEntity example = new EncryptionKeyEntity();
        try{
            Database.selectQuery("SELECT * FROM "+example.getTableName()+" WHERE CAST(receiver AS VARCHAR(128))='"+receiver+"'", results, example);
            return results.size()>0;
        }catch (Exception e){
            return false;
        }
    }
}
