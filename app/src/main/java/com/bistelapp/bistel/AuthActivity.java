package com.bistelapp.bistel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.bistelapp.bistel.drivers.DriverRegisterActivity;
import com.bistelapp.bistel.riders.RegisterActivity;


public class AuthActivity extends ActionBarActivity implements View.OnClickListener{

    Button signin,register;
    String dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        signin = (Button)findViewById(R.id.sign);
        register = (Button)findViewById(R.id.reg);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            dr = bundle.getString("dr");
            //Toast.makeText(AuthActivity.this, "You're a " + dr, Toast.LENGTH_LONG).show();
        }

        signin.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()){
            case R.id.sign:
                Intent intent = new Intent(AuthActivity.this, SigninActivity.class);
                bundle.putString("option",dr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.reg:
                if(dr.contentEquals("rider")) {
                    Intent intent1 = new Intent(AuthActivity.this, RegisterActivity.class);
                    bundle.putString("option", dr);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }else if(dr.contentEquals("driver")){
                    Intent intent2 = new Intent(AuthActivity.this, DriverRegisterActivity.class);
                    bundle.putString("option", dr);
                    intent2.putExtras(bundle);
                    startActivity(intent2);
                }
                break;
        }
    }
}
