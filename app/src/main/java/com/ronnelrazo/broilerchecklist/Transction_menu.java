package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_TRANSACTION;
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_cancel_checklist;
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_farm_list;
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_transaction_checklist;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.config.config;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.database.OfflineData;
import com.ronnelrazo.broilerchecklist.func.DownloadHelper;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.model_confirm_list;
import com.ronnelrazo.broilerchecklist.model.transaction_data;
import com.ronnelrazo.broilerchecklist.model.transaction_data_offline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transction_menu extends AppCompatActivity {

    private Func func;
    private SharedPref sharedPref;
    private OfflineData offlineData;

    private RecyclerView recycler_view;
    private List<transaction_data_offline> list = new ArrayList<>();
    private List<transaction_data> listOL = new ArrayList<>();

    private Adapter_transaction_checklist adapter;



    private ImageView back,logout;
    private TextView title,subtitle;

    private LinearLayout nodata;

    private EditText search,audit_date;


    public ExtendedFloatingActionButton confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_transaction_menu);

        func = new Func(this);
        sharedPref = new SharedPref(this);
        offlineData = new OfflineData(this);
        new Database(this).getReadableDatabase();


        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        nodata = findViewById(R.id.nodata);

        search = findViewById(R.id.search);
        audit_date = findViewById(R.id.audit_date);
        confirm = findViewById(R.id.confirm);

        confirm.hide();

        title.setText("Transaction Checklist");
        subtitle.setText("checklist before harvest");


        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(999999999);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        loadData();
        Search();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                func.Razo_dialog(
                        view.getContext(),
                        Func.HELP,
                        "Download",
                        "you want to download this files?",
                        null,
                        null);
                func.negative_dialog.setOnClickListener(v -> {

                });
                func.positive_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                    List<transaction_data_offline> selectedItems = adapter.getSelectedItems();
                    // Create a ProgressDialog
                    ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setTitle("Downloading Files");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(selectedItems.size());
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    for (int x = 0; x < selectedItems.size(); x++) {
//                        String documentNo = selectedItems.get(x).getDOCUMENT_NO();
                      downloadURL(progressDialog,selectedItems.get(x).getAUDIT_DATE(), selectedItems.get(x).getHOUSE_FLOCK(),selectedItems,x);
//                        Toast.makeText(Transction_menu.this, selectedItems.get(x).getAUDIT_DATE() + " " + selectedItems.get(x).getHOUSE_FLOCK(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });

    }

    public void downloadURL(ProgressDialog progressDialog,String audit_date, String flock,List<transaction_data_offline> selectedItems, int pos){
        API.getClient().TRANSACTION_Broiler_Checklist(SharedPref.getORG_CODE(),SharedPref.getUSER(),audit_date,flock).enqueue(new Callback<API_TRANSACTION>() {
            @Override
            public void onResponse(Call<API_TRANSACTION> call, Response<API_TRANSACTION> response) {
                if (response.isSuccessful()) {
                    API_TRANSACTION res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<transaction_data> checklistItems = res.getTransactionDataList();
                        if (checklistItems != null) {
                            String getDocument_No = checklistItems.get(0).getDocumentNo();
                            String getApproveFlag = checklistItems.get(0).getApprove_flag();
                            String getApproveBy = checklistItems.get(0).getApproveBy();

                            new DownloadTask(progressDialog).execute(getDocument_No);

                        } else {
                            Toast.makeText(Transction_menu.this, "Download Failed : " + selectedItems.get(pos).getFARM_NAME()  + " \n Farm House Flock : " + selectedItems.get(pos).getHOUSE_FLOCK() , Toast.LENGTH_LONG).show();
                        }

                    } else {


                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_TRANSACTION> call, Throwable t) {
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }

    private static class DownloadTask extends AsyncTask<String, Integer, Void> {
        private ProgressDialog progressDialog;

        public DownloadTask(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected Void doInBackground(String... params) {
            for (String documentNo : params) {
                // Download the file
                String url = config.DOWNLOAD + documentNo + ".pdf";
                String filename = documentNo;
                new DownloadHelper().downloadFile(progressDialog.getContext(), url, filename);

                try {
                    // Add a delay of 1 second (1000 milliseconds)
                    Thread.sleep(2000); // Adjust the delay time as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(); // Update progress
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(progressDialog.getProgress() + 1);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(progressDialog.getContext(), "All files downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }


    private void Search(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String searchText = s.toString();
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        audit_date.setOnClickListener(view -> {
            func.Audit_date_filter(audit_date);
        });

        audit_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String date = editable.toString();
                filterData(date);
            }
        });
    }

    private void filterData(String searchText) {
        List<transaction_data_offline> filteredList = new ArrayList<>();
        for (transaction_data_offline data : list) {
            // Implement your filtering logic here
            if (data.getFARM_NAME().toLowerCase().contains(searchText.toLowerCase()) ||
                    data.getAUDIT_DATE().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(data);
            }
        }

        // Pass selected items to the new adapter instance
        adapter = new Adapter_transaction_checklist(filteredList, getApplicationContext(), func, this);
        adapter.setSelectedItems(list);
        recycler_view.setAdapter(adapter);
    }


    protected void loadData() {
        list.clear();
        Cursor cursor = offlineData.offline_transaction_list();
        if (cursor != null && cursor.moveToFirst()) {
            do {

                transaction_data_offline item = new transaction_data_offline(
                        String.valueOf(cursor.getInt(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        "N",
                        "",
                        false
                );
                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
            adapter = new Adapter_transaction_checklist(list,getApplicationContext(),func,Transction_menu.this);
            recycler_view.setAdapter(adapter);
        } else {

        }
    }





//    public void loadData(Context context){
//        list.clear();
//        API.getClient().TRANSACTION_Broiler_Checklist(SharedPref.getORG_CODE(),SharedPref.getUSER()).enqueue(new Callback<API_TRANSACTION>() {
//
//            @Override
//            public void onResponse(Call<API_TRANSACTION> call, Response<API_TRANSACTION> response) {
//                if (response.isSuccessful()) {
//                    API_TRANSACTION res = response.body();
//                    if (res != null && res.isSuccess()) {
//                        List<transaction_data> checklistItems = res.getTransactionDataList();
//                        if (checklistItems != null) {
//
//                            for (int i = 0; i < checklistItems.size(); i++) {
//                                transaction_data obj = checklistItems.get(i);
//                                list.add(obj);
//                            }
//                            adapter = new Adapter_transaction_checklist(list,getApplicationContext(),func,Transction_menu.this);
//                            recycler_view.setAdapter(adapter);
//
//                        } else {
//                            // Handle case where checklistItems is null
//                            func.log("Checklist items are null");
//
//                        }
//
//                    } else {
//
//                        String errorMessage = "Unknown error occurred";
//                        if (res != null && res.getMessage() != null) {
//                            errorMessage = res.getMessage();
//                        }
//                        func.log(errorMessage);
//                        func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);
//
//                    }
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<API_TRANSACTION> call, Throwable t) {
//                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
//                func.log("OnFailure : " + t.getMessage());
//
//            }
//        });
//    }
//

    public void back(View view) {
        func.intent(Menu.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }


    public void clear(View view) {
        search.setText("");
        audit_date.setText("");
        filterData("");
        loadData();
        adapter.setAllCheckedFalse();
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

}