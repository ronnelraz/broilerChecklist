package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_Response_FHF;
import com.ronnelrazo.broilerchecklist.API.API_Response_Farm;
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_farm_list;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.database.Download_Farm;
import com.ronnelrazo.broilerchecklist.database.OfflineData;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.func.NetworkUtil;
import com.ronnelrazo.broilerchecklist.model.Checklist_FHF;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;
import com.ronnelrazo.broilerchecklist.model.model_farm;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Farm_menu extends AppCompatActivity implements NetworkUtil.NetworkStatusListener {
    private Func func;
    private SharedPref sharedPref;

    private ImageView back,logout;
    private TextView title,subtitle;

    private LinearLayout nodata;

    private NetworkUtil networkUtil;

    private RecyclerView recycler_view;
    private List<Checklist_farm_data> list = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    private EditText search;

    private OfflineData offlineData;

    public ArrayAdapter<String> farm_house_flock_adapterItems;
    public List<String> list_farm_house_flock = new ArrayList<>();

    private int online;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_farm_menu);

        func = new Func(this);
        sharedPref = new SharedPref(this);
        offlineData = new OfflineData(this);



        new Database(this).getReadableDatabase();
        networkUtil = new NetworkUtil(this, this);

        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        nodata = findViewById(R.id.nodata);
        search = findViewById(R.id.search);

        title.setText("Create");
        subtitle.setText("checklist before harvest");


        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemViewCacheSize(999999999);
        recycler_view.setLayoutManager(new GridLayoutManager(this,2));
