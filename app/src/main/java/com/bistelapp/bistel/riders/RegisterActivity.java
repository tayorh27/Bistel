package com.bistelapp.bistel.riders;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bistelapp.bistel.MainActivity;
import com.bistelapp.bistel.R;
import com.bistelapp.bistel.SigninActivity;
import com.bistelapp.bistel.internet.rider.RegisterRepo;
import com.bistelapp.bistel.utility.General;
import com.onesignal.OneSignal;

public class RegisterActivity extends ActionBarActivity implements View.OnClickListener{

    Button register;
    String dr;
    General general;
    RegisterRepo registerRepo;

    TextInputLayout tFirst,tLast,tEmail,tMobile,tPassword;
    EditText etFirst,etLast,etEmail,etMobile,etPassword;

    CheckBox checkBox;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button)findViewById(R.id.reg_);
        register.setOnClickListener(this);
        general = new General(RegisterActivity.this);

        tFirst = (TextInputLayout)findViewById(R.id.firstName);
        tLast = (TextInputLayout)findViewById(R.id.lastName);
        tEmail = (TextInputLayout)findViewById(R.id.email);
        tMobile = (TextInputLayout)findViewById(R.id.mobile);
        tPassword = (TextInputLayout)findViewById(R.id.password);

        etFirst = (EditText)findViewById(R.id.fn);
        etLast = (EditText)findViewById(R.id.ln);
        etEmail = (EditText)findViewById(R.id.tv_email);
        etMobile = (EditText)findViewById(R.id.tv_mobile);
        etPassword = (EditText)findViewById(R.id.tv_password);

        checkBox = (CheckBox)findViewById(R.id.check_agreement);
        tv = (TextView)findViewById(R.id.agreement);

        tv.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            dr = bundle.getString("option");
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_:
                if(general.rider_check_all("first name","last name","email address","mobile phone","password",tFirst,etFirst,tLast,etLast,tEmail,etEmail,tMobile,etMobile,tPassword,etPassword,checkBox)) {
                    String fn = etFirst.getText().toString();
                    String ln = etLast.getText().toString();
                    String email = etEmail.getText().toString();
                    String mobile = etMobile.getText().toString();
                    String pass = etPassword.getText().toString();
                    registerRepo = new RegisterRepo(RegisterActivity.this,fn,ln,email,mobile,pass);

                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String s, String s2) {
                            if (s != null) {
                                registerRepo.Register(s);
                            }
                        }
                    });


                }
                break;
            case R.id.agreement:
                general.displayAlertDialog("Rider Terms and Conditions", getResources().getString(R.string.termsShow));
                break;
        }
    }
}
