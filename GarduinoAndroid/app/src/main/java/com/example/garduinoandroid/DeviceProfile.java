package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceProfile extends AppCompatActivity implements View.OnClickListener
{
    private Button show_information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile);

        show_information = (Button) findViewById(R.id.button1);
        show_information.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ManualIrrigation.class);
        startActivity(intent);
    }
}
