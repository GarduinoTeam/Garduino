package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

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

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SettingsInformation extends AppCompatActivity implements View.OnClickListener {
    private Button irrigationRules;
    private Button saveIrrigation;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String deviceSettingsName;
    ArrayList<Data> informationDevice;
    TextView nameDevice;
    String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceSettingsName = getResources().getString(R.string.DeviceSettings);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(deviceSettingsName);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_information);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("https://api.androidhive.info/contacts/");
            }
        });

        irrigationRules = (Button) findViewById(R.id.irrigationRules);
        irrigationRules.setOnClickListener(this);

        saveIrrigation = (Button) findViewById(R.id.saveIrrigation);
        saveIrrigation.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(informationBoolean) {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfileStart.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            startActivityForResult(myIntent, 0);

        } else {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfile.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            startActivityForResult(myIntent, 0);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.irrigationRules:
                Intent intent = new Intent(this, IrrigationRules.class);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                intent.putExtra("addRule", addRule);
                startActivity(intent);
                break;
            case R.id.saveIrrigation:
                if(informationBoolean) {
                    Intent intentSave = new Intent(this, DeviceProfileStart.class);
                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    startActivity(intentSave);
                } else {
                    Intent intentSave = new Intent(this, DeviceProfile.class);
                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    startActivity(intentSave);
                }
                break;
            default:
                break;
        }
    }


    // READING AND MANAGING DATA FORM AN ENDPOINT
    private ArrayList<Data> createList(String jsonStr)
    {
        informationDevice = new ArrayList<Data>();
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

                    ArrayList<Data> contact = new ArrayList<Data>();

                    // adding each child node to ArrayList Data
                    contact.add(new Data(1, name, email, "https://img-cdn.hipertextual.com/files/2019/03/hipertextual-whatsapp-permitira-realizar-busqueda-inversa-imagenes-recibidas-combatir-fake-news-2019852284.jpg?strip=all&lossy=1&quality=57&resize=740%2C490&ssl=1", "Temperature: 40ºC","Moisture: 20%","Soil: 2"));

                    // adding contact to devicesList
                    informationDevice.addAll(contact);
                }
                return informationDevice;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else Toast.makeText(this, "Couldn't get json from file", Toast.LENGTH_SHORT).show();
        return null;
    }

    private class ReadJSON extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<Data> result;
            if (jsonStr != null) {
                result = createList(jsonStr);
                nameDevice = (TextView) findViewById(R.id.tv_si_1);

                for (Data dataDevice : result) {
                    System.out.println(dataDevice.getTitle());
//                }

                    nameDevice.setText(dataDevice.getTitle());
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }


    }
    private String readURL(String urlContacts)
    {
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
        return jsonStr;
    }
}