//        adapter = new Adapter_farm_list(list,this,this,SharedPref.getORG_CODE());
//        recycler_view.setAdapter(adapter);

        //apply seach
        Search();



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
    }

    private void filterData(String searchText) {
        List<Checklist_farm_data> filteredList = new ArrayList<>();
        for (Checklist_farm_data data : list) {
            // Implement your filtering logic here
            if (data.getFarm_code().toLowerCase().contains(searchText.toLowerCase()) ||
                    data.getFarm_name().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(data);
            }
        }
        adapter = new Adapter_farm_list(filteredList, getApplicationContext(),this,SharedPref.getORG_CODE(),online);
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkUtil.unregisterReceiver();
    }

    @Override
    public void onNetworkStatusChanged(int status) {
        switch (status) {
            case 1:
                func.log("Connected to WiFi");
                  Farm_list_online(SharedPref.getORG_CODE());
                online = 2;
                break;
            case 2:
                func.log("Connected to Mobile Data");
                   Farm_list_online(SharedPref.getORG_CODE());
                online = 2;
                break;
            case 3:
                func.log("Disconnected");
                func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
                   Farm_list_offline();
                online = 1;
                break;
            default:
                func.log("disconnected other reason");
                // Handle any other status if needed
                func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
                Farm_list_offline();
                online = 1;
                break;
        }
    }


    @Override
    public void onNetworkStable() {
        func.toast(R.drawable.icons8_wi_fi,"Online", Gravity.BOTTOM|Gravity.CENTER,0,50);
//        Farm_list_online(SharedPref.getORG_CODE());
    }

    @Override
    public void onNetworkUnstable() {
        func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
        Farm_list_offline();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        func.back(this);
        finish();
    }



    protected void Farm_list_offline() {
        list.clear();
        // Check if adapter is null and initialize it if needed
        if (adapter != null) {
             adapter.notifyDataSetChanged(); // Call notifyDataSetChanged() if adapter is not null
        }

        if (func.farm_dialog != null && func.farm_dialog.isShowing()) {
            // Check if data has changed (replace this with your actual condition)
            func.farm_dialog.dismiss(); // Dismiss the dialog if data has changed
        }
        Cursor cursor = offlineData.offline_farm_list();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String farmCode = cursor.getString(0);
                String farmName = cursor.getString(1);
                Checklist_farm_data item = new Checklist_farm_data();
                item.setFarm_code(farmCode);
                item.setFarm_name(farmName);

                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
            nodata.setVisibility(View.GONE);
            adapter = new Adapter_farm_list(list, getApplicationContext(),this,SharedPref.getORG_CODE(),online);
            recycler_view.setAdapter(adapter);
        } else {
            nodata.setVisibility(View.VISIBLE);
        }
    }



    protected void Farm_list_online(String getOrg_Wip){
        func.loading(this);
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Call notifyDataSetChanged() if adapter is not null
        }
        if (func.farm_dialog != null && func.farm_dialog.isShowing()) {
            // Check if data has changed (replace this with your actual condition)
            func.farm_dialog.dismiss(); // Dismiss the dialog if data has changed
        }
        API.getClient().Farm_Broiler_Checklist(getOrg_Wip).enqueue(new Callback<API_Response_Farm>() {
            @Override
            public void onResponse(Call<API_Response_Farm> call, Response<API_Response_Farm> response) {
                if (response.isSuccessful()) {
                    API_Response_Farm res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_farm_data> checklistItems = res.getChecklistFarmData();
                        if (checklistItems != null) {
                            for (int i = 0; i < checklistItems.size(); i++) {
                                Checklist_farm_data obj = checklistItems.get(i);
                                list.add(obj);
                            }
                            adapter = new Adapter_farm_list(list,getApplicationContext(),Farm_menu.this,SharedPref.getORG_CODE(),online);
                            recycler_view.setAdapter(adapter);

                            func.alert_dialog.dismiss();
                            nodata.setVisibility(View.GONE);
                        } else {
                            func.log("Checklist items are null");
                            nodata.setVisibility(View.VISIBLE);
                        }

                    } else {
                        String errorMessage = "Unknown error occurred";
                        if (res != null && res.getMessage() != null) {
                            errorMessage = res.getMessage();
                        }
                        func.log(errorMessage);
                        func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);
                        nodata.setVisibility(View.VISIBLE);

                    }
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<API_Response_Farm> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());
                nodata.setVisibility(View.VISIBLE);

            }
        });
    }



    public void Farm_house_flock_offline(String farm_code,String getFarm_name,Context context) {
        list_farm_house_flock.clear();
        Cursor cursor = offlineData.offline_farm_house_flock_list(farm_code);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String flock = cursor.getString(0);
                Checklist_FHF item = new Checklist_FHF();
                item.setFARM_HOUSE_FLOCK(flock);
                list_farm_house_flock.add(item.getFARM_HOUSE_FLOCK());
            } while (cursor.moveToNext());
            cursor.close();


            func.farm_house_flock_dialog(context,null,null);
            func.audit_date.setText(func.currentDate());
            func.Farm_name.setText(getFarm_name);

            farm_house_flock_adapterItems = new ArrayAdapter<>(context,R.layout.flock_item,list_farm_house_flock);
            func.farm_house_flock.setAdapter(farm_house_flock_adapterItems);
            func.farm_house_flock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String getflock = list_farm_house_flock.get(position);
                    func.GetSelectedFlock = getflock;
                }
            });

            func.farm_negative_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list_farm_house_flock.clear();
                    func.farm_dialog.dismiss();

                }
            });

            func.farm_positive_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String getAuditDate = func.audit_date.getText().toString();


                    if(TextUtils.isEmpty(func.GetSelectedFlock)){
                        func.farm_house_flock.requestFocus();
                        func.toast(R.drawable.close, "Please Select House", Gravity.BOTTOM|Gravity.CENTER,0,50);
                    }
                    else{
                        func.farm_dialog.dismiss();
                        func.intent(Checklist.class,view.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                        Checklist.GET_FARM_NAME = getFarm_name;
                        Checklist.GET_HOUSE_FLOCK = func.GetSelectedFlock;
                        Checklist.GET_AUDIT_DATE = getAuditDate;
                        Checklist.GET_FARM_ORG = farm_code;
                        func.GetSelectedFlock = null;

                    }

                }
            });


        } else {
            func.alert_dialog.dismiss();

        }
    }

    public void Farm_house_flock_online(String getOrg_Wip, String farm_code,String getFarm_name,Context context){
        list_farm_house_flock.clear();

        func.loading(context);
        API.getClient().FHF_Broiler_Checklist(getOrg_Wip,farm_code).enqueue(new Callback<API_Response_FHF>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_FHF> call, Response<API_Response_FHF> response) {
                if (response.isSuccessful()) {
                    API_Response_FHF res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_FHF> checklistItems = res.getChecklistFhfs();
                        if (checklistItems != null) {
                            for (int i = 0; i < checklistItems.size(); i++) {
                                // Access individual checklist item properties here
                                Checklist_FHF item = checklistItems.get(i);
                                list_farm_house_flock.add(item.getFARM_HOUSE_FLOCK());
                                func.log(item.getFARM_HOUSE_FLOCK());
//                                new Download_address(context).setData(i,checklistItems);
                            }
                            func.alert_dialog.dismiss();
                            func.farm_house_flock_dialog(context,null,null);
                            func.audit_date.setText(func.currentDate());
                            func.Farm_name.setText(getFarm_name);

                            farm_house_flock_adapterItems = new ArrayAdapter<>(context,R.layout.flock_item,list_farm_house_flock);
                            func.farm_house_flock.setAdapter(farm_house_flock_adapterItems);
                            func.farm_house_flock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String getflock = list_farm_house_flock.get(position);
                                    func.GetSelectedFlock = getflock;
                                }
                            });

                            func.farm_negative_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    list_farm_house_flock.clear();
                                    func.farm_dialog.dismiss();

                                }
                            });

                            func.farm_positive_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String getAuditDate = func.audit_date.getText().toString();
                                    if(TextUtils.isEmpty(func.GetSelectedFlock)){
                                        func.farm_house_flock.requestFocus();
                                        func.toast(R.drawable.close, "Please Select House", Gravity.BOTTOM|Gravity.CENTER,0,50);
                                    }
                                    else{
                                        func.farm_dialog.dismiss();
                                        func.intent(Checklist.class,view.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                                        Checklist.GET_FARM_NAME = getFarm_name;
                                        Checklist.GET_HOUSE_FLOCK = func.GetSelectedFlock;
                                        Checklist.GET_AUDIT_DATE = getAuditDate;
                                        Checklist.GET_FARM_ORG = farm_code;
                                        func.GetSelectedFlock = null;
                                    }

                                }
                            });




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
            public void onFailure(Call<API_Response_FHF> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }

    public void back(View view) {
        func.intent(Menu.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }
}