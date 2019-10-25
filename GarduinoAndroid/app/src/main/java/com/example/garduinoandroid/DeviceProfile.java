package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeviceProfile extends AppCompatActivity implements View.OnClickListener
{
    private Button manualIrrigation;
    private Button settingsButton;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile);

        image = (ImageView) findViewById(R.id.imageView1);

        manualIrrigation = (Button) findViewById(R.id.button1);
        manualIrrigation.setOnClickListener(this);

        settingsButton = (Button) findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Data obj = (Data) getIntent().getExtras().getSerializable("object");
            image.setImageResource(obj.getImage());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button1:
                Intent intent = new Intent(this, ManualIrrigation.class);
                startActivity(intent);
                break;

            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                startActivity(intentSettings);
                break;

            default:
                break;
        }
    }
}
