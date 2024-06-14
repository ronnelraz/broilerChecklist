package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_Response_header;
import com.ronnelrazo.broilerchecklist.API.API_Response_reason;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.database.OfflineData;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.func.NetworkUtil;
import com.ronnelrazo.broilerchecklist.model.Checklist_Header;
import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;
import com.ronnelrazo.broilerchecklist.model.model_checklist_reasons;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_checklist_form extends AppCompatActivity implements NetworkUtil.NetworkStatusListener {

    private Func func;
    private SharedPref sharedPref;
    private NetworkUtil networkUtil;
    private OfflineData offlineData;

    private ImageView back,logout;
    private TextView title,subtitle;

    private LinearLayout other_reason_container;
    private LinearLayout reason_code_parent;

    private TextView audit_date,date_in,balance,age;

    private CheckBox reason_checkbox;
    private TextView reason_name;

    private MaterialButton reason_addmore;

    public static String GET_FARM_NAME;
    public static String GET_HOUSE_FLOCK;
    public static String GET_AUDIT_DATE;

    public static String GET_FARM_ORG;

    private static String GET_STOCK_BAL,GET_DATE_IN,GET_AGE;

    private ShimmerFrameLayout loading_shimmer;

    // Define a list to keep track of added customLayouts
    List<LinearLayout> reasonList = new ArrayList<>();
    List<LinearLayout> other_reasonList = new ArrayList<>();


    private EditText broiler_birds,broiler_wgh,broiler_reject_birds,broiler_reject_wgh;
    private EditText reason_birds;
    private CheckBox broiler_reject_auto_count;

    private String Broiler_reject_bird_old,Broiler_reject_wgh_old;

    private  List<Checklist_Reason> checklistReasonList;

    private int TotalBroilerRejectCount,TotalBroilerRejectOtherCount = 0;

    private List<model_checklist_reasons> reasonsList,reasonsListEdit = new ArrayList<>();


    public static String GET_BROILER_COUNT,GET_BROILER_WGH;
    public static String GET_BROILER_REJECT_COUNT, GET_BROILER_REJECT_WGH;
    public static boolean GET_AUTO_COUNT;

    public static int GET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_edit_checklist_form);

        func = new Func(this);
        sharedPref = new SharedPref(this);
        offlineData = new OfflineData(this);



        new Database(this).getReadableDatabase();
        networkUtil = new NetworkUtil(this, this);

        back = findViewById(R.id.back);
        logout = findViewById(R.id.logout);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        audit_date = findViewById(R.id.audit_date);
        date_in = findViewById(R.id.date_in);
        balance = findViewById(R.id.balance);
        age = findViewById(R.id.age);

        other_reason_container = findViewById(R.id.other_reason_parent);
        reason_code_parent = findViewById(R.id.reason_code_parent);
        loading_shimmer = findViewById(R.id.loading_shimmer);

        broiler_birds = findViewById(R.id.broiler_birds);
        broiler_birds.setText(func.Decimalformat(GET_BROILER_COUNT));

        broiler_wgh = findViewById(R.id.broiler_wgh);
        broiler_wgh.setText(GET_BROILER_WGH);

        broiler_reject_birds = findViewById(R.id.broiler_reject_birds);
        broiler_reject_birds.setText(func.Decimalformat(GET_BROILER_REJECT_COUNT));

        broiler_reject_wgh = findViewById(R.id.broiler_reject_wgh);
        broiler_reject_wgh.setText(GET_BROILER_REJECT_WGH);

        broiler_reject_auto_count = findViewById(R.id.broiler_reject_auto_count);
        if (GET_AUTO_COUNT) {
            broiler_reject_auto_count.setChecked(true);
            broiler_reject_birds.setEnabled(false);
        } else {
            broiler_reject_auto_count.setChecked(false);
            broiler_reject_birds.setEnabled(true);
        }






        /**broiler**/
        func.InputFormmater(broiler_birds);
        func.WghChecker(broiler_wgh);
        func.CursorFocus(broiler_birds);
        func.CursorFocus(broiler_wgh);
