package com.kgaft.SecureMessengerPCClient.BackEnd.Database;



import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final HashMap<String, Class> registeredTables = new HashMap<>();
    public static void deleteQuery(String sql) throws SQLException {
        generateConnection().createStatement().execute(sql);
    }
    public static void createAndRegisterTableClass(SQLTableEntityI table) throws Exception {
        HashMap<String, String> fields = convertJavaFieldsToSQLFields(table.getAllValues());
        String createTableSQL = "CREATE TABLE "+table.getTableName()+"(";
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            createTableSQL+=entry.getKey()+" "+entry.getValue()+",";
        }
        createTableSQL = createTableSQL.substring(0, createTableSQL.length()-1)+")";
        try{
            generateConnection().createStatement().execute(createTableSQL);
        }catch (Exception e){
            e.printStackTrace();
        }
        registeredTables.put(table.getTableName(), table.getClass());
    }
    public static void insertEntity(SQLTableEntityI entity) throws SQLException {
        if(registeredTables.containsKey(entity.getTableName())){
            generateConnection().createStatement().execute(generateInsertSQL(entity.getTableName(), entity.getAllValues()));
        }
    }
    public static void selectQuery(String sql, List<SQLTableEntityI> target, SQLTableEntityI example) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ResultSet selectionResult = generateConnection().createStatement().executeQuery(sql);
        ArrayList<Value> selectionValues = new ArrayList<>();
        while(selectionResult.next()){
            SQLTableEntityI line = example.getClass().getConstructor().newInstance();
            example.getAllValues().forEach(targetField->{
                Value value = new Value();
                value.setValueName(targetField.getValueName());
                value.setType(targetField.getType());
                try {
                    value.setValue(selectionResult.getString(value.getValueName()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                selectionValues.add(value);
            });
            line.initWithValues(selectionValues);
            target.add(line);
            selectionValues.clear();

        }

    }
    private static String generateInsertSQL(String target, List<Value> values){
        String sql = "INSERT INTO "+target+" (";
        for (Value value : values) {
            sql+=value.getValueName()+", ";
        }
        sql = sql.substring(0, sql.length()-2)+") VALUES(";

        for(Value value : values){
            if(value.getType().getName().equals("java.lang.String")){
                sql+="'"+value.getValue()+"'"+", ";
            }
            else{
                sql+=value.getValue()+", ";
            }
        }
        sql = sql.substring(0, sql.length()-2)+")";
        return sql;
    }
    private static Connection generateConnection(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            return DriverManager.getConnection("jdbc:derby:SecureMessengerAppDb;create=true;user=KGAFT;password=12345");
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static HashMap<String, String> convertJavaFieldsToSQLFields(List<Value> fields) throws Exception {
        HashMap<String, String> fieldsSQL = new HashMap<>();
        for (Value field : fields) {
            fieldsSQL.put(field.getValueName(), javaTypeToSQL(field.getType().getTypeName()));
        }
        return fieldsSQL;
    }
    private static String javaTypeToSQL(String type) throws Exception {
        switch(type){
            case "long":
                return "BIGINT";
            case "java.lang.Long":
                return "BIGINT";
            case "int":
                return "INTEGER";
            case "java.lang.Integer":
                return "INTEGER";
            case "float":
                return "FLOAT";
            case "java.lang.Float":
                return "FLOAT";
            case "java.lang.String":
                return "LONG VARCHAR";
            case "java.lang.Date":
                return "TIMESTAMP";
        }
        throw new Exception("Cannot find type for: "+type);
    }
}
