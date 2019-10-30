package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class SettingsInformation extends AppCompatActivity implements View.OnClickListener {
    private Button irrigationRules;
    private Button saveIrrigation;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String deviceSettingsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceSettingsName = getResources().getString(R.string.DeviceSettings);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(deviceSettingsName);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_information);

        irrigationRules = (Button) findViewById(R.id.irrigationRules);
        irrigationRules.setOnClickListener(this);

        saveIrrigation = (Button) findViewById(R.id.saveIrrigation);
        saveIrrigation.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(informationBoolean) {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfileStart.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            startActivityForResult(myIntent, 0);

        } else {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfile.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            startActivityForResult(myIntent, 0);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.irrigationRules:
                Intent intent = new Intent(this, IrrigationRules.class);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                intent.putExtra("addRule", addRule);
                startActivity(intent);
                break;
            case R.id.saveIrrigation:
                if(informationBoolean) {
                    Intent intentSave = new Intent(this, DeviceProfileStart.class);
                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    startActivity(intentSave);
                } else {
                    Intent intentSave = new Intent(this, DeviceProfile.class);
                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    startActivity(intentSave);
                }
                break;
            default:
                break;
        }
    }
}
