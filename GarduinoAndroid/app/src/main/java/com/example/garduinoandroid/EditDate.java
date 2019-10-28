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

public class EditDate extends AppCompatActivity implements View.OnClickListener {

    Button ok;
    Button cancel;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Time conditions");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_date);

        ok = (Button) findViewById(R.id.okDate);
        ok.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancelDate);
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
            case R.id.okDate:
            case R.id.cancelDate:
                Intent intentSetTime = new Intent(this, TimeConditions.class);
                intentSetTime.putExtra("object", (Serializable) obj);
                intentSetTime.putExtra("btnSettingsDPS", informationBoolean);
                intentSetTime.putExtra("addRule", addRule);
                startActivity(intentSetTime);
                break;
            default:
                break;
        }
    }
}
