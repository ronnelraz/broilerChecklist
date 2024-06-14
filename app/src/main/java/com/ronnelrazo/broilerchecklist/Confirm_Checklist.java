package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_confirm_checklist;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.database.OfflineData;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.model_confirm_list;
import com.ronnelrazo.broilerchecklist.model.model_signature_list;
import com.ronnelrazo.broilerchecklist.model.model_signatures;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Confirm_Checklist extends AppCompatActivity {

    private Func func;
    private SharedPref sharedPref;
    private OfflineData offlineData;

    private RecyclerView recycler_view;
    private List<model_confirm_list> list = new ArrayList<>();

    private Adapter_confirm_checklist adapter;



    private ImageView back,logout;
    private TextView title,subtitle;

    private LinearLayout nodata;

    private EditText search,audit_date;


    public ExtendedFloatingActionButton confirm;

    List<model_signatures> signaturesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_confirm_checklist);

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

        title.setText("Confirm Checklist");
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
                signaturesList.clear();
                func.Razo_dialog(
                        view.getContext(),
                        Func.HELP,
                        "Confirmation",
                        "Are you sure you want to confirm?",
                        null,
                        null);
                func.negative_dialog.setOnClickListener(v -> {
                    signaturesList.clear();
                    func.signature_dialog.dismiss();
                });
                func.positive_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                    signaturesList.clear();
                    showSignatureDialog(v.getContext(),"Prepared By:",0);
                });



            }
        });

    }

    private void showSignatureDialog(Context context, String s, int position){
       if(position == 0){
           func.signature_dialog(context,s,null,null);
           func.label_signature_user.setText("Animal Husbandry");
           func.sign_negative_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   func.signature_dialog.dismiss();
               }
           });
           func.sign_positive_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String sign = saveSignature();
                   String user = func.signature_username.getText().toString();

                   if(func.signaturePad.isSignatureEmpty()){
                       func.toast(R.drawable.close,"No signature", Gravity.BOTTOM|Gravity.CENTER,0,50);
                   }
                   else if(TextUtils.isEmpty(user)){
                       func.toast(R.drawable.close,"Please Enter your Full Name", Gravity.BOTTOM|Gravity.CENTER,0,50);
                       func.signature_username.requestFocus();
                   }
                   else{
                       model_signatures item = new model_signatures(
                               position,sign,user
                       );
                       signaturesList.add(item);
                       func.signature_dialog.dismiss();
                       showSignatureDialog(context, "Acknowledge By:",1);

                   }



               }
           });
       }
       else if(position == 1){
           func.signature_dialog(context,s,null,null);
           func.label_signature_user.setText("Salesman");
           func.sign_negative_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   func.signature_dialog.dismiss();
               }
           });
           func.sign_positive_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                   String sign = saveSignature();
