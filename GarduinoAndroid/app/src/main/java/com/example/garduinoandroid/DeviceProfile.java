package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DeviceProfile extends AppCompatActivity implements View.OnClickListener
{
    private Button manualIrrigation;
    private Button settingsButton;
    boolean settingsDPS;

    Data obj;
    ImageView image;
    Boolean addRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle("Device name");

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile);
        settingsDPS = false;
        image = (ImageView) findViewById(R.id.imageView1);

        manualIrrigation = (Button) findViewById(R.id.button1);
        manualIrrigation.setOnClickListener(this);

        settingsButton = (Button) findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            image.setImageResource(obj.getImage());
            addRule = (Boolean) getIntent().getExtras().getSerializable("addRule");
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button1:
                Intent intent = new Intent(this, ManualIrrigation.class);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("addRule", (Serializable) addRule);
                startActivity(intent);
                break;

            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                intentSettings.putExtra("object", (Serializable) obj);
                intentSettings.putExtra("btnSettingsDPS", settingsDPS);
                intentSettings.putExtra("addRule", (Serializable) addRule);
                startActivity(intentSettings);
                break;

            default:
                break;
        }
    }
}
