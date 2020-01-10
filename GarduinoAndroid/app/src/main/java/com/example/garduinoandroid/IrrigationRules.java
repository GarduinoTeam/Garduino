package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

public class IrrigationRules extends AppCompatActivity implements View.OnClickListener
{
    String jsonStr;
    ListView listView;
    String[] labelListItems;
    ArrayList<Rule> ruleArrayList;


    Button save;
    Button add;
    RelativeLayout irrigationRuleAdded;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String irrigationRules;
    RuleAdapter adapter;
    int deviceId;
    final static String urlPost = "http://10.0.2.2:8080/GarduinoApi/rules/create_rule";
    String newName;
    int ruleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        irrigationRules = getResources().getString(R.string.IrrigationRules);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity)this).getSupportActionBar().setTitle(irrigationRules);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.irrigation_rules);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");


        listView = (ListView) findViewById(R.id.listRules);

        runOnUiThread(new Runnable() {
            @Override
           public void run() {
                new ReadJSON().execute("http://10.0.2.2:8080/GarduinoApi/rules/get_rules?device_id="+deviceId);
            }
        });


//        //Start ListRules
//        listView = (ListView) findViewById(R.id.listRules);
//
//        labelListItems = getResources().getStringArray(R.array.rulesArray);
//
//        ruleArrayList = new ArrayList<Rule>();
//        ruleArrayList.add(new Rule(1, labelListItems[0]));
//        ruleArrayList.add(new Rule(2, labelListItems[1]));
//        ruleArrayList.add(new Rule(3, labelListItems[2]));
//        ruleArrayList.add(new Rule(4, labelListItems[3]));
//
//        adapter = new RuleAdapter(getApplicationContext(), ruleArrayList);
//        listView.setAdapter(adapter);
//
//
//        //End ListView


        save = (Button) findViewById(R.id.saveIrrigationRules);
        save.setOnClickListener(this);

        add = (Button) findViewById(R.id.addRule);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(IrrigationRules.this);
                View mView = getLayoutInflater().inflate(R.layout.create_rule, null);
                final EditText EtRule = (EditText) mView.findViewById(R.id.editTextCR);
                Button buttonCreate = (Button) mView.findViewById(R.id.createRule);
                Button buttonCancel = (Button) mView.findViewById(R.id.cancelRule);

                buttonCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!EtRule.getText().toString().isEmpty()) {
                            Toast.makeText(IrrigationRules.this, "Name Rule added", Toast.LENGTH_SHORT).show();
                            addRule = true;
                            Intent intentCreate = new Intent(getApplication(), IrrigationRules.class);
                            intentCreate.putExtra("addRule",addRule);
                            intentCreate.putExtra("object", (Serializable) obj);
                            intentCreate.putExtra("btnSettingsDPS", informationBoolean);
                            intentCreate.putExtra("deviceId",  deviceId);
                            //************************* Fer el post aqui
                            //verValor(v);
                            newName =  EtRule.getText().toString();
                            DoPostTask task = new DoPostTask();
                            task.execute(new String(urlPost));
                            startActivity(intentCreate);
                        } else {
                            Toast.makeText(IrrigationRules.this, "Please fill the field.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCancel = new Intent(getApplication(), IrrigationRules.class);
                        intentCancel.putExtra("addRule",addRule);
                        intentCancel.putExtra("object", (Serializable) obj);
                        intentCancel.putExtra("btnSettingsDPS", informationBoolean);
                        intentCancel.putExtra("deviceId",  deviceId);
                        startActivity(intentCancel);
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }
//        if (addRule == null){
//            addRule = false;
//        }else if (addRule) {
//            irrigationRuleAdded.setVisibility(View.VISIBLE);
//        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rule RuleObject = (Rule) parent.getItemAtPosition(position);
                ruleId = RuleObject.getId();
                Intent intent = new Intent(getApplication(), EditIrrigationRule.class);
                intent.putExtra("RuleObject", (Serializable) RuleObject);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                intent.putExtra("deviceId",  deviceId);
                intent.putExtra("ruleId",  ruleId);
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


                    String input = "{\"idDevice\":"+deviceId+", \"status\":\"false\",\"name\":\""+newName+"\",\"type\":0}";                    System.out.println("-------");
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

    public void verValor(View v)
    {
        EditText edit = (EditText)findViewById(R.id.editTextCR);
        String textResult = edit.getText().toString();
        Log.d("TAG:",textResult);

    }


    private class ReadJSON extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String jsonStr) {
            ArrayList<Rule> result;
            if(jsonStr != null)
            {

                result = createList(jsonStr);
                View popUpDeleteView = getLayoutInflater().inflate(R.layout.delete_pop_up, null);
                adapter = new RuleAdapter(getApplicationContext(), result, IrrigationRules.this, popUpDeleteView);
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



    private ArrayList<Rule> createList(String jsonStr)
    {
        ruleArrayList = new ArrayList<Rule>();
        if(jsonStr != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);

                // Getting JSON Array
                JSONArray contacts = jsonObject.getJSONArray("rules");
                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    Integer id = c.getInt("id");
                    String name = c.getString("name");

                    ArrayList<Rule> contact = new ArrayList<Rule>();

                    // adding each child node to Arraylist Data
                    contact.add(new Rule(id, name));

                    // adding contact to ruleArrayList
                    ruleArrayList.addAll(contact);

                }
                return ruleArrayList;

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

    // CREATING MENU AND MANAGING MENU OPTIONS

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search name device...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(newText);
                }
                return  true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_search) return true;
        else {
            Intent myIntent = new Intent(getApplicationContext(), SettingsInformation.class);
            myIntent.putExtra("object", (Serializable) obj);
            myIntent.putExtra("btnSettingsDPS", informationBoolean);
            myIntent.putExtra("addRule", addRule);
            myIntent.putExtra("deviceId",  deviceId);
            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.saveIrrigationRules:
                Intent intentSave = new Intent(this, SettingsInformation.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                intentSave.putExtra("addRule", addRule);
                intentSave.putExtra("deviceId",  deviceId);
                startActivity(intentSave);
                break;

//            case R.id.addRule:
//                Intent intentAdd = new Intent(this, CreateRule.class);
//                intentAdd.putExtra("object", (Serializable) obj);
//                intentAdd.putExtra("btnSettingsDPS", informationBoolean);
//                intentAdd.putExtra("addRule", addRule);
//                startActivity(intentAdd);
//                break;
            default:
                break;
        }
    }
}