//                   String user = func.signature_username.getText().toString();
//
//                   if(func.signaturePad.isSignatureEmpty()){
//                       func.toast(R.drawable.close,"No signature", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                   }
//                   else if(TextUtils.isEmpty(user)){
//                       func.toast(R.drawable.close,"Please Enter your Full Name", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                       func.signature_username.requestFocus();
//                   }
//                   else{
//
//
//                   }

                   model_signatures item = new model_signatures(
                           position,null,null
                   );
                   signaturesList.add(item);
                   func.signature_dialog.dismiss();
                   showSignatureDialog(context, "Acknowledge By:",2);



               }
           });
       }
       else{
           func.signature_dialog(context,s,null,null);
           func.label_signature_user.setText("Farm Manager/Farm Owner");
           func.sign_negative_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   func.signature_dialog.dismiss();
               }
           });
           func.sign_positive_dialog.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String sign = saveSignature();
                   String user = func.signature_username.getText().toString();

                   if(func.signaturePad.isSignatureEmpty()){
                       func.toast(R.drawable.close,"No signature", Gravity.BOTTOM|Gravity.CENTER,0,50);
                   }
                   else if(TextUtils.isEmpty(user)){
                       func.toast(R.drawable.close,"Please Enter your Full Name", Gravity.BOTTOM|Gravity.CENTER,0,50);
                       func.signature_username.requestFocus();
                   }
                   else{
                       model_signatures item = new model_signatures(
                               position,sign,user
                       );
                       signaturesList.add(item);
                       func.signature_dialog.dismiss();


                       List<model_confirm_list> selectedItems = adapter.getSelectedItems();
                       for (int x = 0; x < selectedItems.size(); x++) {
                           int trHeaderId = selectedItems.get(x).getId();
                           String auditDate = selectedItems.get(x).getAudit_date();
                           String farmOrg = selectedItems.get(x).getHouse_flock().split("-")[0];
                           String houseFlock = selectedItems.get(x).getHouse_flock();
                           String prepare_sign = signaturesList.get(0).getSignature();
                           String prepare_user = signaturesList.get(0).getUsername();
                           String saleman_sign = signaturesList.get(1).getSignature();
                           String saleman_user = signaturesList.get(1).getUsername();
                           String farm_sign = signaturesList.get(2).getSignature();
                           String farm_user = signaturesList.get(2).getUsername();

                           boolean save  = offlineData.insertSignatureDetails(
                                   String.valueOf(trHeaderId),
                                   auditDate,
                                   farmOrg,
                                   houseFlock,
                                   prepare_sign,
                                   prepare_user,
                                   saleman_sign,
                                   saleman_user,
                                   farm_sign,
                                   farm_user
                           );
                           func.log(save ? "Saved":"error");


                       }

                    for (int x = 0; x < selectedItems.size(); x++) {

                        if(selectedItems.get(x).isChecked()){
                            offlineData.confirm(selectedItems.get(x).getId());
                            selectedItems.remove(x);
                            adapter.notifyItemRemoved(x);
                            loadData();
                            func.alert_dialog.dismiss();

                        }
                        // Check if this is the last item in the loop
                        if (x == selectedItems.size() - 1) {
                            offlineData.confirm(selectedItems.get(x).getId());
                            func.alert_dialog.dismiss();
                            selectedItems.remove(x);
                            adapter.notifyItemRemoved(x);
                            loadData();
                            func.alert_dialog.dismiss();
                        }
                    }


                   }



               }
           });
       }
    }



    private String saveSignature() {
        Bitmap signatureBitmap = func.signaturePad.getBitmap();
        if (signatureBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return base64Image; // Return the Base64 encoded image string
        } else {
            return null; // Return null if there's no signature bitmap
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
        list.clear();
        List<model_confirm_list> filteredList = new ArrayList<>();
        for (model_confirm_list data : list) {
            // Implement your filtering logic here
            if (data.getFarm_name().toLowerCase().contains(searchText.toLowerCase()) ||
                    data.getAudit_date().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(data);
            }
        }

        // Pass selected items to the new adapter instance
        adapter = new Adapter_confirm_checklist(filteredList, getApplicationContext(), func, this);
        adapter.setSelectedItems(list);
        recycler_view.setAdapter(adapter);
    }


    protected void loadData() {
        list.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Call notifyDataSetChanged() if adapter is not null
        }
        Cursor cursor = offlineData.Editlist_menu();
        if (cursor != null && cursor.moveToFirst()) {
            do {

                model_confirm_list item = new model_confirm_list(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13).equals("Y"),
                        false
                );
                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
            nodata.setVisibility(View.GONE);
            adapter = new Adapter_confirm_checklist(list, getApplicationContext(),func,this);
            recycler_view.setAdapter(adapter);
        } else {
            nodata.setVisibility(View.VISIBLE);
            confirm.hide();
        }
    }

    public void back(View view) {
        func.intent(Menu.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }


    public void clear(View view) {
        list.clear();
        search.setText("");
        audit_date.setText("");
        loadData();
        adapter.setAllCheckedFalse();
        confirm.hide();
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