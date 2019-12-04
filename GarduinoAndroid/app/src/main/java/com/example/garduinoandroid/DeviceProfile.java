package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceProfile extends AppCompatActivity implements View.OnClickListener {
    private Button manualIrrigation;
    private Button settingsButton;
    boolean settingsDPS;
    ArrayList<HashMap<String, String>> informationData;
    String jsonStr;

    final static String urlContacts = "https://api.androidhive.info/contacts/";


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
        new GetDataInformation().execute();

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

    private void createList(String jsonStr)
    {
        informationData = new ArrayList<>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array
                JSONArray contacts = jsonObject.getJSONArray("contacts");

                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");

                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    String home = phone.getString("home");

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("mobile", mobile);
                    contact.put("home", home);

                    // adding contact to devicesList
                    informationData.add(contact);
                }
                System.out.println(informationData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else Toast.makeText(this, "Couldn't get json from file", Toast.LENGTH_SHORT).show();
    }

    private class GetDataInformation extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(jsonStr != null)
            {
                createList(jsonStr);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            InputStreamReader isReader = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlContacts);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                inputStream = conn.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if(inputStream == null){
                    // Nothing to do
                    return null;
                }
                isReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(isReader);
                String line;

                while((line = reader.readLine()) != null)
                {
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0)
                {
                    // Stream was empty. No point in parsing.
                    return null;
                }

                jsonStr = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null){ conn.disconnect(); }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}
