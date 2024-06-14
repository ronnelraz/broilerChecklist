package com.ronnelrazo.broilerchecklist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_Response;
import com.ronnelrazo.broilerchecklist.model.Login_data;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.database.Database;
import com.ronnelrazo.broilerchecklist.func.Func;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private Func func;
    private SharedPref sharedPref;

    private TextInputEditText username,password;
    private CheckBox keeplogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        func = new Func(this);
        sharedPref = new SharedPref(this);
        new Database(this).getReadableDatabase();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        keeplogin = findViewById(R.id.keeplogin);

        if(sharedPref.getKeepLogin()){
            func.log("Login: auto login [keep login: true]");
            func.intent(Menu.class,this, R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        else{
            func.log("Login: No session");
        }

    }

    public void login(View view) {
        String getUsername = Objects.requireNonNull(username.getText()).toString();
        String getPassword = Objects.requireNonNull(password.getText()).toString();
        boolean isKeep = keeplogin.isChecked();

        if (TextUtils.isEmpty(getUsername)){
            func.toast(R.drawable.close,"Invalid Username", Gravity.BOTTOM|Gravity.CENTER,0,50);
            func.focus(username);

        }
        else if(TextUtils.isEmpty(getPassword)){
            func.toast(R.drawable.close,"Invalid Password", Gravity.BOTTOM|Gravity.CENTER,0,50);
            func.focus(password);

        }
        else{
            func.loading(view.getContext());
            API.getClient().LOGIN(getUsername,getPassword).enqueue(new Callback<API_Response>() {
                @Override
                public void onResponse(Call<API_Response> call, Response<API_Response> response) {
                    if (response.isSuccessful()) {
                        API_Response res = response.body();
                        if (res != null && res.isSuccess()) {
                            Login_data data = res.getLogin_data();
                            if (data != null) {
                                String orgWip = data.getOrgWip();
                                String status = data.getStatus();
                                String user = data.getUser();
                                // Do something with the data

                                boolean org_wip = sharedPref.setORG_CODE(orgWip);
                                boolean keep = sharedPref.setKeepLogin(isKeep);
                                boolean setUser = sharedPref.setUSER(user);
//
                                func.log("Login : Keep login? " + (sharedPref.getKeepLogin() ? "yes" : "No"));
                                func.log("Login : user " +user + " status : " + status );
                                func.log("Login : org_wip " +orgWip);


                                func.toast(R.drawable.check,res.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                                func.alert_dialog.dismiss();
                                func.intent(Menu.class,view.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();


                            }
                        } else {
                            String errorMessage = "Unknown error occurred";
                            if (res != null && res.getMessage() != null) {
                                errorMessage = res.getMessage();
                            }

                            func.toast(R.drawable.close,errorMessage, Gravity.BOTTOM|Gravity.CENTER,0,50);
                            func.alert_dialog.dismiss();
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<API_Response> call, Throwable t) {
                    func.toast(R.drawable.close,t.getMessage(), Gravity.BOTTOM|Gravity.CENTER,0,50);
                    func.alert_dialog.dismiss();

                }
            });
        }



    }
}