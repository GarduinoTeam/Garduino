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

public class CreateRule extends AppCompatActivity implements View.OnClickListener {

    Button create;
    Button cancel;
    Boolean addRule;
    Data obj;
    Boolean informationBoolean;
    String createRule;
    int deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        createRule = getResources().getString(R.string.CreateRules);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(createRule);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_rule);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");

        create = (Button) findViewById(R.id.createRule);
        create.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancelRule);
        cancel.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.createRule:
                Intent intentCreate = new Intent(this, IrrigationRules.class);
                addRule = true;
                intentCreate.putExtra("addRule",addRule);
                intentCreate.putExtra("object", (Serializable) obj);
                intentCreate.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intentCreate);
                break;

            case R.id.cancelRule:
                Intent intentCancel = new Intent(this, IrrigationRules.class);
                intentCancel.putExtra("addRule",addRule);
                intentCancel.putExtra("object", (Serializable) obj);
                intentCancel.putExtra("btnSettingsDPS", informationBoolean);
                intentCancel.putExtra("deviceId",  deviceId);
                startActivity(intentCancel);
                break;

            default:
                break;
        }
    }
}
