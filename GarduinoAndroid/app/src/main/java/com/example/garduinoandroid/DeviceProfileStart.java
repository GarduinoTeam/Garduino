package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DeviceProfileStart extends AppCompatActivity implements View.OnClickListener
{
    private Button cancelIrrigation;
    private Button settingBtn;

    ImageView image;
    Data obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile_start);

        cancelIrrigation = (Button) findViewById(R.id.btnDPS);
        cancelIrrigation.setOnClickListener(this);

        settingBtn = (Button) findViewById(R.id.btnSettings);
        settingBtn.setOnClickListener(this);

        image = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            image.setImageResource(obj.getImage());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnDPS:
                Intent intent = new Intent(this, DeviceProfile.class);
                intent.putExtra("object", (Serializable) obj);
                startActivity(intent);
                break;

            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                intentSettings.putExtra("object", (Serializable) obj);
                startActivity(intentSettings);
                break;

            default:
                break;
        }
    }
}

