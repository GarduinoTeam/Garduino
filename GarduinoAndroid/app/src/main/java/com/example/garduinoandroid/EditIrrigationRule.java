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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Irrigation rules name");

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
        }

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), IrrigationRules.class);
        myIntent.putExtra("object", (Serializable) obj);
        myIntent.putExtra("btnSettingsDPS", informationBoolean);
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
                startActivity(intentSave);
                break;

            case R.id.addCondition:
                Intent intentAdd = new Intent(this, AddCondition.class);
                intentAdd.putExtra("object", (Serializable) obj);
                intentAdd.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentAdd);
                break;

            case R.id.buttonTimeCondition:
                Intent intentTime = new Intent(this, TimeConditions.class);
                intentTime.putExtra("object", (Serializable) obj);
                intentTime.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentTime);
                break;
            default:
                break;
        }
    }
}
