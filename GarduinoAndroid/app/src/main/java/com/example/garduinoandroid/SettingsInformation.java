package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsInformation extends AppCompatActivity implements View.OnClickListener {
    private Button irrigationRules;
    private Button saveIrrigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_information);

        irrigationRules = (Button) findViewById(R.id.irrigationRules);
        irrigationRules.setOnClickListener(this);

        saveIrrigation = (Button) findViewById(R.id.saveIrrigation);
        saveIrrigation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.irrigationRules:
                Intent intent = new Intent(this, IrrigationRules.class);
                startActivity(intent);
                break;
            case R.id.saveIrrigation:
                Intent intentSave = new Intent(this, DeviceProfile.class);
                startActivity(intentSave);
                break;
            default:
                break;
        }
    }
}
