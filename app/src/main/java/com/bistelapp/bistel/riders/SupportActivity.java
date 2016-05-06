package com.bistelapp.bistel.riders;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.database.rider.UserLocalStorage;
import com.bistelapp.bistel.informations.rider.rider_info;

public class SupportActivity extends ActionBarActivity implements View.OnClickListener {


    TextView call,email;
    UserLocalStorage userLocalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        call = (TextView)findViewById(R.id.tv_call);
        email = (TextView)findViewById(R.id.tv_e);

        call.setOnClickListener(this);
        email.setOnClickListener(this);

        userLocalStorage = new UserLocalStorage(SupportActivity.this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_support, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + "09082700615"));
                startActivity(intent);
                break;
            case R.id.tv_e:
                rider_info ri = userLocalStorage.getRiderInfo();
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_EMAIL, "bistelint@gmail.com");
                intent1.putExtra(Intent.EXTRA_SUBJECT, "Support from "+ri.firstname+" "+ri.lastname);
                intent1.putExtra(Intent.EXTRA_TEXT, "");
                intent1.setType("message/rfc822");
                startActivity(intent1);
                break;
        }
    }
}
