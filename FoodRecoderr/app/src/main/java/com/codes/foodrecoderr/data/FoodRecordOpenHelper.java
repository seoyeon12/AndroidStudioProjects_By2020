package com.codes.foodrecoderr.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FoodRecordOpenHelper extends SQLiteOpenHelper {
    //누가 나를 호출하나(context), 사용할 DB의 이름(name), select 문을 받을 커서 사용값(보통의 NULL)
    //버전(version)
    public FoodRecordOpenHelper(@Nullable Context context,
                                @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    //자동으로 불리는 것은 앱 최초 실행 시 디비가 존재하지 않을 때, 단 한번 호출된다.(즉, 테이블 생성)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table foods (id integer primary key autoincrement," +
                "food text, time text)";
                db.execSQL(sql);
    }
    //DB의 버전이 올라갔을 때 호출된다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table foods";
        db.execSQL(sql);
        onCreate(db);
    }
    /**
     * DB에 데이터 추가하기
     * @param record to store
     * @return created RowId
     */
    public long addRecord(FoodRecord record){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("food", record.getFood());
        values.put("time", record.getTime());

        return  db.insert("foods",null,values);
    }
    public ArrayList<FoodRecord> getRecords(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("foods",
                null, //String[] Columns
                null, // Where, "food=?"
                null, //String[], where clause's args
                null, //GroupBy
                null, //Having
                null); //OrderBy, "time desc"
        ArrayList<FoodRecord> result = new ArrayList<>();
        while (c.moveToNext()){
            FoodRecord r = new FoodRecord(
                    c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("food")),
                    c.getString(c.getColumnIndex("time")));
            result.add(r);
        }
        c.close();
        return  result;
    }
    /**
     * 기록을 하나 지운다.
     * @param id 삭제할 데이터의 아이디
     * @return 삭제된 행 수
     */
    public int delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] args={String.valueOf(id)};
        return db.delete("foods", "id=?",args);
    }
}
