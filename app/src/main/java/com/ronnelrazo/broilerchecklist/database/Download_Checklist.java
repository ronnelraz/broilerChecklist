package com.ronnelrazo.broilerchecklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ronnelrazo.broilerchecklist.model.Checklist_data;

import java.util.List;

public class Download_Checklist {

    private Context context;

    public Download_Checklist(Context context){
        this.context = context;
    }

    public boolean setData(int position, List<Checklist_data> data){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ORG_WIP",data.get(position).getOrg_wip());
        cv.put("BRANCH_NAME",data.get(position).getBranch_name());
        cv.put("FARM_CODE",data.get(position).getFarm_code());
        cv.put("FARM_HOUSE_FLOCK",data.get(position).getFarm_house_flock());
        cv.put("FARM_NAME",data.get(position).getFarm_name());
        cv.put("DATE_IN",data.get(position).getDate_in());
        cv.put("CHICK_AGE",data.get(position).getChick_age());
        cv.put("CURRENT_BALANCE",data.get(position).getCurrent_balance());
        long result = db.insert("CHECKLIST_DATA",null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean remove() {
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        db.delete("CHECKLIST_DATA",null,null);
        return true;
    }

}
