package com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties;

import com.kgaft.SecureMessengerPCClient.BackEnd.AppProperties.Entities.Property;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.Database;
import com.kgaft.SecureMessengerPCClient.BackEnd.Database.SQLTableEntityI;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppProperties {
    static {
        try {
            Database.createAndRegisterTableClass(new Property());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getServerBaseUrl() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<SQLTableEntityI> results = new ArrayList<>();
        Property example = new Property();
        try{
            Database.selectQuery("SELECT * FROM "+example.getTableName()+" WHERE cast(propertyName as CHAR(128))='serverBaseUrl'", results, example);
        }catch (Exception e){
            return null;
        }

        return ((Property)results.get(0)).getPropertyValue();
    }
    public static void setServerBaseUrl(String serverUrl) throws SQLException {
        Property property = new Property("serverBaseUrl", serverUrl);
        Database.insertEntity(property);
    }
}
