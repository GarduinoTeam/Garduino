package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DeviceProfileStart extends AppCompatActivity implements View.OnClickListener
{
    private Button cancelIrrigation;
    ArrayList<Data> informationData;
    String jsonStr;

    ImageView image;
    Data obj;
    boolean settingsDPS;
    Boolean addRule;

    String deviceProfileStart;

    final static String urlContacts = "https://api.androidhive.info/contacts/";
    private TextView temperature;
    private TextView moisture;
    private TextView soil;
    int deviceId;

    static String urlPost = "http://10.0.2.2:8080/GarduinoApi/operations/stop_irrigate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceProfileStart = getResources().getString(R.string.DeviceProfileStart);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(deviceProfileStart);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile_start);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");

        settingsDPS = true;

        cancelIrrigation = (Button) findViewById(R.id.btnDPS);
        cancelIrrigation.setOnClickListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/devices/get_device/"+deviceId);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            addRule = (Boolean) getIntent().getExtras().getSerializable("addRule");
            //image.setImageResource(obj.getImage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsInformation.class);
                intentSettings.putExtra("object", (Serializable) obj);
                intentSettings.putExtra("btnSettingsDPS", settingsDPS);
                intentSettings.putExtra("addRule", addRule);
                intentSettings.putExtra("deviceId", deviceId);
                startActivity(intentSettings);
                break;

            case R.id.action_refresh:
                break;
            default:
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnDPS:
                Intent intent = new Intent(this, DeviceProfile.class);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("addRule", addRule);
                intent.putExtra("deviceId", deviceId);

                DoPostTaskStopIrrigate task = new DoPostTaskStopIrrigate();
                task.execute(new String(urlPost));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    // READING AND MANAGING DATA FORM AN ENDPOINT
    private ArrayList<Data> createList(String jsonStr)
    {
        informationData = new ArrayList<>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                Integer id = jsonObject.getInt("id");
                String temperature = jsonObject.getString("temperature");
                String soil = jsonObject.getString("soil");
                String humidity = jsonObject.getString("humidity");
                String imagePath = jsonObject.getString("imageAndroidURL");

                ArrayList<Data> contact = new ArrayList<Data>();

                // adding each child node to ArrayList Data
                contact.add(new Data(id, null, null, imagePath, temperature, humidity, soil));

                // adding contact to devicesList
                informationData.addAll(contact);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return informationData;

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
                image = (ImageView) findViewById(R.id.imageView);
                temperature = (TextView) findViewById(R.id.tv_dvs_1);
                moisture = (TextView) findViewById(R.id.tv_dvs_2);
                soil = (TextView) findViewById(R.id.tv_dvs_3);
                for (Data dataDevice : result) {
                    Picasso.get().load(result.get(0).getImagePath()).transform(new RoundedCornersTransformation(80,5)).into(image);
                    System.out.println(dataDevice.getTitle());
                    System.out.println(dataDevice.getTemperature());
//                    System.out.println("***************");
//                }

                    temperature.setText(dataDevice.getTemperature());
                    moisture.setText(dataDevice.getMoisture());
                    soil.setText(dataDevice.getSoil());
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

    private class DoPostTaskStopIrrigate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            HttpURLConnection conn = null;
            InputStream inputStream = null;
            InputStreamReader inputReader = null;
            BufferedReader reader = null;

            for (String url : urls) {
                try {
                    URL myUrl = new URL(url);
                    conn = (HttpURLConnection) myUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");


                    String input = "{\"device_id\": \""+deviceId+"\"}";

                    System.out.println(input);
                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = conn.getInputStream();
                        inputReader = new InputStreamReader(inputStream);
                        BufferedReader buffer = new BufferedReader(inputReader);

                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.out.println(response);
            return response;
        }
    }
}