//        /**broielr reject ***/
        func.InputFormmater(broiler_reject_birds);
        func.WghChecker(broiler_reject_wgh);
        func.CursorFocus(broiler_reject_birds);
        func.CursorFocus(broiler_reject_wgh);
        func.Ontextchanged(broiler_reject_birds);


        title.setText(GET_FARM_NAME);
        subtitle.setText(GET_HOUSE_FLOCK);
        audit_date.setText(func.header_html("Audit Date" , GET_AUDIT_DATE,""));


        //auto count
        broiler_reject_auto_count.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                TotalBroilerRejectCount = 0;
                broiler_reject_birds.setEnabled(false);
                int strokeColor = Color.parseColor("#A8ABAC");
                broiler_reject_birds.setBackgroundTintList(ColorStateList.valueOf(strokeColor));

                //save the old bird and wgh
                Broiler_reject_bird_old = broiler_reject_birds.getText().toString();
                Broiler_reject_wgh_old = broiler_reject_wgh.getText().toString();

                for (int i = 0; i < reason_code_parent.getChildCount(); i++) {
                    View childView = reason_code_parent.getChildAt(i);
                    if (childView.findViewById(R.id.reason_birds) != null) {
                        // If it's 'reason_item', retrieve the value of 'reason_birds'
                        EditText reason_birds = childView.findViewById(R.id.reason_birds);
                        TextView reason_name = childView.findViewById(R.id.reason_name);
                        String birdsValue = reason_birds.getText().toString();
                        String getreason_name = reason_name.getText().toString();
                        String season_id = checklistReasonList.get(i).getREASON_ID();

                        if(!TextUtils.isEmpty(birdsValue)){
                            TotalBroilerRejectCount += func.ConvertStringtoNumber(birdsValue);
                        }
                    }
                }

                for (int i = 0; i < other_reason_container.getChildCount(); i++) {
                    View childView = other_reason_container.getChildAt(i);
                    if (childView.findViewById(R.id.reason_birds) != null) {
                        // If it's 'reason_item', retrieve the value of 'reason_birds'
                        EditText reason_birds = childView.findViewById(R.id.reason_birds);
                        TextView other_reason = childView.findViewById(R.id.other_reason);
                        String birdsValue = reason_birds.getText().toString();
                        String getOtherReason = other_reason.getText().toString();


                        if(!TextUtils.isEmpty(birdsValue)){
                            TotalBroilerRejectCount += func.ConvertStringtoNumber(birdsValue);
                        }

                    }
                }

                func.log("Total Birds: " + TotalBroilerRejectCount);

                broiler_reject_birds.setText(TotalBroilerRejectCount == 0 ? "" : String.valueOf(TotalBroilerRejectCount));
                broiler_reject_wgh.setText("");

            }
            else{
                broiler_reject_birds.setEnabled(true);
                int strokeColor = Color.parseColor("#DAE5EA");
                broiler_reject_birds.setBackgroundTintList(ColorStateList.valueOf(strokeColor));

                //retrieve old data
                broiler_reject_birds.setText(Broiler_reject_bird_old);
                broiler_reject_wgh.setText(Broiler_reject_wgh_old);
            }
        });



