package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class IrrigationRules extends AppCompatActivity implements View.OnClickListener
{
    Button save;
    Button add;
    RelativeLayout irrigationRuleAdded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.irrigation_rules);

        save = (Button) findViewById(R.id.saveIrrigationRules);
        save.setOnClickListener(this);

        add = (Button) findViewById(R.id.addRule);
        add.setOnClickListener(this);

        irrigationRuleAdded = (RelativeLayout) findViewById(R.id.irrigationRuleAdded);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.saveIrrigationRules:

                Intent intentSave = new Intent(this, SettingsInformation.class);
                startActivity(intentSave);
                break;

            case R.id.addRule:
                irrigationRuleAdded.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }
}
