package com.codes.sampletodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToDoRecordOpenHelper extends SQLiteOpenHelper {

    public ToDoRecordOpenHelper(@Nullable Context context,
                                @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table todo ( id integer primary key autoincrement," +
                "contents text, time text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table todo";
        db.execSQL(sql);
        onCreate(db);
    }

    /**
     * DB에 데이터 추가하기
     * @param record to store
     * @return created RowId
     */
    public long addRecord(ToDoRecord record){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put("contents", record.getContents());
        values.put("time", record.getTime());

        return db.insert("todo",null,values);
    }

    public ArrayList<ToDoRecord> getRecords(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("todo",
                null,
                null,
                null,
                null,
                null,
                null);
        ArrayList<ToDoRecord> result = new ArrayList<>();
        while (c.moveToNext()){
            ToDoRecord r = new ToDoRecord(
                    c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("contents")),
                    c.getString(c.getColumnIndex("time")));
            result.add(r);
        }
        c.close();
        return result;
    }
}
