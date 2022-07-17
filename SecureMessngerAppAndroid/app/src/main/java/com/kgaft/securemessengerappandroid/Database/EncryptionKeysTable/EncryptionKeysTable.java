package com.kgaft.securemessengerappandroid.Database.EncryptionKeysTable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.kgaft.securemessengerappandroid.Database.SQLUtil;
import com.kgaft.securemessengerappandroid.Database.TableColumn;
import com.kgaft.securemessengerappandroid.Database.TableInterface;

import java.util.ArrayList;
import java.util.List;

public class EncryptionKeysTable extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "EncryptionKeys";
    private static final int VERSION = 1;
    private final Class<? extends TableInterface> keysForm;

    public EncryptionKeysTable(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, Class<? extends TableInterface> keysForm) {
        super(context, TABLE_NAME, factory, VERSION);
        this.keysForm = keysForm;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+ SQLUtil.valuesToCreateTableInfo(keysForm.newInstance().getAllValues())+");");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void deleteKey(TableInterface key){
        TableColumn idColumn = key.getIDField();
        String idValue = idColumn.getColumnType().equals("TEXT")?"'":""+idColumn.getColumnValue()+(idColumn.getColumnType().equals("TEXT")?"'":"");
        getWritableDatabase().delete(TABLE_NAME, key.getIDField().getColumnName()+"=?", new String[]{key.getIDField().getColumnValue()});
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertKey(TableInterface key){
        try {
            deleteKey(key);
        }catch (Exception ignored){

        }
        getWritableDatabase().insert(TABLE_NAME, null, SQLUtil.tableColumnsToContentValues(key.getAllValues()));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public TableInterface getKey(String idFieldValue) throws IllegalAccessException, InstantiationException {
        SQLiteDatabase db = getReadableDatabase();
        TableInterface keyExample = keysForm.newInstance();
        return cursorToTableInterface(db.query(TABLE_NAME, SQLUtil.columnsToSelect(keyExample.getAllValues()), keyExample.getIDField().getColumnName()+"=?", new String[]{idFieldValue}, null, null, null));
    }
    @SuppressLint("Range")
    public List<String> getAvailableReceivers(){
        SQLiteDatabase db = getReadableDatabase();
        List<String> results = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"receiver"}, null, null, null, null, null);
        cursor.moveToFirst();
        do{
            results.add(cursor.getString(cursor.getColumnIndex("receiver")));
        }while (cursor.moveToNext());
        return results;
    }
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private TableInterface cursorToTableInterface(Cursor cursor) throws IllegalAccessException, InstantiationException {
        cursor.moveToFirst();
        List<TableColumn> values = new ArrayList<>();
        TableInterface result = keysForm.newInstance();
        result.getAllValues().forEach(column-> values.add(new TableColumn(column.getColumnName(), column.getColumnType(), cursor.getString(cursor.getColumnIndex(column.getColumnName())))));
        result.initWithValues(values);
        return result;
    }
}
