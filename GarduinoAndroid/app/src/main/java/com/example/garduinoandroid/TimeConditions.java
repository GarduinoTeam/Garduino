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

public class TimeConditions extends AppCompatActivity implements View.OnClickListener {

    Button save;
    Button startTime;
    Button endTime;
    Data obj;
    boolean informationBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Time conditions");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_conditions);

        save = (Button) findViewById(R.id.saveTimeCondition);
        save.setOnClickListener(this);

        startTime = (Button) findViewById(R.id.buttonStartTime);
        startTime.setOnClickListener(this);

        endTime = (Button) findViewById(R.id.buttonEndTime);
        endTime.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveTimeCondition:
                Intent intentSave = new Intent(this, EditIrrigationRule.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentSave);
                break;
            case R.id.buttonStartTime:
            case R.id.buttonEndTime:
                Intent intentTime = new Intent(this, EditTime.class);
                intentTime.putExtra("object", (Serializable) obj);
                intentTime.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentTime);
                break;
            default:
                break;
        }
    }
}