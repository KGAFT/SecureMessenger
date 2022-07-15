package com.kgaft.securemessengerappandroid.Database;

import android.content.ContentValues;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

public class SQLUtil {
    public static String valuesToCreateTableInfo(List<TableColumn> values){
        StringBuilder info = new StringBuilder();
        for (TableColumn value : values) {
            info.append(value.getColumnName()).append(" ").append(value.getColumnType()).append(",");
        }
        return info.substring(0, info.length()-1);
    }

    public static String[] columnsToSelect(List<TableColumn> columns){
        String[] columnsToSelect = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnsToSelect[i] = columns.get(i).getColumnName();
        }
        return columnsToSelect;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ContentValues tableColumnsToContentValues(List<TableColumn> columns){
        ContentValues contentValues = new ContentValues();
        columns.forEach(element->{
            switch (element.getColumnType()){
                case "TEXT":
                    contentValues.put(element.getColumnName(), element.getColumnValue());
                    break;
                case "BLOB":
                    contentValues.put(element.getColumnName(), stringToBlob(element.getColumnValue()));
                    break;
                case "BIGINT":
                    contentValues.put(element.getColumnName(), Long.parseLong(element.getColumnValue()));
                    break;
                case "INTEGER":
                    contentValues.put(element.getColumnName(), Integer.parseInt(element.getColumnValue()));
                    break;
                case "REAL":
                    contentValues.put(element.getColumnName(), Float.parseFloat(element.getColumnValue()));
                    break;
            }

        });
        return contentValues;
    }
    private static byte[] stringToBlob(String array){
        String[] textBytes = array.split(";");
        byte[] result = new byte[textBytes.length];
        for (int i = 0; i < textBytes.length; i++) {
            result[i] = Byte.parseByte(textBytes[i]);
        }
        return result;
    }
}
