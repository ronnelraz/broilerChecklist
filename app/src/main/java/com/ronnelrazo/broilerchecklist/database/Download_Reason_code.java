package com.ronnelrazo.broilerchecklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;

import java.util.List;

public class Download_Reason_code {

    private Context context;

    public Download_Reason_code(Context context){
        this.context = context;
    }

    public boolean setData(int position, List<Checklist_Reason> data){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("REASON_ID",data.get(position).getREASON_ID());
        cv.put("REASON_NAME",data.get(position).getREASON_NAME());
        long result = db.insert("REASON_CODE",null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean remove() {
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        db.delete("REASON_CODE",null,null);
        return true;
    }

}
