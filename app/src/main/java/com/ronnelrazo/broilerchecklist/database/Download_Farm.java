package com.ronnelrazo.broilerchecklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ronnelrazo.broilerchecklist.model.Checklist_data;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;

import java.util.List;

public class Download_Farm {

    private Context context;

    public Download_Farm(Context context){
        this.context = context;
    }

    public boolean setData(int position, List<Checklist_farm_data> data){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FARM_CODE",data.get(position).getFarm_code());
        cv.put("FARM_NAME",data.get(position).getFarm_name());
        long result = db.insert("Farm",null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean remove() {
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        db.delete("Farm",null,null);
        return true;
    }

}
