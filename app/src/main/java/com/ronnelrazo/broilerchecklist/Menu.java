package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_CBH;
import com.ronnelrazo.broilerchecklist.API.API_PDF;
import com.ronnelrazo.broilerchecklist.API.API_Response_Address;
import com.ronnelrazo.broilerchecklist.API.API_Response_Farm;
import com.ronnelrazo.broilerchecklist.API.API_Response_checklist;
import com.ronnelrazo.broilerchecklist.API.API_Response_reason;
import com.ronnelrazo.broilerchecklist.API.API_SIGNATURE;
import com.ronnelrazo.broilerchecklist.API.API_TRN_DETAILS;
import com.ronnelrazo.broilerchecklist.API.API_TRN_HEADER;
import com.ronnelrazo.broilerchecklist.database.Download_Checklist;

import com.ronnelrazo.broilerchecklist.database.Download_Farm;
import com.ronnelrazo.broilerchecklist.database.Download_Reason_code;
import com.ronnelrazo.broilerchecklist.database.Download_address;
import com.ronnelrazo.broilerchecklist.database.OfflineData;
import com.ronnelrazo.broilerchecklist.func.AppUpdater;
import com.ronnelrazo.broilerchecklist.func.NetworkUtil;
import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;
import com.ronnelrazo.broilerchecklist.model.Checklist_address;
import com.ronnelrazo.broilerchecklist.model.Checklist_data;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;
import com.ronnelrazo.broilerchecklist.datalist.menu_setup;
import com.ronnelrazo.broilerchecklist.model.model_checklist_reasons;


