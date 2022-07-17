package com.kgaft.securemessengerappandroid.Database.AppPropertiesTable;

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

public class AppPropertiesTable extends SQLiteOpenHelper {
    private static final String TABLE_NAME="AppProperties";
    private static final int VERSION = 1;
    private Class<? extends TableInterface> propertiesForm;
    public AppPropertiesTable(@Nullable Context context,  @Nullable SQLiteDatabase.CursorFactory factory,  Class<? extends TableInterface> propertiesForm) {
        super(context, TABLE_NAME, factory, VERSION);
        this.propertiesForm = propertiesForm;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+ SQLUtil.valuesToCreateTableInfo(propertiesForm.newInstance().getAllValues())+");");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void deleteAppProperties(){
        getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+";");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertAppProperties(TableInterface properties){
        deleteAppProperties();
        getWritableDatabase().insert(TABLE_NAME, null, SQLUtil.tableColumnsToContentValues(properties.getAllValues()));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public TableInterface getProperties() throws IllegalAccessException, InstantiationException {
        SQLiteDatabase db = getReadableDatabase();
        Cursor results = db.query(TABLE_NAME, SQLUtil.columnsToSelect(propertiesForm.newInstance().getAllValues()), null ,null, null, null, null);
        return cursorToTableInterface(results);
    }
    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private TableInterface cursorToTableInterface(Cursor cursor) throws IllegalAccessException, InstantiationException {
        cursor.moveToFirst();
        List<TableColumn> values = new ArrayList<>();
        TableInterface result = propertiesForm.newInstance();
        result.getAllValues().forEach(column->{
            values.add(new TableColumn(column.getColumnName(), column.getColumnType(), cursor.getString(cursor.getColumnIndex(column.getColumnName()))));
        });
        result.initWithValues(values);
        return result;
    }

}
