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

public class EditIrrigationRule extends AppCompatActivity implements View.OnClickListener{
    Button save;
    Button add;
    Button time;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String irrigationRules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        irrigationRules = getResources().getString(R.string.EditIrrigationRules);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(irrigationRules);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_irrigation_rule);

        save = (Button) findViewById(R.id.saveConditons);
        save.setOnClickListener(this);

        add = (Button) findViewById(R.id.addCondition);
        add.setOnClickListener(this);

        time = (Button) findViewById(R.id.buttonTimeCondition);
        time.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), IrrigationRules.class);
        myIntent.putExtra("object", (Serializable) obj);
        myIntent.putExtra("btnSettingsDPS", informationBoolean);
        myIntent.putExtra("addRule", addRule);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveConditons:
                Intent intentSave = new Intent(this, IrrigationRules.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                intentSave.putExtra("addRule", addRule);
                startActivity(intentSave);
                break;

            case R.id.addCondition:
                Intent intentAdd = new Intent(this, AddCondition.class);
                intentAdd.putExtra("object", (Serializable) obj);
                intentAdd.putExtra("btnSettingsDPS", informationBoolean);
                intentAdd.putExtra("addRule", addRule);
                startActivity(intentAdd);
                break;

            case R.id.buttonTimeCondition:
                Intent intentTime = new Intent(this, TimeConditions.class);
                intentTime.putExtra("object", (Serializable) obj);
                intentTime.putExtra("btnSettingsDPS", informationBoolean);
                intentTime.putExtra("addRule", addRule);
                startActivity(intentTime);
                break;
            default:
                break;
        }
    }
}
