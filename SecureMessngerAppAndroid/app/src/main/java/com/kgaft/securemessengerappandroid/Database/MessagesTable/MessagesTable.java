package com.kgaft.securemessengerappandroid.Database.MessagesTable;

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

public class MessagesTable extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "MessagesTable";
    private static final int VERSION = 1;
    private final Class<? extends TableInterface> messagesForm;
    public MessagesTable(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, Class<? extends TableInterface> messagesForm) {
        super(context, TABLE_NAME, factory, VERSION);
        this.messagesForm = messagesForm;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+ SQLUtil.valuesToCreateTableInfo(messagesForm.newInstance().getAllValues())+");");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean insertMessage(TableInterface message) throws IllegalAccessException, InstantiationException {
        if(!isMessageExists(message.getIDField().getColumnValue())){
            getWritableDatabase().insert(TABLE_NAME, null, SQLUtil.tableColumnsToContentValues(message.getAllValues()));
            return true;
        }
        return false;
    }
    public void deleteMessagesByReceiverOrSender(String receiverOrSender){
        getWritableDatabase().delete(TABLE_NAME, "receiver=? OR sender = ?", new String[]{receiverOrSender, receiverOrSender});
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<TableInterface> getMessagesByReceiverOrSender(String receiverOrSender) throws IllegalAccessException, InstantiationException {
        return cursorToTableInterfaces(getReadableDatabase().query(TABLE_NAME, SQLUtil.columnsToSelect(messagesForm.newInstance().getAllValues()), "receiver = ? OR sender = ?", new String[]{receiverOrSender, receiverOrSender}, null, null, "time ASC"));
    }
    public void deleteAll(){
        getWritableDatabase().delete(TABLE_NAME, null, null);
    }
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<TableInterface> cursorToTableInterfaces(Cursor cursor) throws IllegalAccessException, InstantiationException {
        List<TableInterface> messages = new ArrayList<>();
        cursor.moveToFirst();
        do{
            List<TableColumn> values = new ArrayList<>();
            TableInterface message = messagesForm.newInstance();
            message.getAllValues().forEach(column-> values.add(new TableColumn(column.getColumnName(), column.getColumnType(), cursor.getString(cursor.getColumnIndex(column.getColumnName())))));
            message.initWithValues(values);
            messages.add(message);
        }while (cursor.moveToNext());

        return messages;
    }
    private boolean isMessageExists(String idFieldValue) throws IllegalAccessException, InstantiationException {
        TableInterface messageExample = messagesForm.newInstance();
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{messageExample.getIDField().getColumnName()}, messageExample.getIDField().getColumnName()+"=?", new String[]{idFieldValue}, null, null, null);
        return cursor.moveToFirst();
    }

}
