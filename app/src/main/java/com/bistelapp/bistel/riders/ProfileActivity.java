package com.bistelapp.bistel.riders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;
import com.bistelapp.bistel.internet.rider.UpdateRepo;
import com.bistelapp.bistel.utility.General;

public class ProfileActivity extends ActionBarActivity implements View.OnClickListener {

    General general;
    EditText etFirst,etLast,etMobile;
    Button update;

    TextView eEmail;
    UserLocalStorage userLocalStorage;

    UpdateRepo updateRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        general = new General(ProfileActivity.this);
        userLocalStorage = new UserLocalStorage(ProfileActivity.this);
        update = (Button)findViewById(R.id.update);

        etFirst = (EditText)findViewById(R.id.fn);
        etLast = (EditText)findViewById(R.id.ln);
        etMobile = (EditText)findViewById(R.id.tv_mobile);

        eEmail = (TextView)findViewById(R.id.em);

        rider_info ri = userLocalStorage.getRiderInfo();
        eEmail.setText(ri.email);
        etFirst.setText(ri.firstname);
        etLast.setText(ri.lastname);
        etMobile.setText(ri.mobile);

        update.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_password) {
            startActivity(new Intent(ProfileActivity.this,ChangePasswordActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                if(general.rider_check_all("first name","last name","mobile",etFirst,etLast,etMobile)){
                    String first = etFirst.getText().toString();
                    String last = etLast.getText().toString();
                    String mobile = etMobile.getText().toString();

                    updateRepo = new UpdateRepo(ProfileActivity.this,first,last,mobile);
                    updateRepo.updateRider();
                }
                break;
        }
    }
}
