package com.bistelapp.bistel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.riders.RiderActivity;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    Button driver,rider;
    UserLocalStorage userLocalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driver = (Button)findViewById(R.id.driver);
        rider = (Button)findViewById(R.id.rider);

        driver.setOnClickListener(this);
        rider.setOnClickListener(this);

        userLocalStorage = new UserLocalStorage(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userLocalStorage.getLoggedUser()){
            startActivity(new Intent(MainActivity.this, RiderActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        switch (v.getId()){
            case R.id.driver:
                bundle.putString("dr","driver");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rider:
                bundle.putString("dr","rider");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
