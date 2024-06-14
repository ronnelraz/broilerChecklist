package com.ronnelrazo.broilerchecklist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ronnelrazo.broilerchecklist.func.Func;

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "BCL_SYSTEM.db";
    private static final int DATABASE_VERSION = 8;


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Func.getInstance(context).log("database created");
        db.execSQL("CREATE TABLE IF NOT EXISTS CHECKLIST_DATA"+" (" +
                "ORG_WIP TEXT," +
                "BRANCH_NAME TEXT," +
                "FARM_CODE TEXT," +
                "FARM_HOUSE_FLOCK TEXT," +
                "FARM_NAME TEXT," +
                "DATE_IN TEXT," +
                "CHICK_AGE TEXT," +
                "CURRENT_BALANCE TEXT"+
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS Farm"+" (" +
                "FARM_CODE TEXT," +
                "FARM_NAME TEXT"+
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS Address"+" (" +
                "ORG_WIP TEXT," +
                "REG_OFFICE_1 TEXT," +
                "REG_OFFICE_2 TEXT," +
                "TEL_NO TEXT" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS REASON_CODE"+" (" +
                "REASON_ID TEXT," +
                "REASON_NAME TEXT" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS TRANSACTION_HEADER" +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AUDIT_DATE TEXT," +
                "ORG_CODE TEXT," +
                "FARM_ORG TEXT," +
                "FARM_NAME TEXT," +
                "STOCK_BALANCE TEXT," +
                "DATE_IN TEXT," +
                "AGE TEXT," +
                "HOUSE_FLOCK TEXT," +
                "BROILER_COUNT TEXT," +
                "BROILER_WEIGHT TEXT," +
                "BROILER_REJECT_COUNT TEXT," +
                "BROILER_REJECT_WEIGHT TEXT," +
                "BROILER_AUTO_COUNT_FLAG TEXT," +
                "CANCEL_FLAG TEXT," +
                "CONFIRM_FLAG TEXT," +
                "CREATE_DATE TEXT" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS TRANSACTION_DETAILS" +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TR_HEADER_ID TEXT," +
                "SEQ TEXT," +
                "BIRD_COUNT TEXT," +
                "REASON_ID TEXT," +
                "REASON TEXT" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS SIGNATURE" +
                " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TR_HEADER_ID TEXT," +
                "AUDIT_DATE TEXT," +
                "FARM_ORG TEXT," +
                "HOUSE_FLOCK TEXT," +
                "PREPARED_SIGN TEXT," +
                "PREPAREDBY_USER TEXT," +
                "SALEMAN_SIGN TEXT," +
                "SALEMAN_USER TEXT," +
                "FARM_MANAGER_SIGN TEXT," +
                "FARM_MANAGERUSER TEXT," +
                "CREATE_DATE TEXT" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int OldVersion, int i1) {
        Func.getInstance(context).log("database onUpgrade success");
        if(OldVersion < DATABASE_VERSION){
            onCreate(sqLiteDatabase);
        }
        else{
            onCreate(sqLiteDatabase);
        }

    }

    private boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
