package com.ronnelrazo.broilerchecklist.func;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.novoda.merlin.Merlin;
import com.ronnelrazo.broilerchecklist.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import taimoor.sultani.sweetalert2.Sweetalert;

public class Func {

    private static Func application;
    private static Context cont;



    public MaterialAlertDialogBuilder modal_dialog;
    public AlertDialog alert_dialog,loading_alert_dialog,farm_dialog,signature_dialog;


    /***Razo Dialog**/
    public static int SUCCESS = R.drawable.icons8_ok;
    public static int CANCEL = R.drawable.icons8_cancel;
    public static int INFO = R.drawable.icons8_info;

    public static int HELP = R.drawable.icons8_help;

    public static int DOWNLOAD = R.drawable.icons8_download;

    public ImageView logo_dialog;
    public TextView title_dialog,body_dialog;
    public MaterialButton negative_dialog,positive_dialog;
    public MaterialButton loading_negative_dialog,loading_positive_dialog;

    public MaterialButton farm_negative_dialog,farm_positive_dialog;

    public AutoCompleteTextView farm_house_flock;

    public String GetSelectedFlock = null;

    public TextView Farm_name = null;

    public EditText audit_date;

    public final Calendar myCalendar = Calendar.getInstance();

    /**signature**/
    public SignaturePad signaturePad;
    public TextView label_signature_type;
    public EditText signature_username;
    public TextView label_signature_user;
    public MaterialButton sign_negative_dialog,sign_positive_dialog,sign_reset;





    /***end Razo Dialog**/

    public ProgressBar download_progressbar;
    public TextView downlaod_progress;

    public Func(Context context){
        cont = context;
    }


    public static synchronized Func getInstance(Context context){
        if(application == null){
            application = new Func(context);
        }
        return application;
    }

    public void toast(int raw,String body,int postion,int x ,int y){
        Toast toast = new Toast(cont);
        View vs = LayoutInflater.from(cont).inflate(R.layout.custom_toast, null);
        ImageView icon = vs.findViewById(R.id.icon);
        icon.setImageResource(raw);
        TextView msg = vs.findViewById(R.id.body);
        msg.setText(body);
        toast.setGravity(postion, x, y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(vs);
        toast.show();
    }


    public void Audit_date(TextView date){
        DatePickerDialog.OnDateSetListener datepicker1 = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_textDateview(date);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                cont,
                R.style.MyDatePickerStyle,
                datepicker1,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today's date
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    public void Audit_date_filter(TextView date){
        DatePickerDialog.OnDateSetListener datepicker1 = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_textDateview(date);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                cont,
                R.style.MyDatePickerStyle,
                datepicker1,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            if (which == DialogInterface.BUTTON_NEGATIVE) {
               date.setText("");
            }
        });

        datePickerDialog.show();
    }


    public long ConvertStringtoNumber(String s){
        if (TextUtils.isEmpty(s)) {
            return 0;
        } else {
            String numberWithCommasRemoved = s.replace(",", "");
            return Long.parseLong(numberWithCommasRemoved);
        }
    }


    public void WghChecker(EditText editText){
        editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(999999, 2)});
    }

    public void Ontextchanged(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int strokeColor = Color.parseColor("#DAE5EA");
                editText.setBackgroundTintList(ColorStateList.valueOf(strokeColor));
            }
        });
    }


    public void CursorFocus(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editText.setSelection(editText.getText().length());
                        }
                    }, 200); // Delay in milliseconds
                }
            }
        });
    }


    public String Decimalformat(String s){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(Long.parseLong(s));
    }


    public void InputFormmater(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed while text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Format the text after changes
                String input = s.toString().replaceAll(",", ""); // Remove existing commas
                if (!input.isEmpty()) {
                    String formattedInput = formatNumber(input); // Format the input number
                    editText.removeTextChangedListener(this); // Remove the listener to avoid infinite loop
                    editText.setText(formattedInput); // Set the formatted text
                    editText.setSelection(formattedInput.length()); // Set cursor position
                    editText.addTextChangedListener(this); // Add the listener back
                }
            }
        });
    }

    private String formatNumber(String number) {
        try {
            long numericValue = Long.parseLong(number);
            return String.format("%,d", numericValue); // Format the number with commas
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return number; // Return the original input if it's not a valid number
        }
    }

    private void update_textDateview(TextView textView) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(sdf.format(myCalendar.getTime()));
    }


    public void loading(Context context){
        modal_dialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.loading,null);

        modal_dialog.setView(v);
        alert_dialog = modal_dialog.create();
        alert_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert_dialog.setCancelable(false);
        alert_dialog.setCanceledOnTouchOutside(false);
        alert_dialog.show();

    }

    public String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(myCalendar.getTime());
    }


    public void farm_house_flock_dialog(Context context,String negative,String positive){
        modal_dialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.farm_dialog,null);

        farm_house_flock = v.findViewById(R.id.farm_house_flock);
        audit_date = v.findViewById(R.id.audit_date);
        Farm_name = v.findViewById(R.id.Farm_name);

        //show currentdate as default date
        audit_date.setText(currentDate());

