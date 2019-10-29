package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DeviceProfileStart extends AppCompatActivity implements View.OnClickListener
{
    private Button cancelIrrigation;
    private Button settingBtn;

    ImageView image;
    Data obj;
    boolean settingsDPS;
    Boolean addRule;

    String deviceProfileStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceProfileStart = getResources().getString(R.string.DeviceProfileStart);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(deviceProfileStart);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile_start);

        settingsDPS = true;

        cancelIrrigation = (Button) findViewById(R.id.btnDPS);
        cancelIrrigation.setOnClickListener(this);

        settingBtn = (Button) findViewById(R.id.btnSettingsStart);
        settingBtn.setOnClickListener(this);

        image = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            addRule = (Boolean) getIntent().getExtras().getSerializable("addRule");
            image.setImageResource(obj.getImage());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnDPS:
                Intent intent = new Intent(this, DeviceProfile.class);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("addRule", addRule);
                startActivity(intent);
                break;

            case R.id.btnSettingsStart:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                intentSettings.putExtra("object", (Serializable) obj);
                intentSettings.putExtra("btnSettingsDPS", settingsDPS);
                intentSettings.putExtra("addRule", addRule);
                startActivity(intentSettings);
                break;

            default:
                break;
        }
    }
}

