package com.ronnelrazo.broilerchecklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.Checklist_address;

import java.util.List;

public class OfflineData {

    private Context context;

    public OfflineData(Context context) {
        this.context = context;
    }

    public Cursor offline_farm_list(){
        String query = "SELECT farm_code,farm_name from farm order by farm_name asc";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public Cursor offline_farm_house_flock_list(String farm_code) {
        String query = "SELECT farm_house_flock, farm_name FROM checklist_data WHERE farm_code = ? ORDER BY farm_house_flock ASC";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{farm_code});
        }
        return cursor;
    }


    public Cursor offline_reason_code_list() {
        String query = "SELECT * from REASON_CODE order by reason_id asc";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor upload_trn_header() {
        String query = "SELECT * from transaction_header where confirm_flag = 'Y'\n" +
                "and date(substr(audit_date, 7, 4) || '-' || substr(audit_date, 4, 2) || '-' || substr(audit_date, 1, 2)) >= date('now', '-30 days')";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }



    public Cursor offline_transaction_list(){
        String query = "SELECT ID, AUDIT_DATE,farm_org,org_code,farm_name,house_flock, cancel_flag,confirm_flag,'' as approveby   FROM TRANSACTION_HEADER where confirm_flag = 'Y' order by id desc";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    public Cursor upload_trn_details(int id) {
        String query = "SELECT a.SEQ, a.bird_count, a.reason_id, a.reason,b.AUDIT_DATE,b.ORG_CODE,b.FARM_ORG,b.HOUSE_FLOCK\n" +
                "FROM TRANSACTION_DETAILS a " +
                "join TRANSACTION_HEADER b " +
                "on b.id = a.TR_HEADER_ID " +
                "WHERE a.tr_header_id = ? " +
                "ORDER BY CASE WHEN a.reason_id = 'R999' THEN 1 ELSE 0 END, a.SEQ ASC";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        }
        return cursor;
    }

    public Cursor upload_trn_signature(int id) {
        String query = "SELECT * from signature where tr_header_id = ?";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        }
        return cursor;
    }




    public Cursor offline_header_list(String farm_code) {
        String query = "SELECT date_in,chick_age,current_balance from checklist_data where farm_house_flock = ?";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{farm_code});
        }
        return cursor;
    }


    public Cursor EditList_menu_details(int id) {
        String query = "SELECT SEQ, bird_count, reason_id, reason,ID " +
                "FROM TRANSACTION_DETAILS " +
                "WHERE tr_header_id = ? " +
                "ORDER BY CASE WHEN reason_id = 'R999' THEN 1 ELSE 0 END, SEQ ASC";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        }
        return cursor;
    }



    public void deleteTransactionDetails(String id) {
        SQLiteDatabase db = new Database(context).getWritableDatabase();
        String[] whereArgs = {id};
        db.delete("transaction_details", "id = ? AND reason_id = 'R999'", whereArgs);
        db.close();
    }


    public Cursor Editlist_menu(){
        String query = "SELECT * from transaction_header where confirm_flag = 'N'";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor Editlist_cancel_menu(){
        String query = "SELECT * from transaction_header where confirm_flag = 'Y' and cancel_flag = 'N' order by id desc";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    private String getID() {
        String query = "SELECT max(id) FROM transaction_header";
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        Cursor cursor = null;
        String maxId = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                maxId = cursor.getString(0); // Get the value from the first column
            }

            if (cursor != null) {
                cursor.close(); // Close the cursor to release resources
            }
        }

        return maxId;
    }


    public boolean confirm(long tr_header_id) {
        SQLiteDatabase db = new Database(context).getWritableDatabase();

        // Construct the ContentValues object with the new values
        ContentValues cv = new ContentValues();
        cv.put("confirm_flag", "Y");

        // Specify the WHERE clause to update existing rows
        String whereClause = "id = ?";
        // Specify the values for the WHERE clause
        String[] whereArgs = {String.valueOf(tr_header_id)};

        // Execute the update query
        int rowsAffected = db.update("TRANSACTION_HEADER", cv, whereClause, whereArgs);

        // Check if rows were affected
        return rowsAffected > 0;
    }


    public boolean cancel(long tr_header_id) {
        SQLiteDatabase db = new Database(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cancel_flag", "Y");
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(tr_header_id)};
        int rowsAffected = db.update("TRANSACTION_HEADER", cv, whereClause, whereArgs);

        // Check if rows were affected
        return rowsAffected > 0;
    }


    public boolean insertSignatureDetails(
            String trHeaderId,
            String auditDate,
            String farmOrg,
            String houseFlock,
            String preparedSign,
            String preparedByUser,
            String salesmanSign,
            String salesmanUser,
            String farmManagerSign,
            String farmManagerUser) {

        SQLiteDatabase db = new Database(context).getWritableDatabase(); // Use getWritableDatabase() to allow writing to the database
        ContentValues cv = new ContentValues();
        cv.put("TR_HEADER_ID", trHeaderId);
        cv.put("AUDIT_DATE", auditDate);
        cv.put("FARM_ORG", farmOrg);
        cv.put("HOUSE_FLOCK", houseFlock);
        cv.put("PREPARED_SIGN", preparedSign);
        cv.put("PREPAREDBY_USER", preparedByUser);
        cv.put("SALEMAN_SIGN", salesmanSign);
        cv.put("SALEMAN_USER", salesmanUser);
        cv.put("FARM_MANAGER_SIGN", farmManagerSign);
        cv.put("FARM_MANAGERUSER", farmManagerUser);
        cv.put("CREATE_DATE", Func.getInstance(context).currentDate());

        long result = db.insertWithOnConflict(
                "SIGNATURE",
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE); // Replace existing row on conflict

        return result != -1; // If result is -1, insertion failed, otherwise successful
    }



    public boolean Insert_transaction_details(
            int seq,
            long bird_count,
            String reason_id,
            String reason){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TR_HEADER_ID",getID());
        cv.put("SEQ",seq);
        cv.put("BIRD_COUNT",bird_count);
        cv.put("REASON_ID",reason_id);
        cv.put("REASON",reason);
        long result = db.insertWithOnConflict(
                "TRANSACTION_DETAILS",
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE); // Replace existing row on conflict

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean Insert_transaction_details_new_update(
            int id,
            int seq,
            long bird_count,
            String reason_id,
            String reason){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TR_HEADER_ID",id);
        cv.put("SEQ",seq);
        cv.put("BIRD_COUNT",bird_count);
        cv.put("REASON_ID",reason_id);
        cv.put("REASON",reason);
        long result = db.insertWithOnConflict(
                "TRANSACTION_DETAILS",
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE); // Replace existing row on conflict

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean Update_transaction_details(
            long tr_header_id,
            int seq,
            long bird_count,
            String reason_id,
            String reason) {

        SQLiteDatabase db = new Database(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BIRD_COUNT", bird_count);
        cv.put("REASON_ID", reason_id);
        cv.put("REASON", reason);

        // Specify the WHERE clause to update existing rows
        String whereClause = "TR_HEADER_ID = ? AND SEQ = ? AND REASON_ID = ?";
        // Specify the values for the WHERE clause
        String[] whereArgs = {String.valueOf(tr_header_id), String.valueOf(seq), reason_id};

        int rowsAffected = db.update("TRANSACTION_DETAILS", cv, whereClause, whereArgs);

        // Check if rows were affected
        return rowsAffected > 0;
    }





    public boolean Insert_transaction_header(
            String audit_date,
            String org_code,
            String farm_org,
            String farm_name,
            String stock_balance,
            String date_in,
            String age,
            String house_flock,
            String broiler_count,
            String broiler_wgh,
            String broiler_reject_count,
            String broiler_reject_wgh,
            String auto_count){
        SQLiteDatabase db = new Database(context).getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AUDIT_DATE",audit_date);
        cv.put("ORG_CODE",org_code);
        cv.put("FARM_ORG",farm_org);
        cv.put("FARM_NAME",farm_name);
        cv.put("STOCK_BALANCE",stock_balance);
        cv.put("DATE_IN",date_in);
        cv.put("AGE",age);
        cv.put("HOUSE_FLOCK",house_flock);
        cv.put("BROILER_COUNT",broiler_count);
        cv.put("BROILER_WEIGHT",broiler_wgh);
        cv.put("BROILER_REJECT_COUNT",broiler_reject_count);
        cv.put("BROILER_REJECT_WEIGHT",broiler_reject_wgh);
        cv.put("BROILER_AUTO_COUNT_FLAG",auto_count);
        cv.put("CANCEL_FLAG","N");
        cv.put("CONFIRM_FLAG","N");
        cv.put("CREATE_DATE", Func.getInstance(context).currentDate());
        long result = db.insertWithOnConflict(
                "TRANSACTION_HEADER",
                null,
                cv,
                SQLiteDatabase.CONFLICT_REPLACE); // Replace existing row on conflict

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean Update_transaction_header(
            String id,
            String audit_date,
            String org_code,
            String farm_org,
            String farm_name,
            String stock_balance,
            String date_in,
            String age,
            String house_flock,
            String broiler_count,
            String broiler_wgh,
            String broiler_reject_count,
            String broiler_reject_wgh,
            String auto_count) {

        SQLiteDatabase db = new Database(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AUDIT_DATE", audit_date);
        cv.put("ORG_CODE", org_code);
        cv.put("FARM_ORG", farm_org);
        cv.put("FARM_NAME", farm_name);
        cv.put("STOCK_BALANCE", stock_balance);
        cv.put("DATE_IN", date_in);
        cv.put("AGE", age);
        cv.put("HOUSE_FLOCK", house_flock);
        cv.put("BROILER_COUNT", broiler_count);
        cv.put("BROILER_WEIGHT", broiler_wgh);
        cv.put("BROILER_REJECT_COUNT", broiler_reject_count);
        cv.put("BROILER_REJECT_WEIGHT", broiler_reject_wgh);
        cv.put("BROILER_AUTO_COUNT_FLAG", auto_count);

        // Specify the WHERE clause to update existing rows
        String whereClause = "CONFIRM_FLAG = ? AND ID = ? AND CANCEL_FLAG = ?";
        // Specify the values for the WHERE clause
        String[] whereArgs = {"N", id, "N"}; // Update with the appropriate values

        int rowsAffected = db.update("TRANSACTION_HEADER", cv, whereClause, whereArgs);

        // Check if rows were affected
        return rowsAffected > 0;
    }






}