//        Audit_date(audit_date);
        audit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Audit_date(audit_date);
            }
        });



        farm_negative_dialog = v.findViewById(R.id.negative);
        farm_negative_dialog.setText(TextUtils.isEmpty(negative) ? "Cancel" : negative);

        farm_positive_dialog = v.findViewById(R.id.positive);
        farm_positive_dialog.setText(TextUtils.isEmpty(positive) ? "Ok" : positive);


        modal_dialog.setView(v);
        farm_dialog = modal_dialog.create();
        farm_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        farm_dialog.setCancelable(false);
        farm_dialog.setCanceledOnTouchOutside(false);
        farm_dialog.show();
    }

    public void Razo_dialog(Context context,int type,String title,String body,String negative,String positive){
        modal_dialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.alert_dialog,null);



        logo_dialog = v.findViewById(R.id.logo);
        logo_dialog.setImageResource(IntUtils.isEmpty(type) ? SUCCESS : type);

        title_dialog = v.findViewById(R.id.title);
        title_dialog.setText(title);

        body_dialog = v.findViewById(R.id.body);
        body_dialog.setText(body);

        negative_dialog = v.findViewById(R.id.negative);
        negative_dialog.setText(TextUtils.isEmpty(negative) ? "Cancel" : negative);

        positive_dialog = v.findViewById(R.id.positive);
        positive_dialog.setText(TextUtils.isEmpty(positive) ? "Ok" : positive);


        modal_dialog.setView(v);
        alert_dialog = modal_dialog.create();
        alert_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert_dialog.setCancelable(false);
        alert_dialog.setCanceledOnTouchOutside(false);
        alert_dialog.show();
    }


    public void signature_dialog(Context context,String sign_type,String negative,String positive){
        modal_dialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.signature_dialog,null);

        signaturePad = v.findViewById(R.id.signaturePad);
        label_signature_type = v.findViewById(R.id.label_signature_type);
        signature_username = v.findViewById(R.id.username);
        label_signature_user = v.findViewById(R.id.signature_user);

        sign_reset = v.findViewById(R.id.reset);

        sign_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
                signaturePad.clearSignature();

            }
        });


        label_signature_type.setText(sign_type);



        sign_negative_dialog = v.findViewById(R.id.negative);
        sign_negative_dialog.setText(TextUtils.isEmpty(negative) ? "Cancel" : negative);

        sign_positive_dialog = v.findViewById(R.id.positive);
        sign_positive_dialog.setText(TextUtils.isEmpty(positive) ? "Ok" : positive);


        modal_dialog.setView(v);
        signature_dialog = modal_dialog.create();
        signature_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        signature_dialog.setCancelable(false);
        signature_dialog.setCanceledOnTouchOutside(false);
        signature_dialog.show();
    }


    public void download_dialog(Context context,int type,String title, String progress,String negative,String positive){
        modal_dialog = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.loading_dialog,null);



        logo_dialog = v.findViewById(R.id.logo);
        logo_dialog.setImageResource(IntUtils.isEmpty(type) ? SUCCESS : type);

        title_dialog = v.findViewById(R.id.title);
        title_dialog.setText(title);


        download_progressbar = v.findViewById(R.id.progressbar);

        downlaod_progress = v.findViewById(R.id.progress);
        downlaod_progress.setText(progress);

        loading_negative_dialog = v.findViewById(R.id.negative);
        loading_negative_dialog.setText(TextUtils.isEmpty(negative) ? "Cancel" : negative);

        loading_positive_dialog = v.findViewById(R.id.positive);
        loading_positive_dialog.setText(TextUtils.isEmpty(positive) ? "Ok" : positive);


        modal_dialog.setView(v);
        loading_alert_dialog = modal_dialog.create();
        loading_alert_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_alert_dialog.setCancelable(false);
        loading_alert_dialog.setCanceledOnTouchOutside(false);
        loading_alert_dialog.show();
    }


    public String NumberFormat(int number){
        return NumberFormat.getNumberInstance().format(number);
    }

    public Spanned header_html(String title, String details, String t){
        String text = String.format("<font color='%s'><strong>%s</strong></font> : <font color='%s' ></strong>%s</strong></font><font color='%s'><strong>%s</strong></font>","#31363F",title,"#378CE7",details,"#31363F",t);
        return Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY);
    }

    public Animation fadeIn() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // Add decelerate interpolator for a more natural animation
        fadeIn.setDuration(1000); // Adjust duration as needed
        return fadeIn;
    }

    public Animation fadeOut() {
        Animation fadeOut = new AlphaAnimation(1, 0); // From fully visible (alpha=1) to fully invisible (alpha=0)
        fadeOut.setInterpolator(new DecelerateInterpolator()); // Add decelerate interpolator for a more natural animation
        fadeOut.setDuration(1000); // Adjust duration as needed
        return fadeOut;
    }



    public int log(String log){
        return Log.i("BCList",log);
    }

    public void intent(Class<?> activity, Context context,int enterAnim, int exitAnim){
        Intent i = new Intent(context,activity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
    }


    public Animation anim(){
        return AnimationUtils.loadAnimation(cont, R.anim.fadein);
    }


    public Boolean focus(TextInputEditText inputEditText){
        return inputEditText.requestFocus();
    }
    public void back(Context context){
        ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
