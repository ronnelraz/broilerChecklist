package com.ronnelrazo.broilerchecklist.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref application;
    private static Context context;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHARED_DATA = "SHARED_DATA";
    private static final String SHARED_KEEP_LOGIN = "KEEP_LOGIN";
    private static final String SHARED_USER = "USER";

    private static final String SHARED_ORG_WIP = "ORG_WIP";

    public SharedPref(Context context1){
        context = context1;
    }

    public static synchronized SharedPref getInstance(Context context){
        if(application == null){
            application = new SharedPref(context);
        }
        return application;
    }


    public boolean setKeepLogin(boolean keeplogin){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_KEEP_LOGIN,keeplogin);
        editor.apply();
        return true;
    }

    public boolean getKeepLogin(){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_KEEP_LOGIN,false);
    }

    public boolean setUSER(String USER){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SHARED_USER,USER);
        editor.apply();
        return true;
    }

    public boolean setORG_CODE(String org_code){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(SHARED_ORG_WIP,org_code);
        editor.apply();
        return true;
    }


    public static String getORG_CODE(){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_ORG_WIP,"");
    }

    public static String getUSER(){
        sharedPreferences = context.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_USER,"");
    }

}
