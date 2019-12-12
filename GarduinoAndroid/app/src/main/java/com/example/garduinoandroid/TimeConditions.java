package com.example.garduinoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.io.Serializable;
import java.util.ArrayList;

public class TimeConditions extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Button save;
    Button startTime;
    Button endTime;
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
    }
}