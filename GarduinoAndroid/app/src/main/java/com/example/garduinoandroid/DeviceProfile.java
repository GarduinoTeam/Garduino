package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class DeviceProfile extends AppCompatActivity implements View.OnClickListener {
    private Button manualIrrigation;
    private Button settingsButton;
    boolean settingsDPS;

    Data obj;
    ImageView image;
    Boolean addRule;
    String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceName = getResources().getString(R.string.DeviceProfile);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(deviceName);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile);
        settingsDPS = false;
        image = (ImageView) findViewById(R.id.imageView1);

        manualIrrigation = (Button) findViewById(R.id.button1);


        settingsButton = (Button) findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(this);
        manualIrrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeviceProfile.this);
                View mView = getLayoutInflater().inflate(R.layout.manual_irrigation, null);
                final EditText numeric = (EditText) mView.findViewById(R.id.numericText);
                Button buttonStart = (Button) mView.findViewById(R.id.btnMI1);
                Button buttonCancel = (Button) mView.findViewById(R.id.btnMI2);

                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!numeric.getText().toString().isEmpty()) {
                            Toast.makeText(DeviceProfile.this, "Start Click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), DeviceProfileStart.class);
                            intent.putExtra("object", (Serializable) obj);
                            intent.putExtra("addRule", (Serializable) addRule);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeviceProfile.this, "Please fill the field.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentSettings = new Intent(getApplication(), DeviceProfile.class);
                        intentSettings.putExtra("object", (Serializable) obj);
                        intentSettings.putExtra("btnSettingsDPS", settingsDPS);
                        intentSettings.putExtra("addRule", (Serializable) addRule);
                        startActivity(intentSettings);
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            image.setImageResource(obj.getImage());
            addRule = (Boolean) getIntent().getExtras().getSerializable("addRule");
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                intentSettings.putExtra("object", (Serializable) obj);
                intentSettings.putExtra("btnSettingsDPS", settingsDPS);
                intentSettings.putExtra("addRule", addRule);
                startActivity(intentSettings);
                break;

            default:
                break;
        }

    }
}
