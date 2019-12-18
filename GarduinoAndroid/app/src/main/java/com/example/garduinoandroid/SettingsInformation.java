package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.OutputStream;
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
    int deviceId;
    static String urlPut;
    String newName;
    EditText edit;

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

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");
        edit =  (EditText)findViewById(R.id.et_ds_1);

        urlPut = "http://10.0.2.2:8080/GarduinoApi/devices/update_device/"+deviceId;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/devices/get_device/"+deviceId);
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

    public void verValor(View v)
    {
        EditText edit = (EditText)findViewById(R.id.et_ds_1);
        EditText edit2 = (EditText) findViewById(R.id.et_ds_2);
        String result = edit.getText().toString();
        String numericResult = edit2.getText().toString();
        Log.d("TAG:",result);
        Log.d("TAG:",numericResult);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(informationBoolean) {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfileStart.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            myIntent.putExtra("deviceId",  deviceId);
            startActivityForResult(myIntent, 0);

        } else {
            Intent myIntent = new Intent(getApplicationContext(), DeviceProfile.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("addRule", addRule);
            myIntent.putExtra("deviceId",  deviceId);
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
                intent.putExtra("deviceId",  deviceId);
                startActivity(intent);
                break;
            case R.id.saveIrrigation:
                if(informationBoolean) {
                    Intent intentSave = new Intent(this, DeviceProfileStart.class);
                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    intentSave.putExtra("deviceId",  deviceId);
                    SaveDevice(intentSave);

                } else {
                    Intent intentSave = new Intent(this, DeviceProfile.class);

                    intentSave.putExtra("object", (Serializable) obj);
                    intentSave.putExtra("addRule", addRule);
                    intentSave.putExtra("deviceId",  deviceId);

//                    intentSave.putExtra("object", (Serializable) obj);
//                    intentSave.putExtra("addRule", addRule);
                    // ************************* Fer el post aqui ********************************** Crear classe Async i posarho al onclick com a new ..execute();
                    SaveDevice(intentSave);
                }
                break;
            default:
                break;
        }
    }

    private void SaveDevice(Intent intentSave){

        if(edit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Give a name for your device.", Toast.LENGTH_SHORT).show();
        } else {
            newName =  edit.getText().toString();
            DoPutTask task = new DoPutTask();
            task.execute(new String(urlPut));
            startActivity(intentSave);
        }
    }


    // READING AND MANAGING DATA FORM AN ENDPOINT
    private String createList(String jsonStr)
    {
        informationDevice = new ArrayList<Data>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");

                return name;

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
                String name = createList(jsonStr);
                edit.setText(name);
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
    private class DoPutTask extends AsyncTask<String, Void, String> {

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
                    conn.setRequestMethod("PUT");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");
                    String input = "{\"userId\":1, \"status\":\"DISCONNECTED\",\"name\":\""+newName+"\"}";

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
