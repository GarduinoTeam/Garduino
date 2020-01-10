package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

import static java.lang.Integer.parseInt;

public class DeviceProfile extends AppCompatActivity {
    private Button manualIrrigation;
    boolean settingsDPS;
    ArrayList<Data>informationDevice;
    String jsonStr;

    Data obj;
    ImageView image;
    TextView soil;
    TextView moisture;
    TextView temperature;
    Boolean addRule;
    String deviceName;
    int deviceId;
    int time;

    static String urlPost = "http://10.0.2.2:8080/GarduinoApi/operations/irrigate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        deviceName = getResources().getString(R.string.DeviceProfile);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(deviceName);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");
        time = datos.getInt("time");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_profile);
        settingsDPS = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/devices/get_device/"+deviceId);
            }
        });
        manualIrrigation = (Button) findViewById(R.id.button1);

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
                            time = Integer.parseInt(String.valueOf(numeric.getText()));
                            Intent intent = new Intent(getApplication(), DeviceProfileStart.class);
                            intent.putExtra("object", (Serializable) obj);
                            intent.putExtra("time", time);
                            intent.putExtra("addRule", (Serializable) addRule);
                            intent.putExtra("deviceId",  deviceId);

                            DoPostTaskIrrigate task = new DoPostTaskIrrigate();
                            task.execute(new String(urlPost));
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
                        intentSettings.putExtra("deviceId",  deviceId);


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
            //image.setImageResource(obj.getImage());
            addRule = (Boolean) getIntent().getExtras().getSerializable("addRule");
        }

    }
    // NAVBAR MENU
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
                intentSettings.putExtra("deviceId",  deviceId);
                startActivity(intentSettings);

            case R.id.action_refresh:
                return true;

            default:
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    // READING AND MANAGING DATA FORM AN ENDPOINT
    private ArrayList<Data> createList(String jsonStr)
    {
        informationDevice = new ArrayList<Data>();
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
                    informationDevice.addAll(contact);
                } catch (JSONException e) {
                e.printStackTrace();
            }
            return informationDevice;

        } else Toast.makeText(this, "Couldn't get json from file", Toast.LENGTH_SHORT).show();
        return null;
    }

    private class ReadJSON extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<Data> result;
            if (jsonStr != null) {
                System.out.println("*******************************");
                result = createList(jsonStr);
                image = (ImageView) findViewById(R.id.imageView1);
                temperature = (TextView) findViewById(R.id.tv_dvp_1);
                moisture = (TextView) findViewById(R.id.tv_dvp_2);
                soil = (TextView) findViewById(R.id.tv_dvp_3);
                System.out.println("*******************************");
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
    private class DoPostTaskIrrigate extends AsyncTask<String, Void, String> {

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


                    String input = "{\"device_id\": \""+deviceId+"\", \"irrigationTime\":\""+time+"\"}";

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

