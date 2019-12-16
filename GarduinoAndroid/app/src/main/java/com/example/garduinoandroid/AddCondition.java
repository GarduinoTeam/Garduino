package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddCondition extends AppCompatActivity implements View.OnClickListener{

    ListView listView;
    String[] labelListItems;
    ArrayList<Condition> conditionArrayList;

    Button timeCondition;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String addCond;
    int deviceId;
    int ruleId;
    String jsonStr;
    ConditionAdapter adapter;
    Condition conditionToCreate;
    final static String urlPost = "http://10.0.2.2:8080/GarduinoApi/ruleconditions/create_rule_condition";

    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        addCond = getResources().getString(R.string.AddCond);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(addCond);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_condition);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");
        ruleId = datos.getInt("ruleId");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/conditions/get_conditions");
            }
        });


//        timeCondition = (Button) findViewById(R.id.timeCondition);
//        timeCondition.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
        listView = (ListView) findViewById(R.id.listCondition);

        //Start ListRules
//        listView = (ListView) findViewById(R.id.listCondition);
//
//        labelListItems = getResources().getStringArray(R.array.conditionsArray);
//
//        conditionArrayList = new ArrayList<Condition>();
//        conditionArrayList.add(new Condition(1, labelListItems[0]));
//        conditionArrayList.add(new Condition(2, labelListItems[1]));
//        conditionArrayList.add(new Condition(3, labelListItems[2]));
//        conditionArrayList.add(new Condition(4, labelListItems[3]));
//        conditionArrayList.add(new Condition(5, labelListItems[4]));
//
//        ConditionAdapter adapter = new ConditionAdapter(getApplicationContext(), conditionArrayList);
//        listView.setAdapter(adapter);

        //End ListView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Condition ConditionObject = (Condition) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplication(), EditIrrigationRule.class);
                intent.putExtra("ConditionObject", (Serializable) ConditionObject);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                intent.putExtra("deviceId",  deviceId);
                intent.putExtra("ruleId",  ruleId);

                conditionToCreate = ConditionObject;
                DoPostTask task = new DoPostTask();
                task.execute(new String(urlPost));


                startActivity(intent);
            }
        });


    }

    private class DoPostTask extends AsyncTask<String, Void, String> {

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


                    String input = "{\"idRule\":"+ruleId+", \"idCondition\":"+ conditionToCreate.getId() +",\"conditionValue\":0}";
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

    private class ReadJSON extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<Condition> result;
            if(jsonStr != null)
            {
                result = createList(jsonStr);

                adapter = new ConditionAdapter(getApplicationContext(), result);
                listView.setAdapter(adapter);

//                for(Data dataDevice: result) {
//                    System.out.println(dataDevice.getTitle());
//                    System.out.println("***************");
//                }

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

    private ArrayList<Condition> createList(String jsonStr)
    {
        conditionArrayList = new ArrayList<Condition>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array
                JSONArray contacts = jsonObject.getJSONArray("conditions");
                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    Integer id = c.getInt("id");
                    String name = c.getString("name");

                    ArrayList<Condition> contact = new ArrayList<Condition>();

                    // adding each child node to Arraylist Data
                    contact.add(new Condition(id, name));

                    // adding contact to ruleArrayList
                    conditionArrayList.addAll(contact);

                }
                return conditionArrayList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else Toast.makeText(this, "Couldn't get json from file", Toast.LENGTH_SHORT).show();
        return null;
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), EditIrrigationRule.class);
        myIntent.putExtra("object", (Serializable) obj);
        myIntent.putExtra("btnSettingsDPS", informationBoolean);
        myIntent.putExtra("addRule", addRule);
        myIntent.putExtra("deviceId",  deviceId);
        myIntent.putExtra("ruleId",  ruleId);

        startActivityForResult(myIntent, 0);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.timeCondition:
//                Intent intentTime = new Intent(this, TimeConditions.class);
//                intentTime.putExtra("object", (Serializable) obj);
//                intentTime.putExtra("btnSettingsDPS", informationBoolean);
//                intentTime.putExtra("addRule", addRule);
//                startActivity(intentTime);
//                break;
            default:
                break;
        }
    }
}
