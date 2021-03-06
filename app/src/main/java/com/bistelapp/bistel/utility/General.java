package com.bistelapp.bistel.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bistelapp.bistel.MainActivity;
import com.bistelapp.bistel.MyApplication;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.informations.driver.driver_info;
import com.bistelapp.bistel.informations.rider.Bookings;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tayo on 3/28/2016.
 */

public class General {
    public Context context;
    public ProgressDialog progressDialog;
    public AlertDialog alertDialog;
    private boolean check_sign_in = false;

    public General(Context context) {
        this.context = context;
    }

    public void displayAlertDialog(String title, String text) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void displayAlertDialog(String title, String text, final String mobile) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + mobile));
                context.startActivity(intent);
            }
        });
        alertDialog.show();
    }

    public void dismissAlertDialog() {
        alertDialog.dismiss();
    }

    public void displayProgressDialog(String text) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void Logout() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public boolean rider_check_all(String text1, String text2, TextInputLayout t1, EditText et1, TextInputLayout t2, EditText et2) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();

        if (option1.isEmpty() && t1 != null) {
            t1.setError("please provide " + text1);
            check_sign_in = false;
        } else if (option2.isEmpty() && t2 != null) {
            t2.setError("please provide " + text2);
            check_sign_in = false;
        } else {
            check_sign_in = true;
            t1.setError("");
            t2.setError("");
        }

        return check_sign_in;
    }

    public boolean rider_book_all(String text1, String text2, TextInputLayout t1, EditText et1, TextInputLayout t2, EditText et2) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();

        if (option1.isEmpty() && t1 != null) {
            Toast.makeText(context, "please provide " + text1, Toast.LENGTH_LONG).show();
            check_sign_in = false;
        } else if (option2.isEmpty() && t2 != null) {
            Toast.makeText(context, "please provide " + text2, Toast.LENGTH_LONG).show();
            check_sign_in = false;
        } else {
            check_sign_in = true;
            t1.setError("");
            t2.setError("");
        }

        return check_sign_in;
    }

    public boolean rider_check_all(String text1, String text2, String text3, TextInputLayout t1, EditText et1, TextInputLayout t2, EditText et2, TextInputLayout t3, EditText et3) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();
        String option3 = et3.getText().toString();

        if (option1.isEmpty() && t1 != null) {
            t1.setError("please provide " + text1);
            check_sign_in = false;
        } else if (option2.isEmpty() && t2 != null) {
            t2.setError("please provide " + text2);
            check_sign_in = false;
        } else if (option3.isEmpty() && t3 != null) {
            t3.setError("please provide " + text3);
            check_sign_in = false;
        } else {
            check_sign_in = true;
            t1.setError("");
            t2.setError("");
            t3.setError("");
        }

        return check_sign_in;
    }

    public boolean rider_check_all(String text1, String text2, String text3, String text4, String text5, TextInputLayout t1, EditText et1, TextInputLayout t2, EditText et2, TextInputLayout t3, EditText et3, TextInputLayout t4, EditText et4, TextInputLayout t5, EditText et5, CheckBox checkBox) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();
        String option3 = et3.getText().toString();
        String option4 = et4.getText().toString();
        String option5 = et5.getText().toString();
        boolean check = checkBox.isChecked();

        if (option1.isEmpty() && t1 != null) {
            t1.setError("please provide " + text1);
            check_sign_in = false;
        } else if (option2.isEmpty() && t2 != null) {
            t2.setError("please provide " + text2);
            check_sign_in = false;
        } else if (option3.isEmpty() && t3 != null) {
            t3.setError("please provide " + text3);
            check_sign_in = false;
        } else if (option4.isEmpty() && t4 != null) {
            t4.setError("please provide " + text4);
            check_sign_in = false;
        } else if (option5.isEmpty() && t5 != null) {
            t5.setError("please provide " + text5);
            check_sign_in = false;
        } else if (!check) {
            displayAlertDialog("Error", "please agree to the terms and condition");
        } else if (!isValidEmail(option3)) {
            displayAlertDialog("Error", "Invalid email address");
        } else {
            check_sign_in = true;
            t1.setError("");
            t2.setError("");
            t3.setError("");
            t4.setError("");
            t5.setError("");
        }

        return check_sign_in;
    }

    public boolean rider_check_all(String text1, String text2, String text3, String text4, String text5, String text6, TextInputLayout t1, EditText et1, TextInputLayout t2, EditText et2, TextInputLayout t3, EditText et3, TextInputLayout t4, EditText et4, TextInputLayout t5, EditText et5, TextInputLayout t6, EditText et6, CheckBox checkBox) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();
        String option3 = et3.getText().toString();
        String option4 = et4.getText().toString();
        String option5 = et5.getText().toString();
        String option6 = et6.getText().toString();
        boolean check = checkBox.isChecked();

        if (option1.isEmpty() && t1 != null) {
            t1.setError("please provide " + text1);
            check_sign_in = false;
        } else if (option2.isEmpty() && t2 != null) {
            t2.setError("please provide " + text2);
            check_sign_in = false;
        } else if (option3.isEmpty() && t3 != null) {
            t3.setError("please provide " + text3);
            check_sign_in = false;
        } else if (option4.isEmpty() && t4 != null) {
            t4.setError("please provide " + text4);
            check_sign_in = false;
        } else if (option5.isEmpty() && t5 != null) {
            t5.setError("please provide " + text5);
            check_sign_in = false;
        } else if (option6.isEmpty() && t6 != null) {
            t6.setError("please provide " + text6);
            check_sign_in = false;
        } else if (!check) {
            check_sign_in = false;
            displayAlertDialog("Error", "please agree to the terms and condition");
        } else {
            check_sign_in = true;
            t1.setError("");
            t2.setError("");
            t3.setError("");
            t4.setError("");
            t5.setError("");
            t6.setError("");
        }

        return check_sign_in;
    }

    public boolean rider_check_all(boolean confirm, String text1, String text2, String text3, EditText et1, EditText et2, EditText et3) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();
        String option3 = et3.getText().toString();

        if (confirm) {
            if (!option2.contentEquals(option3)) {
                displayAlertDialog("Error", "please confirm passwords");
                check_sign_in = false;
            } else if (option1.isEmpty()) {
                check_sign_in = false;
                displayAlertDialog("Error", "please provide " + text1);
            } else if (option2.isEmpty()) {
                check_sign_in = false;
                displayAlertDialog("Error", "please provide " + text2);
            } else if (option3.isEmpty()) {
                check_sign_in = false;
                displayAlertDialog("Error", "please provide " + text3);
            } else {
                check_sign_in = true;
            }
        }


        return check_sign_in;
    }

    public boolean rider_check_all(String text1, String text2, String text3, EditText et1, EditText et2, EditText et3) {
        String option1 = et1.getText().toString();
        String option2 = et2.getText().toString();
        String option3 = et3.getText().toString();

        if (option1.isEmpty()) {
            check_sign_in = false;
            displayAlertDialog("Error", "please provide " + text1);
        } else if (option2.isEmpty()) {
            check_sign_in = false;
            displayAlertDialog("Error", "please provide " + text2);
        } else if (option3.isEmpty()) {
            check_sign_in = false;
            displayAlertDialog("Error", "please provide " + text3);
        } else {
            check_sign_in = true;
        }


        return check_sign_in;
    }

    public static void handleVolleyError(VolleyError error, Context mTextError) {
        //if any error occurs in the network operations, show the TextView that contains the error message

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(mTextError, R.string.error_timeout, Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(mTextError, R.string.error_auth, Toast.LENGTH_LONG).show();
            //
        } else if (error instanceof ServerError) {
            Toast.makeText(mTextError, R.string.error_server, Toast.LENGTH_LONG).show();
            //
        } else if (error instanceof NetworkError) {
            Toast.makeText(mTextError, R.string.error_network, Toast.LENGTH_LONG).show();
            //
        } else if (error instanceof ParseError) {
            Toast.makeText(mTextError, R.string.error_parse, Toast.LENGTH_LONG).show();
            //
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getDeviceID() {
        String device = "";
        device = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return device;
    }

    public String return_subString(String what) {
        String returnSub = "";
        returnSub = what.substring(0, what.indexOf(" "));
        return returnSub;
    }

    public void deletePreferenceData(SharedPreferences data) {
        SharedPreferences.Editor editor = data.edit();
        editor.clear();
        editor.apply();
    }

    public ArrayList<driver_info> sortList(ArrayList<driver_info> getData) {
        final ArrayList<driver_info> customData = new ArrayList<>();

        for (int i = 0; i < getData.size(); i++) {

            for (int j = i; j < getData.size(); j++) {

            }
        }

        return customData;
    }

    public void LoadDB(){
        ArrayList<Bookings> currentData = new ArrayList<>();
        Bookings current = new Bookings();
        current.get_id = "0";
        current.driver_name = "none";
        current.driver_number = "none";
        current.driver_player_id = "none";
        current.plateNumber = "none";
        current.pickUp = "none";
        current.destination = "none";
        current.distance = "none";
        current.time = "none";
        current.amount = "none";
        current.payment_type = "none";
        current.booked_date = "none";
        currentData.add(current);
        MyApplication.getWritableDatabase().insertMyPost(currentData,false);
    }

    public int totalAmount(double figure) {
        int r_totalAmount = 0;
        int f = (int)figure;
        String gLD = String.valueOf(f);
        int getLastDigit = Integer.parseInt(gLD.substring(gLD.length() - 1));
        Log.e("getLastDigit Bistel","getLastDigit = "+getLastDigit+" Length = "+gLD.length());
        if (getLastDigit < 5) {
            r_totalAmount = f - getLastDigit;
            Log.e("r_totalAmountBistel < 5","r_totalAmount = "+r_totalAmount+"");
        } else if (getLastDigit > 5) {
            r_totalAmount = f + (10 - getLastDigit);
            Log.e("r_totalAmountBistel > 5","getLastDigit = "+r_totalAmount+"");
        }
        return r_totalAmount;
    }
}