import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Menu extends AppCompatActivity implements NetworkUtil.NetworkStatusListener {
    private Func func;
    private SharedPref sharedPref;

    private RecyclerView recycler_view;

    private NetworkUtil networkUtil;


    private OfflineData offlineData;
    private AppUpdater appUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_menu);
        func = new Func(this);
        sharedPref = new SharedPref(this);
        offlineData = new OfflineData(this);
        new Database(this).getReadableDatabase();

        recycler_view = findViewById(R.id.recycler_view);
        networkUtil = new NetworkUtil(this, this);



        // Initialize AppUpdater with update URL
        String updateUrl = "https://agro.cpf-phil.com/cpos/api/latest_version"; // Replace with your update URL
        appUpdater = new AppUpdater(this, updateUrl);

        // Check for updates when activity starts
        appUpdater.checkForUpdate();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkUtil.unregisterReceiver();
        appUpdater.cleanup();
    }


    @Override
    public void onNetworkStatusChanged(int status) {
        switch (status) {
            case 1:
                func.log("Connected to WiFi");
                new menu_setup().Menu(recycler_view,true,this);
                break;
            case 2:
                func.log("Connected to Mobile Data");
                new menu_setup().Menu(recycler_view,true,this);
                break;
            case 3:
                func.log("Disconnected");
                new menu_setup().Menu(recycler_view,false,this);
                break;
            default:
                // Handle any other status if needed
                break;
        }
    }

    @Override
    public void onNetworkStable() {
        func.toast(R.drawable.icons8_synchronize,"Network is stable.", Gravity.BOTTOM|Gravity.CENTER,0,50);
    }

    @Override
    public void onNetworkUnstable() {
        func.toast(R.drawable.icons8_synchronize,"Network is unstable", Gravity.BOTTOM|Gravity.CENTER,0,50);
    }


    public void download_Data(Context context){
        func.log("start downloading");
        func.download_dialog(context,Func.DOWNLOAD,"Syncing data",null,null,"Sync");
        func.downlaod_progress.setText(MessageFormat.format("0/{0}", 0));
        func.logo_dialog.setImageResource(R.drawable.icons8_download);
        func.loading_negative_dialog.setOnClickListener(v -> {
            func.Razo_dialog(
                    this,
                    Func.HELP,
                    "Cancel",
                    "Are you sure you want to cancel syncing data",
                    null,
                    null);

            // Set click listener for negative button
            func.negative_dialog.setOnClickListener(v1 -> {
                func.alert_dialog.dismiss();
            });

            // Set click listener for positive button
            func.positive_dialog.setOnClickListener(v1 -> {
                func.alert_dialog.dismiss();
                func.loading_alert_dialog.dismiss();

            });
        });
        func.loading_positive_dialog.setOnClickListener(v ->{
            func.loading_positive_dialog.setEnabled(false);
            func.downlaod_progress.setText(MessageFormat.format("0/{0}", 0));
            func.logo_dialog.setImageResource(R.drawable.icons8_download);
            func.downlaod_progress.setTextColor(getResources().getColor(R.color.black));
            String getOrg_Wip = SharedPref.getORG_CODE();
            func.log(getOrg_Wip);
            DownloadFarm(getOrg_Wip,v.getContext());
            DownloadAddress(getOrg_Wip,v.getContext());
            DownloadReason(getOrg_Wip,v.getContext());
            upload_trn_header_sync(); //upload online
            API.getClient().Broiler_Checklist(getOrg_Wip).enqueue(new Callback<API_Response_checklist>() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onResponse(Call<API_Response_checklist> call, Response<API_Response_checklist> response) {
                    if (response.isSuccessful()) {
                        API_Response_checklist res = response.body();
                        if (res != null && res.isSuccess()) {
                            List<Checklist_data> checklistItems = res.getChecklistItems();
                            if (checklistItems != null) {
                                boolean remove = new Download_Checklist(context).remove();
                                if(remove){
                                    new AsyncTask<Void, Integer, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            for (int i = 0; i < checklistItems.size(); i++) {
                                                // Access individual checklist item properties here
                                                Checklist_data item = checklistItems.get(i);

                                                String orgWip = item.getOrg_wip();
                                                String branchName = item.getBranch_name();
                                                String farmCode = item.getFarm_code();
                                                String farmHouseFlock = item.getFarm_house_flock();
                                                String farmName = item.getFarm_name();
                                                String dateIn = item.getDate_in();
                                                String chickAge = item.getChick_age();
                                                String currentBalance = item.getCurrent_balance();

                                                new Download_Checklist(context).setData(i, checklistItems);

                                                // Publish progress to update the UI
                                                publishProgress(i + 1);

                                                // Add a delay of 1 second (1000 milliseconds) between each iteration
                                                try {
                                                    Thread.sleep(50); // Adjust delay time as needed
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void onProgressUpdate(Integer... values) {
                                            super.onProgressUpdate(values);
                                            int progress = values[0];
                                            func.download_progressbar.setProgress(progress);
                                            func.downlaod_progress.setText(MessageFormat.format("{0}/{1}", progress, checklistItems.size()));
                                        }


                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            // Hide the progress bar once the download is complete
                                            func.downlaod_progress.setText("sync complete");
                                            func.downlaod_progress.setTextColor(getResources().getColor(R.color.btn_primary));
                                            func.loading_positive_dialog.setEnabled(true);
                                            func.loading_positive_dialog.setText("OK");
                                            func.loading_positive_dialog.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(func.loading_positive_dialog.getText().equals("OK")){
                                                        func.loading_alert_dialog.dismiss();
                                                    }
                                                }
                                            });

                                            func.logo_dialog.setImageResource(R.drawable.icons8_cloud_done);
                                            func.logo_dialog.startAnimation(func.fadeIn());
                                        }
                                    }.execute();
                                }
                                else{
                                    func.log("Error: Data removal was not successful.");
                                }
                            } else {
                                // Handle case where checklistItems is null
                                func.log("Checklist items are null");
                            }

                        } else {
                            String errorMessage = "Unknown error occurred";
                            if (res != null && res.getMessage() != null) {
                                errorMessage = res.getMessage();
                            }
                            func.log(errorMessage);
                            func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);

                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<API_Response_checklist> call, Throwable t) {
                    func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    func.log("OnFailure : " + t.getMessage());

                }
            });
        });

    }

    protected void DownloadFarm(String getOrg_Wip, Context context){
        API.getClient().Farm_Broiler_Checklist(getOrg_Wip).enqueue(new Callback<API_Response_Farm>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_Farm> call, Response<API_Response_Farm> response) {
                if (response.isSuccessful()) {
                    API_Response_Farm res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_farm_data> checklistItems = res.getChecklistFarmData();
                        if (checklistItems != null) {
                            boolean remove = new Download_Farm(context).remove();
                            if(remove){

                                    for (int i = 0; i < checklistItems.size(); i++) {
                                        // Access individual checklist item properties here
                                        Checklist_farm_data item = checklistItems.get(i);
                                        new Download_Farm(context).setData(i,checklistItems);
                                    }

                            }
                            else{
                                func.log("Error: Data removal was not successful.");
                            }
                        } else {
                            // Handle case where checklistItems is null
                            func.log("Checklist items are null");
                        }

                    } else {
                        String errorMessage = "Unknown error occurred";
                        if (res != null && res.getMessage() != null) {
                            errorMessage = res.getMessage();
                        }
                        func.log(errorMessage);
                        func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_Response_Farm> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }


    protected void DownloadAddress(String getOrg_Wip, Context context){
        API.getClient().Address_Broiler_Checklist(getOrg_Wip).enqueue(new Callback<API_Response_Address>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_Address> call, Response<API_Response_Address> response) {
                if (response.isSuccessful()) {
                    API_Response_Address res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_address> checklistItems = res.getChecklistAddresses();
                        if (checklistItems != null) {
                            boolean remove = new Download_Farm(context).remove();
                            if(remove){

                                for (int i = 0; i < checklistItems.size(); i++) {
                                    // Access individual checklist item properties here
                                    Checklist_address item = checklistItems.get(i);
                                    new Download_address(context).setData(i,checklistItems);
                                }

                            }
                            else{
                                func.log("Error: Data removal was not successful.");
                            }
                        } else {
                            // Handle case where checklistItems is null
                            func.log("Checklist items are null");
                        }

                    } else {
                        String errorMessage = "Unknown error occurred";
                        if (res != null && res.getMessage() != null) {
                            errorMessage = res.getMessage();
                        }
                        func.log(errorMessage);
                        func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_Response_Address> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }


    protected void DownloadReason(String getOrg_Wip, Context context){
        API.getClient().REASON_Broiler_Checklist(getOrg_Wip).enqueue(new Callback<API_Response_reason>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_reason> call, Response<API_Response_reason> response) {
                if (response.isSuccessful()) {
                    API_Response_reason res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_Reason> checklistItems = res.getChecklistReasons();
                        if (checklistItems != null) {
                            boolean remove = new Download_Reason_code(context).remove();
                            if(remove){

                                for (int i = 0; i < checklistItems.size(); i++) {
                                    // Access individual checklist item properties here
                                    Checklist_Reason item = checklistItems.get(i);
                                    new Download_Reason_code(context).setData(i,checklistItems);
                                }

                            }
                            else{
                                func.log("Error: Data removal was not successful.");
                            }
                        } else {
                            // Handle case where checklistItems is null
                            func.log("Checklist items are null");
                        }

                    } else {
                        String errorMessage = "Unknown error occurred";
                        if (res != null && res.getMessage() != null) {
                            errorMessage = res.getMessage();
                        }
                        func.log(errorMessage);
                        func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_Response_reason> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }


    public void upload_trn_header_sync() {

        Cursor cursor = offlineData.upload_trn_header();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                UPLOAD_TRN_HEADER_SYNC(
                        cursor.getInt(0),
                        sharedPref.getUSER(),
                        cursor.getString(1),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15),
                        cursor.getString(16)
                );

                // Add delay here
                try {
                    Thread.sleep(3000); // Delay in milliseconds (adjust as needed)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

            cursor.close();
        } else {

        }
    }


    protected void UPLOAD_TRN_HEADER_SYNC(
            int id,String username,String audit_date,String farm_org,
            String farm_name, String stock_balance, String datein,
            String age, String house_flock, String broiler_c,
            String broiler_w, String broiler_reject_c, String broiler_reject_w,
            String auto_count, String cancel_flag, String confirm_flag, String createDate
    ){

        func.log(id + " " +
                username + " " +
                audit_date + " " +
                farm_org + " " +
                farm_name + " " +
                stock_balance + " " +
                datein + " " +
                age + " " +
                house_flock + " " +
                broiler_c + " " +
                broiler_w + " " +
                broiler_reject_c + " " +
                broiler_reject_w + " " +
                auto_count + " " +
                cancel_flag + " " +
                confirm_flag + " " +
                createDate);

        API.getClient().INSERT_HEADER_Broiler_Checklist(username,audit_date,farm_org,farm_name,
                stock_balance,datein,age,house_flock,broiler_c,broiler_w,broiler_reject_c,broiler_reject_w,
                auto_count,cancel_flag,confirm_flag,createDate).enqueue(new Callback<API_TRN_HEADER>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_TRN_HEADER> call, Response<API_TRN_HEADER> response) {
                if (response.isSuccessful()) {
                    API_TRN_HEADER res = response.body();
                    if (res != null && res.isSuccess()) {
                        func.log(res.getMessage());
                        upload_trn_Detail(id,res.getDocument_no());
                        upload_trn_signature(id,res.getDocument_no());


                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_TRN_HEADER> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }

    public void upload_trn_Detail(int id,String document_no) {

        Cursor cursor = offlineData.upload_trn_details(id);
        if (cursor != null && cursor.moveToFirst()) {
            int index = 0;
            do {
                index++;
                func.log(
                        document_no + " " +
                        index + " " +
                        cursor.getInt(1) + " " +
                        cursor.getString(2) + " " +
                        cursor.getString(3) + " " +
                        cursor.getInt(4) + " " +
                        cursor.getString(5) + " " +
                        cursor.getString(6) + " " +
                        cursor.getString(7)
                        );
                UPLOAD_TRN_DETAILS_SYNC(document_no,cursor.getString(4),
                        index,cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(1),cursor.getString(5),cursor.getString(6),cursor.getString(7));
            } while (cursor.moveToNext());

            cursor.close();
        }
        else {
            func.alert_dialog.dismiss();

        }
    }

    protected void UPLOAD_TRN_DETAILS_SYNC(
            String document_no, String audit_date,int ext,
            String reason_id, String reason_name, String bird_count,
            String org_wip, String farm_org, String house_flock
    ){

        API.getClient().INSERT_DETAILS_Broiler_Checklist(document_no,audit_date, String.valueOf(ext),reason_id,reason_name,bird_count,org_wip,farm_org,house_flock).enqueue(new Callback<API_TRN_DETAILS>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_TRN_DETAILS> call, Response<API_TRN_DETAILS> response) {
                if (response.isSuccessful()) {
                    API_TRN_DETAILS res = response.body();
                    if (res != null && res.isSuccess()) {
                        func.log(res.getMessage());
                    } else {


                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_TRN_DETAILS> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }


    public void upload_trn_signature(int id,String document_no) {

        Cursor cursor = offlineData.upload_trn_signature(id);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                UPLOAD_TRN_SIGNATURE_SYNC(
                        document_no,
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4) ,
                                cursor.getString(6) ,
                                cursor.getString(5),
                                cursor.getString(8) ,
                                cursor.getString(7),
                                cursor.getString(10),
                                cursor.getString(9)
                );

            } while (cursor.moveToNext());

            cursor.close();
        }
        else {
            func.alert_dialog.dismiss();

        }
    }

    protected void UPLOAD_TRN_SIGNATURE_SYNC(
         String document_no, String audit_date, String farm_org,
         String house_flock, String preparedby, String preparedSign,
         String salesman, String salesmanSign, String farmManager,
         String farmManagerSign
    ){

        API.getClient().INSERT_signature_Broiler_Checklist(
                document_no,audit_date,farm_org,house_flock,preparedby,preparedSign,salesman,salesmanSign,farmManager,farmManagerSign
        ).enqueue(new Callback<API_SIGNATURE>() {
            @Override
            public void onResponse(Call<API_SIGNATURE> call, Response<API_SIGNATURE> response) {
                if (response.isSuccessful()) {
                    API_SIGNATURE res = response.body();
                    if (res != null && res.isSuccess()) {
                        func.log(res.getMessage());
                        GENERATE_PDF(document_no);
                    } else {


                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_SIGNATURE> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure Signature : " + t.getMessage());

            }
        });
    }


    protected void GENERATE_PDF(String document_no){
        API_CBH.getClient().pdf(document_no
        ).enqueue(new Callback<API_PDF>() {
            @Override
            public void onResponse(Call<API_PDF> call, Response<API_PDF> response) {
                if (response.isSuccessful()) {
                    API_PDF res = response.body();
                    if (res != null && res.isSuccess()) {
                        func.log(res.getMessage());
                    } else {
                        func.log(res.getMessage());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_PDF> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure Signature : " + t.getMessage());

            }
        });
    }


    public void Logout(View view) {
        func.Razo_dialog(
                view.getContext(),
                Func.HELP,
                "Logout",
                "Are you sure you want to logout your account?",
                null,
                null);
        func.negative_dialog.setOnClickListener(v -> {
            func.alert_dialog.dismiss();
        });
        func.positive_dialog.setOnClickListener(v -> {
            func.alert_dialog.dismiss();
            sharedPref.setKeepLogin(false);
            func.intent(MainActivity.class,this, R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        func.Razo_dialog(
                this,
                Func.HELP,
                "Exit",
                "Are you sure you want to exit this app?",
                null,
                null);

        // Set click listener for negative button
        func.negative_dialog.setOnClickListener(v -> {
            func.alert_dialog.dismiss();
        });

        // Set click listener for positive button
        func.positive_dialog.setOnClickListener(v -> {
            func.alert_dialog.dismiss();
            finish();
        });
    }

}