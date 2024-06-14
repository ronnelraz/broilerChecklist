package com.ronnelrazo.broilerchecklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ronnelrazo.broilerchecklist.model.Checklist_address;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;

import java.util.List;

public class Download_address {

    private Context context;

    public Download_address(Context context){
        this.context = context;
    }

    public boolean setData(int position, List<Checklist_address> data){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ORG_WIP",data.get(position).getOrg_code());
        cv.put("REG_OFFICE_1",data.get(position).getReg_office_1());
        cv.put("REG_OFFICE_2",data.get(position).getReg_office_2());
        cv.put("TEL_NO",data.get(position).getTel_no());
        long result = db.insert("Address",null, cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean remove() {
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        db.delete("Address",null,null);
        return true;
    }

}
