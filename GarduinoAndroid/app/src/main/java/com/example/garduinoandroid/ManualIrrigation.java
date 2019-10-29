package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class ManualIrrigation extends AppCompatActivity implements View.OnClickListener
{
    private Button start;
    private Button cancel;

    Data obj;
    Boolean addRule;
    String manualIrrigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        manualIrrigation = getResources().getString(R.string.ManualIrrigation);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(manualIrrigation);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_irrigation);

        start = (Button) findViewById(R.id.btnMI1);
        start.setOnClickListener(this);

        cancel= (Button) findViewById(R.id.btnMI2);
        cancel.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnMI1:

                Intent intentStart = new Intent(this, DeviceProfileStart.class);
                intentStart.putExtra("object", (Serializable) obj);
                intentStart.putExtra("addRule", addRule);
                startActivity(intentStart);
                break;

            case R.id.btnMI2:
                Intent intentCancel = new Intent(this, DeviceProfile.class);
                intentCancel.putExtra("object", (Serializable) obj);
                intentCancel.putExtra("addRule", addRule);
                startActivity(intentCancel);
                break;

            default:
                break;
        }

    }
}
