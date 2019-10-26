package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class SettingsInformation extends AppCompatActivity implements View.OnClickListener {
    private Button irrigationRules;
    private Button saveIrrigation;
    Data obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle("Device settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_information);

        irrigationRules = (Button) findViewById(R.id.irrigationRules);
        irrigationRules.setOnClickListener(this);

        saveIrrigation = (Button) findViewById(R.id.saveIrrigation);
        saveIrrigation.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.irrigationRules:
                Intent intent = new Intent(this, IrrigationRules.class);
                intent.putExtra("object", (Serializable) obj);
                startActivity(intent);
                break;
            case R.id.saveIrrigation:
                Intent intentSave = new Intent(this, DeviceProfile.class);
                intentSave.putExtra("object", (Serializable) obj);
                startActivity(intentSave);
                break;
            default:
                break;
        }
    }
}
