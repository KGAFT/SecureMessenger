package com.kgaft.securemessengerapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.kgaft.securemessengerapp.Utils.KeyUtil;

import java.util.ArrayList;

public class EncryptionKeys extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "EncryptionKeys";
    private static final int VERSION = 1;
    public EncryptionKeys(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(" +
                "receiverLogin TEXT," +
                "encryptionKey BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void deleteKey(String receiver){
        getWritableDatabase().delete(TABLE_NAME, "receiverLogin = ?", new String[]{receiver});
    }
    public void insertKey(String key, String receiver){
        try{
            deleteKey(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("receiverLogin", receiver);
        data.put("encryptionKey", KeyUtil.stringToByte(key));
        db.insert(TABLE_NAME, null, data);
    }
    @SuppressLint("Range")
    public byte[] getKey(String receiver){
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.query(TABLE_NAME, new String[]{"encryptionKey"}, "receiverLogin = ?", new String[]{receiver}, null, null, null);
        data.moveToFirst();
        return data.getBlob(data.getColumnIndex("encryptionKey"));
    }
    @SuppressLint("Range")
    public ArrayList<String> getAllReceivers(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> receivers = new ArrayList<>();
        Cursor data = db.query(TABLE_NAME, new String[]{"receiverLogin"}, null, null, null, null, null);
        data.moveToFirst();
        try{
            do{
                receivers.add(data.getString(data.getColumnIndex("receiverLogin")));
            }while(data.moveToNext());
        }catch (Exception e){
            e.printStackTrace();
        }

        return receivers;
    }
}
