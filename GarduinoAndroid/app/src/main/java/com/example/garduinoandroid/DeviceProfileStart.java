package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceProfileStart extends AppCompatActivity implements View.OnClickListener
{
    private Button cancelIrrigation;
    private Button settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile_start);

        cancelIrrigation = (Button) findViewById(R.id.btnDPS);
        cancelIrrigation.setOnClickListener(this);

        settingBtn = (Button) findViewById(R.id.btnSettings);
        settingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnDPS:
                Intent intent = new Intent(this, DeviceProfile.class);
                startActivity(intent);
                break;

            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                startActivity(intentSettings);
                break;

            default:
                break;
        }
    }
}

