package com.bistelapp.bistel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bistelapp.bistel.internet.driver.DriverLoginRepo;
import com.bistelapp.bistel.internet.rider.LoginRepo;
import com.bistelapp.bistel.utility.General;


public class SigninActivity extends ActionBarActivity implements View.OnClickListener {

    Button signIn;
    String dr;
    General general;

    TextInputLayout tEmail,tPassword;
    EditText etEmail,etPassword;
    LoginRepo loginRepo;
    DriverLoginRepo driverLoginRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signIn = (Button)findViewById(R.id.signin);
        tEmail = (TextInputLayout)findViewById(R.id.email);
        tPassword = (TextInputLayout)findViewById(R.id.password);
        etEmail = (EditText)findViewById(R.id.tv_email);
        etPassword = (EditText)findViewById(R.id.tv_password);




        signIn.setOnClickListener(this);
        general = new General(SigninActivity.this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            dr = bundle.getString("option");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SigninActivity.this,MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void SignRider(){
        if(dr.contentEquals("rider")) {
            if(general.rider_check_all("email address","password",tEmail,etEmail,tPassword,etPassword)) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                loginRepo = new LoginRepo(SigninActivity.this,email,password);
                loginRepo.LogRiderIn();
            }
        }else if(dr.contentEquals("driver")){
            if(general.rider_check_all("email address","password",tEmail,etEmail,tPassword,etPassword)) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                driverLoginRepo = new DriverLoginRepo(SigninActivity.this,email,password);
                driverLoginRepo.Login();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin:
                SignRider();
                break;
        }
    }
}
