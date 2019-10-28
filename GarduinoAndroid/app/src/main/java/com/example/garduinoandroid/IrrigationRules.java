package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class IrrigationRules extends AppCompatActivity implements View.OnClickListener
{
    Button rule1;
    Button rule2;
    Button save;
    Button add;
    RelativeLayout irrigationRuleAdded;
    Data obj;
    Boolean informationBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle("Irrigation rules");

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.irrigation_rules);

        save = (Button) findViewById(R.id.saveIrrigationRules);
        save.setOnClickListener(this);

        add = (Button) findViewById(R.id.addRule);
        add.setOnClickListener(this);

        rule1 = (Button) findViewById(R.id.buttonIrrigationRule1);
        rule1.setOnClickListener(this);

        rule2 = (Button) findViewById(R.id.buttonIrrigationRule2);
        rule2.setOnClickListener(this);

        irrigationRuleAdded = (RelativeLayout) findViewById(R.id.irrigationRule2);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), SettingsInformation.class);
        myIntent.putExtra("object", (Serializable) obj);
        myIntent.putExtra("btnSettingsDPS", informationBoolean);
        startActivityForResult(myIntent, 0);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.saveIrrigationRules:
                Intent intentSave = new Intent(this, SettingsInformation.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentSave);
                break;

            case R.id.addRule:
                irrigationRuleAdded.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonIrrigationRule1:
            case R.id.buttonIrrigationRule2:
                Intent intentRule = new Intent(this, EditIrrigationRule.class);
                intentRule.putExtra("object", (Serializable) obj);
                intentRule.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentRule);
                break;
            default:
                break;
        }
    }
}
