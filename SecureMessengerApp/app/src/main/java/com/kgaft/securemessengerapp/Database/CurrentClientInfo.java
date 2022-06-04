package com.kgaft.securemessengerapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CurrentClientInfo extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "ClientInfo";
    private static final int VERSION = 1;
    private SQLiteDatabase dbHandle;

    public CurrentClientInfo(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, VERSION);
        dbHandle = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "userId BIGINT," +
                "appId BIGINT," +
                "userLogin TEXT," +
                "userName TEXT," +
                "serverAddress TEXT);");
        dbHandle = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertClientData(long userId, long appId, String userLogin, String userName, String serverAddress) {
        clearTable();
        ContentValues data = new ContentValues();
        data.put("userId", userId);
        data.put("appId", appId);
        data.put("userLogin", userLogin);
        data.put("userName", userName);
        data.put("serverAddress", serverAddress);
        dbHandle.insert(TABLE_NAME, null, data);
    }

    @SuppressLint("Range")
    public ContentValues getAppData() {
        ContentValues data = new ContentValues();
        Cursor cursor = dbHandle.query(TABLE_NAME, new String[]{"userId", "appId", "userLogin", "userName", "serverAddress"}, "userId != 0", null, null, null, null);
        cursor.moveToFirst();
        data.put("userId", cursor.getLong(cursor.getColumnIndex("userId")));
        data.put("appId", cursor.getLong(cursor.getColumnIndex("appId")));
        data.put("userLogin", cursor.getString(cursor.getColumnIndex("userLogin")));
        data.put("userName", cursor.getString(cursor.getColumnIndex("userName")));
        data.put("serverAddress", cursor.getString(cursor.getColumnIndex("serverAddress")));
        return data;

    }

    private void clearTable() {
        dbHandle.execSQL("DELETE FROM " + TABLE_NAME + " WHERE userId!=0");
    }

    public void updateData(long userId, long appId, String userLogin, String userName, String serverAddress) {
        clearTable();
        insertClientData(userId, appId, userLogin, userName, serverAddress);
    }

    @SuppressLint("Range")
    public String getServerAddress() {
        Cursor cursor = dbHandle.query(TABLE_NAME, new String[]{"serverAddress"}, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("serverAddress"));
    }

}
