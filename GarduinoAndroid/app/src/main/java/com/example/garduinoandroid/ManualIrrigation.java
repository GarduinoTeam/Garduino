package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManualIrrigation extends AppCompatActivity implements View.OnClickListener
{
    private Button start;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_irrigation);

        start = (Button) findViewById(R.id.btnMI1);
        start.setOnClickListener(this);

        cancel= (Button) findViewById(R.id.btnMI2);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnMI1:

                Intent intentStart = new Intent(this, DeviceProfileStart.class);
                startActivity(intentStart);
                break;

            case R.id.btnMI2:
                Intent intentCancel = new Intent(this, DeviceProfile.class);
                startActivity(intentCancel);
                break;

            default:
                break;
        }

    }
}