//        HEADER_Broiler_Checklist_online();
        HEADER_Broiler_Checklist_offline();




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


    public void Transaction_details_edit() {

        reasonsListEdit.clear();
        Cursor cursor = offlineData.EditList_menu_details(GET_ID);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                 model_checklist_reasons reason = new model_checklist_reasons(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                 );

                reasonsListEdit.add(reason);
            } while (cursor.moveToNext());

            cursor.close();
        }
        else {
            func.alert_dialog.dismiss();

        }
    }

    public void HEADER_Broiler_Checklist_offline() {
        date_in.setText(func.header_html("Date In" , "loading",""));
        balance.setText(func.header_html("Stock Balance" , "loading",""));
        age.setText(func.header_html("Age" , "loading",""));

        Cursor cursor = offlineData.offline_header_list(GET_HOUSE_FLOCK);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                date_in.setText(func.header_html("Date In" , cursor.getString(0),""));
                balance.setText(func.header_html("Stock Balance" , func.NumberFormat(Integer.parseInt(cursor.getString(2)))," Birds"));
                age.setText(func.header_html("Age" , cursor.getString(1)," Days"));

                GET_DATE_IN = cursor.getString(0);
                GET_STOCK_BAL = cursor.getString(2);
                GET_AGE = cursor.getString(1);

            } while (cursor.moveToNext());

            cursor.close();
        }
        else {
            func.alert_dialog.dismiss();

        }
    }

    protected void HEADER_Broiler_Checklist_online(){
        date_in.setText(func.header_html("Date In" , "loading",""));
        balance.setText(func.header_html("Stock Balance" , "loading",""));
        age.setText(func.header_html("Age" , "loading",""));
        API.getClient().HEADER_Broiler_Checklist(SharedPref.getORG_CODE(),GET_HOUSE_FLOCK).enqueue(new Callback<API_Response_header>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_header> call, Response<API_Response_header> response) {
                if (response.isSuccessful()) {
                    API_Response_header res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<Checklist_Header> checklistItems = res.getChecklistHeaders();
                        if (checklistItems != null) {
                            for (int i = 0; i < checklistItems.size(); i++) {
                                // Access individual checklist item properties here
                                Checklist_Header item = checklistItems.get(i);
                                date_in.setText(func.header_html("Date In" , item.getDATE_IN(),""));
                                balance.setText(func.header_html("Stock Balance" , func.NumberFormat(Integer.parseInt(item.getCURRENT_BALANCE()))," Birds"));
                                age.setText(func.header_html("Age" , item.getCHICK_AGE()," Days"));

                                GET_DATE_IN = item.getDATE_IN();
                                GET_STOCK_BAL = item.getCURRENT_BALANCE();
                                GET_AGE = item.getCHICK_AGE();

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
            public void onFailure(Call<API_Response_header> call, Throwable t) {
                func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }

    public void reason_code_offline(Context context) {
        //getData from sqlite
        Transaction_details_edit();
        reason_code_parent.removeAllViews();
        other_reason_container.removeAllViews();
        Cursor cursor = offlineData.offline_reason_code_list();
        if (cursor != null && cursor.moveToFirst()) {
            LayoutInflater inflater = LayoutInflater.from(context);

            int i = 0; // Initialize the iterator

            do {
                int layoutResId = (i == cursor.getCount() - 1) ? R.layout.reason_other_item : R.layout.reason_item;
                LinearLayout parent = (LinearLayout) inflater.inflate(layoutResId, reason_code_parent, false);


                if (layoutResId == R.layout.reason_other_item) {
                    reason_checkbox = parent.findViewById(R.id.checkbox);
                    reason_name = parent.findViewById(R.id.reason_name);
                    reason_addmore = parent.findViewById(R.id.addmore);
                    reason_name.setText(cursor.getString(1));
                    Other(reason_checkbox, reason_addmore);

                } else {
                    reason_birds = parent.findViewById(R.id.reason_birds);
                    func.InputFormmater(reason_birds);
                    func.CursorFocus(reason_birds);
                    reason_name = parent.findViewById(R.id.reason_name);
                    reason_name.setText(cursor.getString(1));



                }

                // Apply fade-in animation
                Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fall_down_animation);
                parent.startAnimation(fadeInAnimation);

                // Add the inflated layout to the parent
                reason_code_parent.addView(parent);


                // Increment i
                i++;


            } while (cursor.moveToNext());
            cursor.close();
        }
        else {
            func.alert_dialog.dismiss();

        }
    }


    public void reason_code_online(Context context){
        //getData from sqlite
        Transaction_details_edit();

        reason_code_parent.removeAllViews();
        other_reason_container.removeAllViews();
        loading_shimmer.setVisibility(View.VISIBLE);
        API.getClient().REASON_Broiler_Checklist(SharedPref.getORG_CODE()).enqueue(new Callback<API_Response_reason>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<API_Response_reason> call, Response<API_Response_reason> response) {
                if (response.isSuccessful()) {
                    API_Response_reason res = response.body();
                    if (res != null && res.isSuccess()) {
                        checklistReasonList = res.getChecklistReasons();
                        if (checklistReasonList != null) {
                            loading_shimmer.setVisibility(View.GONE);
                            for (int i = 0; i < checklistReasonList.size(); i++) {

                                int layoutResId = (i == checklistReasonList.size() - 1) ? R.layout.reason_other_item : R.layout.reason_item;
                                LinearLayout parent = (LinearLayout) LayoutInflater.from(context).inflate(layoutResId, reason_code_parent, false);

                                if (layoutResId == R.layout.reason_other_item) {
                                    Checklist_Reason item = checklistReasonList.get(i);
                                    reason_checkbox = parent.findViewById(R.id.checkbox);
                                    reason_name = parent.findViewById(R.id.reason_name);
                                    reason_addmore = parent.findViewById(R.id.addmore);
                                    reason_name.setText(item.getREASON_NAME());


                                    if(hasR999(reasonsListEdit)){
                                        reason_checkbox.setChecked(true);
                                        int strokeColor = ContextCompat.getColor(Edit_checklist_form.this, R.color.btn_success);
                                        reason_addmore.setEnabled(true);
                                        reason_addmore.setStrokeColor(ColorStateList.valueOf(strokeColor));
                                        //this function for other specify only
                                        add_more_other(reason_addmore,true,reason_checkbox);
                                    }
                                    else{
                                        reason_checkbox.setChecked(false);
                                        int strokeColor = ContextCompat.getColor(Edit_checklist_form.this, R.color.black);
                                        reason_addmore.setEnabled(false);
                                        reason_addmore.setStrokeColor(ColorStateList.valueOf(strokeColor));
                                        add_more_other(reason_addmore,false,reason_checkbox);
                                    }

                                    model_checklist_reasons lastReason = null;
                                    for (model_checklist_reasons x : reasonsListEdit) {
                                        if(x.getReason_id().equals(item.getREASON_ID())){
                                            loadotherlist(x.getReason(), String.valueOf(x.getBirds()),x.getID());
                                            func.log(x.getPosition() + " " + x.getReason());
                                            lastReason = x;
                                        }

                                    }
                                    if (lastReason != null) {
                                        func.log("id: "+(lastReason.getPosition() + 1));
                                    }



                                } else {
                                    Checklist_Reason item = checklistReasonList.get(i);
                                    reason_birds = parent.findViewById(R.id.reason_birds);


                                    func.InputFormmater(reason_birds);
                                    func.CursorFocus(reason_birds);
                                    reason_name = parent.findViewById(R.id.reason_name);
                                    reason_name.setText(item.getREASON_NAME());


                                    model_checklist_reasons editReason = reasonsListEdit.get(i);
                                    if (item.getREASON_ID().equals(editReason.getReason_id())) {
                                        reason_birds.setText(func.Decimalformat(String.valueOf(editReason.getBirds())));
                                        TotalBroilerRejectCount += func.ConvertStringtoNumber(String.valueOf(editReason.getBirds()));
                                    }
                                    Reason_bird_update(reason_birds,reason_code_parent,"reason");


                                }

                                // Apply fade-in animation
                                Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fall_down_animation);
                                parent.startAnimation(fadeInAnimation);

                                // Add the inflated layout to the parent
                                reason_code_parent.addView(parent);
                            }



                            //this function for other specify only
                            Other(reason_checkbox,reason_addmore);




                        } else {
                            // Handle case where checklistItems is null
                            func.log("Checklist items are null");
                            loading_shimmer.setVisibility(View.GONE);
                        }

                    } else {
                        loading_shimmer.setVisibility(View.GONE);
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

    public boolean hasR999(List<model_checklist_reasons> reasonsListEdit) {
        // Iterate through reasonsListEdit
        for (model_checklist_reasons x : reasonsListEdit) {
            // Check if reason_id is "R999"
            if (x.getReason_id().equals("R999")) {
                // Return true if "R999" found
                return true;
            }
        }
        // Return false if "R999" not found
        return false;
    }


    public void loadotherlist(String reason, String count,int id){
        LinearLayout customLayout = (LinearLayout) LayoutInflater.from(Edit_checklist_form.this)
                .inflate(R.layout.reason_others_item, other_reason_container, false);
        // Find the remove ImageView inside the inflated custom layout
        ImageView removeImageView = customLayout.findViewById(R.id.remove);
        EditText other_reason = customLayout.findViewById(R.id.other_reason);
        EditText reason_birds = customLayout.findViewById(R.id.reason_birds); // Initialize reason_birds here

        other_reason.setText(reason);
        reason_birds.setText(func.Decimalformat(String.valueOf(count)));
        func.InputFormmater(reason_birds);
        func.CursorFocus(reason_birds);

        reason_birds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (broiler_reject_auto_count.isChecked()) {
                    TotalBroilerRejectOtherCount = 0;
                    // Loop through all EditText views in the container
                    for (int j = 0; j < other_reason_container.getChildCount(); j++) {
                        View childView = other_reason_container.getChildAt(j);
                        EditText childReasonBirds = childView.findViewById(R.id.reason_birds);
                        if (childReasonBirds != null) {
                            String birdsValue = childReasonBirds.getText().toString().trim();
                            if (!TextUtils.isEmpty(birdsValue)) {
                                TotalBroilerRejectOtherCount += func.ConvertStringtoNumber(birdsValue);
                            }
                        }
                    }

                    // Update UI with the total count
                    int totalBroilerRejectCount = TotalBroilerRejectCount + TotalBroilerRejectOtherCount;
                    broiler_reject_birds.setText(totalBroilerRejectCount == 0 ? "" : String.valueOf(totalBroilerRejectCount));
                    broiler_reject_wgh.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        removeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the parent of the remove ImageView, which is the customLayout itself
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null) {
                    // Remove the parent of the remove ImageView (i.e., the customLayout) from the parent container
                    ((ViewGroup) parent.getParent()).removeView(parent);
                    other_reasonList.remove((LinearLayout) parent);
                    func.log("delete id: " + id);
                    offlineData.deleteTransactionDetails(String.valueOf(id));
                }
            }
        });
        other_reason_container.addView(customLayout);
        other_reasonList.add(customLayout);

        ScrollView scrollView = findViewById(R.id.scrollview); // Replace with the ID of your ScrollView
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }



    private void Other(CheckBox checkBox, MaterialButton addmore){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    int strokeColor = ContextCompat.getColor(Edit_checklist_form.this, R.color.btn_success);
                    addmore.setEnabled(true);
                    addmore.setStrokeColor(ColorStateList.valueOf(strokeColor));
                    add_more_other(addmore,true,checkBox);


                }
                else{
                    int strokeColor = ContextCompat.getColor(Edit_checklist_form.this, R.color.black);
                    addmore.setEnabled(false);
                    addmore.setStrokeColor(ColorStateList.valueOf(strokeColor));
                    add_more_other(addmore,false,checkBox);
                }
            }
        });

    }


    private void Reason_bird_update(EditText reason_birds, LinearLayout container, String type) {
        reason_birds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (broiler_reject_auto_count.isChecked()) {
                    TotalBroilerRejectCount = 0;
                    // Loop through all EditText views in the container
                    for (int j = 0; j < container.getChildCount(); j++) {
                        View childView = container.getChildAt(j);
                        EditText childReasonBirds = childView.findViewById(R.id.reason_birds);
                        if (childReasonBirds != null) {
                            String birdsValue = childReasonBirds.getText().toString().trim();
                            if (!TextUtils.isEmpty(birdsValue)) {
                                TotalBroilerRejectCount += func.ConvertStringtoNumber(birdsValue);
                            }
                        }
                    }

                    int totalBroilerRejectCount = TotalBroilerRejectCount + TotalBroilerRejectOtherCount;
                    broiler_reject_birds.setText(totalBroilerRejectCount == 0 ? "" : String.valueOf(totalBroilerRejectCount));
                    broiler_reject_wgh.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }



    public void add_more_other(MaterialButton add, boolean isChecked, CheckBox checkBox) {
        if (!isChecked) {
            int others = other_reasonList.size();
            if (others > 0) {
                func.Razo_dialog(
                        Edit_checklist_form.this,
                        Func.HELP,
                        "Confirmation",
                        "Are you sure you want to remove all other reason?",
                        "No",
                        "Yes");
                func.negative_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                    checkBox.setChecked(true);
                });
                func.positive_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                    other_reason_container.removeAllViews();
                    other_reasonList.clear();
                    TotalBroilerRejectOtherCount = 0;
                    int totalBroilerRejectCount = TotalBroilerRejectCount + TotalBroilerRejectOtherCount;
                    broiler_reject_birds.setText(totalBroilerRejectCount == 0 ? "" : String.valueOf(totalBroilerRejectCount));
                    broiler_reject_wgh.setText("");
                });
            } else {
                other_reason_container.removeAllViews();
            }
        } else {
            // Add OnClickListener to the 'add' button
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout customLayout = (LinearLayout) LayoutInflater.from(view.getContext())
                            .inflate(R.layout.reason_others_item, other_reason_container, false);
                    // Find the remove ImageView inside the inflated custom layout
                    ImageView removeImageView = customLayout.findViewById(R.id.remove);
                    EditText reason_birds = customLayout.findViewById(R.id.reason_birds); // Initialize reason_birds here
                    func.InputFormmater(reason_birds);
                    func.CursorFocus(reason_birds);

                    reason_birds.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (broiler_reject_auto_count.isChecked()) {
                                TotalBroilerRejectOtherCount = 0;
                                // Loop through all EditText views in the container
                                for (int j = 0; j < other_reason_container.getChildCount(); j++) {
                                    View childView = other_reason_container.getChildAt(j);
                                    EditText childReasonBirds = childView.findViewById(R.id.reason_birds);
                                    if (childReasonBirds != null) {
                                        String birdsValue = childReasonBirds.getText().toString().trim();
                                        if (!TextUtils.isEmpty(birdsValue)) {
                                            TotalBroilerRejectOtherCount += func.ConvertStringtoNumber(birdsValue);
                                        }
                                    }
                                }

                                // Update UI with the total count
                                int totalBroilerRejectCount = TotalBroilerRejectCount + TotalBroilerRejectOtherCount;
                                broiler_reject_birds.setText(totalBroilerRejectCount == 0 ? "" : String.valueOf(totalBroilerRejectCount));
                                broiler_reject_wgh.setText("");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });

                    removeImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the parent of the remove ImageView, which is the customLayout itself
                            ViewGroup parent = (ViewGroup) v.getParent();
                            if (parent != null) {
                                // Remove the parent of the remove ImageView (i.e., the customLayout) from the parent container
                                ((ViewGroup) parent.getParent()).removeView(parent);
                                other_reasonList.remove((LinearLayout) parent);

                                if (broiler_reject_auto_count.isChecked()) {
                                    TotalBroilerRejectOtherCount = 0;
                                    // Loop through all EditText views in the container
                                    for (int j = 0; j < other_reason_container.getChildCount(); j++) {
                                        View childView = other_reason_container.getChildAt(j);
                                        EditText childReasonBirds = childView.findViewById(R.id.reason_birds);
                                        if (childReasonBirds != null) {
                                            String birdsValue = childReasonBirds.getText().toString().trim();
                                            if (!TextUtils.isEmpty(birdsValue)) {
                                                TotalBroilerRejectOtherCount += func.ConvertStringtoNumber(birdsValue);
                                                func.log(reasonsListEdit.get(j).getPosition() + " " + reasonsListEdit.get(j).getID());
                                            }
                                        }
                                    }

                                    // Update UI with the total count
                                    int totalBroilerRejectCount = TotalBroilerRejectCount + TotalBroilerRejectOtherCount;
                                    broiler_reject_birds.setText(totalBroilerRejectCount == 0 ? "" : String.valueOf(totalBroilerRejectCount));
                                    broiler_reject_wgh.setText("");
                                }

                            }
                        }
                    });
                    other_reason_container.addView(customLayout);
                    other_reasonList.add(customLayout);

                    ScrollView scrollView = findViewById(R.id.scrollview); // Replace with the ID of your ScrollView
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            });


        }
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
//                Farm_list_online(SharedPref.getORG_CODE(),2);
                reason_code_online(this);
                break;
            case 2:
                func.log("Connected to Mobile Data");
