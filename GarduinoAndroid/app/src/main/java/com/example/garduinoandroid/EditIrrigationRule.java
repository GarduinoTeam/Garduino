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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class EditIrrigationRule extends AppCompatActivity implements View.OnClickListener{

    String jsonStr;
    ListView listViewTime;
    String[] labelListItems;
    String[] valueListItems;
    String[] measureListItems;
    ArrayList<EditTextCondition> conditionArrayList;
    EditTextConditionAdapter adapter;

    RuleAdapter adapterTime;
    ListView listViewEdit;
    ArrayList<Rule> timeConditonArrayList;
    String[] labelListTimeCondition;

    Button save;
    Button add;
    Button time;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String irrigationRules;
    int deviceId;
    int ruleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        irrigationRules = getResources().getString(R.string.EditIrrigationRules);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(irrigationRules);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_irrigation_rule);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");
        ruleId = datos.getInt("ruleId");
        listViewEdit = (ListView) findViewById(R.id.listEditTextConditions);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/ruleconditions/get_rule_conditions?rule_id="+ruleId);
            }
        });

        save = (Button) findViewById(R.id.saveConditons);
        save.setOnClickListener(this);

        add = (Button) findViewById(R.id.addCondition);
        add.setOnClickListener(this);

//        time = (Button) findViewById(R.id.buttonTimeCondition);
//        time.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }

        //Start ListConditions
//        listViewEdit = (ListView) findViewById(R.id.listEditTextConditions);
//
//        labelListItems = getResources().getStringArray(R.array.conditionLabel);
//        valueListItems = getResources().getStringArray(R.array.valueCondition);
//        measureListItems = getResources().getStringArray(R.array.measureCondition);
//
//        conditionArrayList = new ArrayList<EditTextCondition>();
//        conditionArrayList.add(new EditTextCondition(1, labelListItems[0],valueListItems[0],measureListItems[0]));
//        conditionArrayList.add(new EditTextCondition(2, labelListItems[1],valueListItems[1],measureListItems[1]));
//        conditionArrayList.add(new EditTextCondition(3, labelListItems[2],valueListItems[2],measureListItems[2]));
//
//
//        adapter = new EditTextConditionAdapter(getApplicationContext(), conditionArrayList);
//        listViewEdit.setAdapter(adapter);

        //End ListView

        //Start ListTimeConditions
        listViewTime = (ListView) findViewById(R.id.listTimeConditons);

        labelListTimeCondition = getResources().getStringArray(R.array.timeConditionLabel);

        timeConditonArrayList = new ArrayList<Rule>();
        timeConditonArrayList.add(new Rule(1, labelListTimeCondition[0]));
        timeConditonArrayList.add(new Rule(2, labelListTimeCondition[1]));

        adapterTime = new RuleAdapter(getApplicationContext(), timeConditonArrayList);
        listViewTime.setAdapter(adapterTime);

        listViewTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rule timeCondition = (Rule) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplication(), TimeConditions.class);
                intent.putExtra("timeCondition", (Serializable) timeCondition);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                intent.putExtra("deviceId",  deviceId);
                intent.putExtra("ruleId",  ruleId);

                startActivity(intent);
            }
        });

    }

    private class ReadJSON extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<EditTextCondition> result;
            if(jsonStr != null)
            {
                result = createList(jsonStr);

                adapter = new EditTextConditionAdapter(getApplicationContext(), result);
                listViewEdit.setAdapter(adapter);

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

    private ArrayList<EditTextCondition> createList(String jsonStr)
    {
        conditionArrayList = new ArrayList<EditTextCondition>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array
                JSONArray contacts = jsonObject.getJSONArray("ruleconditions");
                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    Integer id = c.getInt("id");
                    String conditionValue = c.getString("conditionValue");
                    String name = c.getString("name");
                    String measure = c.getString("measure");

                    ArrayList<EditTextCondition> contact = new ArrayList<EditTextCondition>();

                    // adding each child node to Arraylist Data
                    contact.add(new EditTextCondition(id, name, conditionValue, measure));

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
        Intent myIntent = new Intent(getApplicationContext(), IrrigationRules.class);
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
            case R.id.saveConditons:
                Intent intentSave = new Intent(this, IrrigationRules.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                intentSave.putExtra("addRule", addRule);
                intentSave.putExtra("deviceId",  deviceId);
                intentSave.putExtra("ruleId",  ruleId);

                startActivity(intentSave);
                break;

            case R.id.addCondition:
                Intent intentAdd = new Intent(this, AddCondition.class);
                intentAdd.putExtra("object", (Serializable) obj);
                intentAdd.putExtra("btnSettingsDPS", informationBoolean);
                intentAdd.putExtra("addRule", addRule);
                intentAdd.putExtra("deviceId",  deviceId);
                intentAdd.putExtra("ruleId",  ruleId);

                startActivity(intentAdd);
                break;

//            case R.id.buttonTimeCondition:
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
