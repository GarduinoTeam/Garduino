package com.example.garduinoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TimeConditions extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button save;
    String startTime;
    String endTime;
    String time;
    String date;
    Button dates;
    Data obj;
    Boolean informationBoolean;
    Boolean addRule;
    String timeConditions;

    ListView listView;
    String[] labelListItems;
    String[] descriptionItems;
    ArrayList<TimeCondition> timeConditionArrayList;
    int deviceId;
    int ruleId;
    int timeConditionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        timeConditions = getResources().getString(R.string.TimeConditions);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle(timeConditions);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_conditions);

        Bundle datos = this.getIntent().getExtras();
        deviceId = datos.getInt("deviceId");
        ruleId = datos.getInt("ruleId");
        timeConditionId = datos.getInt("TimeConditionId");

        save = (Button) findViewById(R.id.saveTimeCondition);
        save.setOnClickListener(this);
//
//        startTime = (Button) findViewById(R.id.buttonStartTime);
////        startTime.setOnClickListener(this);
//
//        endTime = (Button) findViewById(R.id.buttonEndTime);
//        //endTime.setOnClickListener(this);
//
//        dates = (Button) findViewById(R.id.dates);
//        //dates.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }

        //Start ListRules
        listView = (ListView) findViewById(R.id.listTimeCondition);

        labelListItems = getResources().getStringArray(R.array.timeConditionsArray);
        descriptionItems = getResources().getStringArray(R.array.timeConditionsDescriptionArray);

        timeConditionArrayList = new ArrayList<TimeCondition>();
        timeConditionArrayList.add(new TimeCondition(1, labelListItems[0],descriptionItems[0]));
        timeConditionArrayList.add(new TimeCondition(2, labelListItems[1],descriptionItems[1]));
        timeConditionArrayList.add(new TimeCondition(3, labelListItems[2],descriptionItems[2]));
        timeConditionArrayList.add(new TimeCondition(4, labelListItems[3],descriptionItems[3]));
        timeConditionArrayList.add(new TimeCondition(5, labelListItems[4],descriptionItems[4]));

        TimeConditionAdapter adapter = new TimeConditionAdapter(getApplicationContext(), timeConditionArrayList);
        listView.setAdapter(adapter);

        //End ListView


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 1){
                    showTimeDialog(view);
                }else if(id == 2){
                    showTimeDialog(view);
                }else if(id == 5){
                    showDateDialog(view);
                }
            }
        });
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
            case R.id.saveTimeCondition:
                Intent intentSave = new Intent(this, EditIrrigationRule.class);
                intentSave.putExtra("object", (Serializable) obj);
                intentSave.putExtra("btnSettingsDPS", informationBoolean);
                intentSave.putExtra("addRule", addRule);
                intentSave.putExtra("deviceId",  deviceId);
                intentSave.putExtra("ruleId",  ruleId);

//                String urlPut = "http://10.0.2.2:8080/GarduinoApi/ruletimeconditions/update_rule_time_condition/"+timeConditionId;
//                DoPutTask task = new DoPutTask();
//                task.execute(new String(urlPut));

                startActivity(intentSave);
                break;
           // case R.id.buttonStartTime:
//            case R.id.buttonEndTime:
//                Intent intentTime = new Intent(this, EditTime.class);
//                intentTime.putExtra("object", (Serializable) obj);
//                intentTime.putExtra("btnSettingsDPS", informationBoolean);
//                intentTime.putExtra("addRule", addRule);
//                startActivity(intentTime);
////                break;
//            case R.id.dates:
//                Intent intentDates = new Intent(this, EditDate.class);
//                intentDates.putExtra("object", (Serializable) obj);
//                intentDates.putExtra("btnSettingsDPS", informationBoolean);
//                intentDates.putExtra("addRule", addRule);
//                startActivity(intentDates);
//                break;
            default:
                break;
        }
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
                    //String input = "{\"startTime\":"+ startTime +"\"endTime\":}";
                    String input = "{\"specificDates\":"+ date +":}";

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

    public void showTimeDialog(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(getBaseContext(), new StringBuilder()
                .append(hourOfDay)
                .append(":")
                .append(minute), Toast.LENGTH_LONG).show();
        time = hourOfDay+":"+minute;
    }

    public void showDateDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(getBaseContext(), new StringBuilder()
                .append(dayOfMonth).append("/")
                .append(month+1)
                .append("/")
                .append(year), Toast.LENGTH_LONG).show();
        date = year + "-" + month + "-" + dayOfMonth;
    }
}