//                Farm_list_online(SharedPref.getORG_CODE(),2);
                reason_code_online(this);
                break;
            case 3:
                func.log("Disconnected");
                func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                Farm_list_offline(1);
                reason_code_offline(this);
                break;
            default:
                func.log("disconnected other reason");
                // Handle any other status if needed
                func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
//                Farm_list_offline(1);
                break;
        }
    }


    @Override
    public void onNetworkStable() {
        func.toast(R.drawable.icons8_wi_fi,"Online", Gravity.BOTTOM|Gravity.CENTER,0,50);
//        reason_code_online(this);
    }

    @Override
    public void onNetworkUnstable() {
        func.toast(R.drawable.icons8_wi_fi_off,"Offline", Gravity.BOTTOM|Gravity.CENTER,0,50);
//        Farm_list_offline(1);
    }

    @Override
    public void onBackPressed() {

        int others = other_reasonList.size();
        if (others > 0) {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to go back to the farm menu? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.back(this);
                super.onBackPressed();
                finish();

            });
        } else {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to go back to the farm menu? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.back(this);
                super.onBackPressed();
                finish();

            });
        }
    }

    public void back(View view) {
        int others = other_reasonList.size();
        if (others > 0) {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to go back to the farm menu? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.intent(Edit_Checklist.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
                finish();

            });
        } else {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to go back to the farm menu? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.intent(Edit_Checklist.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
                finish();

            });
        }
    }


    public void save(View view) {
        TotalBroilerRejectCount = 0;
        String getBroiler_birds = broiler_birds.getText().toString();
        String getBroiler_wgh = broiler_wgh.getText().toString();
        String getBroiler_reject_birds = broiler_reject_birds.getText().toString();
        String getBroiler_reject_wgh = broiler_reject_wgh.getText().toString();


        if(TextUtils.isEmpty(getBroiler_birds)){
            func.toast(R.drawable.close,"Please Check Broiler bird quantity", Gravity.BOTTOM|Gravity.CENTER,0,50);
            broiler_birds.requestFocus();
        }
        else if(TextUtils.isEmpty(getBroiler_wgh)){
            func.toast(R.drawable.close,"Please Check Broiler bird weight", Gravity.BOTTOM|Gravity.CENTER,0,50);
            broiler_wgh.requestFocus();
        }

        else{

            func.log("broiler_c : " + func.ConvertStringtoNumber(getBroiler_birds));
            func.log("broiler_w : " + getBroiler_wgh);
            func.log("==========================");
            func.log("broiler reject_c : " + (TextUtils.isEmpty(getBroiler_reject_birds) ? "0" : func.ConvertStringtoNumber(getBroiler_reject_birds)));
            func.log("broiler reject_w : " + (TextUtils.isEmpty(getBroiler_reject_wgh) ? "0" : getBroiler_reject_wgh));


            boolean checked = broiler_reject_auto_count.isChecked();
            if(checked){
                if(TextUtils.isEmpty(getBroiler_reject_birds)){
                    func.toast(R.drawable.close,"Please Check Broiler Reject bird quantity", Gravity.BOTTOM|Gravity.CENTER,0,50);
                    broiler_reject_birds.requestFocus();
                }
                else if(getBroiler_reject_birds.equals("0")){
                    func.toast(R.drawable.close,"Please Check Broiler Reject bird quantity", Gravity.BOTTOM|Gravity.CENTER,0,50);
                    broiler_reject_birds.requestFocus();
                }
                else if(TextUtils.isEmpty(getBroiler_reject_wgh)){
                    func.toast(R.drawable.close,"Please Check Broiler Reject bird weight", Gravity.BOTTOM|Gravity.CENTER,0,50);
                    broiler_reject_wgh.requestFocus();
                }
                else{
                    func.Razo_dialog(
                            Edit_checklist_form.this,
                            Func.HELP,
                            "Save Changes",
                            "Are you sure you want to save the data?",
                            "No",
                            "Yes");
                    func.negative_dialog.setOnClickListener(v -> {
                        func.alert_dialog.dismiss();
                    });
                    func.positive_dialog.setOnClickListener(v -> {
                        func.alert_dialog.dismiss();

                        boolean save_header = offlineData.Update_transaction_header(
                                String.valueOf(GET_ID),
                                GET_AUDIT_DATE,
                                SharedPref.getORG_CODE(),
                                GET_FARM_ORG,
                                GET_FARM_NAME,
                                GET_STOCK_BAL,
                                GET_DATE_IN,
                                GET_AGE,
                                GET_HOUSE_FLOCK,
                                String.valueOf(func.ConvertStringtoNumber(getBroiler_birds)),
                                getBroiler_wgh,
                                TextUtils.isEmpty(getBroiler_reject_birds) ? "0" : String.valueOf(func.ConvertStringtoNumber(getBroiler_reject_birds)),
                                TextUtils.isEmpty(getBroiler_reject_wgh) ? "0" : getBroiler_reject_wgh,
                                broiler_reject_auto_count.isChecked() ? "Y" : "N"
                        );
                        reason_list();

                        for (model_checklist_reasons reason : reasonsListEdit) {
                            // Access each element and perform operations as needed
                            boolean save_details = offlineData.Update_transaction_details(
                                    GET_ID,
                                    reason.getPosition(),
                                    reason.getBirds(),
                                    reason.getReason_id(),
                                    reason.getReason()
                            );
                            if(save_details){
                                func.log("updated_details : " +save_details);
                            }
                            else{
                                boolean save_details_new = offlineData.Insert_transaction_details_new_update(
                                        GET_ID,
                                        reason.getPosition(),
                                        reason.getBirds(),
                                        reason.getReason_id(),
                                        reason.getReason()
                                );
                                if(save_details_new){
                                    func.log("added new details : " +save_details_new);
                                }
                            }

                            func.log("updated_details : " +GET_ID + " " + reason.getPosition() + " " +
                                    reason.getBirds() + " " +
                                    reason.getReason_id()+ " " +
                                    reason.getReason());
                        }


                        func.Razo_dialog(
                                Edit_checklist_form.this,
                                save_header ? R.drawable.check : R.drawable.close,
                                save_header ? "Updated" : "Error",
                                save_header ? "Your data has been updated successfully." : "Data could not be update.",
                                null,
                                "OK");
                        func.negative_dialog.setEnabled(false);
                        func.negative_dialog.setOnClickListener(v1 -> {
                            func.alert_dialog.dismiss();

                        });
                        func.positive_dialog.setOnClickListener(v1 -> {
                            func.alert_dialog.dismiss();
                            func.intent(Edit_Checklist.class,Edit_checklist_form.this, R.anim.slide_out_right, R.anim.slide_in_left);
                            finish();

                        });



                    });
                }
            }
            else{
                func.Razo_dialog(
                        Edit_checklist_form.this,
                        Func.HELP,
                        "Save Data?",
                        "Are you sure you want to save the data?",
                        "No",
                        "Yes");
                func.negative_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                });
                func.positive_dialog.setOnClickListener(v -> {
                    func.alert_dialog.dismiss();
                    reason_list();
                    boolean save_header = offlineData.Update_transaction_header(
                            String.valueOf(GET_ID),
                            GET_AUDIT_DATE,
                            SharedPref.getORG_CODE(),
                            GET_FARM_ORG,
                            GET_FARM_NAME,
                            GET_STOCK_BAL,
                            GET_DATE_IN,
                            GET_AGE,
                            GET_HOUSE_FLOCK,
                            String.valueOf(func.ConvertStringtoNumber(getBroiler_birds)),
                            getBroiler_wgh,
                            TextUtils.isEmpty(getBroiler_reject_birds) ? "0" : String.valueOf(func.ConvertStringtoNumber(getBroiler_reject_birds)),
                            TextUtils.isEmpty(getBroiler_reject_wgh) ? "0" : getBroiler_reject_wgh,
                            broiler_reject_auto_count.isChecked() ? "Y" : "N");

                    for (model_checklist_reasons reason : reasonsList) {
                        // Access each element and perform operations as needed
                        boolean save_details = offlineData.Update_transaction_details(
                                GET_ID,
                                reason.getPosition(),
                                reason.getBirds(),
                                reason.getReason_id(),
                                reason.getReason()
                        );
                        if(save_details){
                            func.log("updated_details : " +save_details);
                        }
                        else{
                            boolean save_details_new = offlineData.Insert_transaction_details_new_update(
                                    GET_ID,
                                    reason.getPosition(),
                                    reason.getBirds(),
                                    reason.getReason_id(),
                                    reason.getReason()
                            );
                            if(save_details_new){
                                func.log("added new details : " +save_details_new);
                            }
                        }
                        func.log("updated_details : " +GET_ID + " " + reason.getPosition() + " " +
                                reason.getBirds() + " " +
                                reason.getReason_id()+ " " +
                                reason.getReason());
                    }
                    func.Razo_dialog(
                            Edit_checklist_form.this,
                            save_header ? R.drawable.check : R.drawable.close,
                            save_header ? "Saved" : "Error",
                            save_header ? "Your data has been saved successfully." : "Data could not be saved.",
                            null,
                            "OK");
                    func.negative_dialog.setEnabled(false);
                    func.negative_dialog.setOnClickListener(v1 -> {
                        func.alert_dialog.dismiss();

                    });
                    func.positive_dialog.setOnClickListener(v1 -> {
                        func.alert_dialog.dismiss();
                        func.intent(Edit_Checklist.class,Edit_checklist_form.this, R.anim.slide_out_right, R.anim.slide_in_left);
                        finish();

                    });

                });

            }



        }
    }


    protected void reason_list(){
        reasonsListEdit.clear();
        /**passed**/
        for (int i = 0; i < reason_code_parent.getChildCount(); i++) {
            View childView = reason_code_parent.getChildAt(i);
            if (childView.findViewById(R.id.reason_birds) != null) {
                // If it's 'reason_item', retrieve the value of 'reason_birds'
                EditText reason_birds = childView.findViewById(R.id.reason_birds);
                TextView reason_name = childView.findViewById(R.id.reason_name);
                String birdsValue = reason_birds.getText().toString();
                String getreason_name = reason_name.getText().toString();
                String season_id = checklistReasonList.get(i).getREASON_ID();

                TotalBroilerRejectCount += func.ConvertStringtoNumber(birdsValue);

                long birds = TextUtils.isEmpty(birdsValue) ? 0 : (birdsValue.equals("0") ? 0 : func.ConvertStringtoNumber(birdsValue));
                model_checklist_reasons item = new model_checklist_reasons(
                        i,
                        birds,
                        season_id,
                        getreason_name,
                        GET_ID
                );

                reasonsListEdit.add(item);
            }
        }

        for (int i = 0; i < other_reason_container.getChildCount(); i++) {
            View childView = other_reason_container.getChildAt(i);
            if (childView.findViewById(R.id.reason_birds) != null) {
                // If it's 'reason_item', retrieve the value of 'reason_birds'
                EditText reason_birds = childView.findViewById(R.id.reason_birds);
                TextView other_reason = childView.findViewById(R.id.other_reason);
                String birdsValue = reason_birds.getText().toString();
                String getOtherReason = other_reason.getText().toString();




                if(!TextUtils.isEmpty(birdsValue)){
                    if(TextUtils.isEmpty(other_reason.getText().toString().trim())){
                        func.toast(R.drawable.close,"Please state the reason", Gravity.BOTTOM|Gravity.CENTER,0,50);
                        other_reason.requestFocus();
                    }
                    else{
                        TotalBroilerRejectCount += func.ConvertStringtoNumber(birdsValue);
                        long birds = TextUtils.isEmpty(birdsValue) ? 0 : (birdsValue.equals("0") ? 0 : func.ConvertStringtoNumber(birdsValue));

                        model_checklist_reasons item = new model_checklist_reasons(
                                i,
                                birds,
                                "R999",
                                getOtherReason,
                                GET_ID
                        );

                        reasonsListEdit.add(item);
                    }

                }

            }
        }

        func.log("Total Birds: " + TotalBroilerRejectCount);
    }

    public void Cancel(View view) {
        int others = other_reasonList.size();
        if (others > 0) {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to cancel this transaction? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.intent(Farm_menu.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
                finish();

            });
        } else {
            func.Razo_dialog(
                    Edit_checklist_form.this,
                    Func.HELP,
                    "Confirmation",
                    "Are you sure you want to cancel this transaction? Any unsaved changes will be lost.",
                    "No",
                    "Yes");
            func.negative_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();

            });
            func.positive_dialog.setOnClickListener(v -> {
                func.alert_dialog.dismiss();
                func.intent(Farm_menu.class,this, R.anim.slide_out_right, R.anim.slide_in_left);
                finish();

            });
        }
    }
}