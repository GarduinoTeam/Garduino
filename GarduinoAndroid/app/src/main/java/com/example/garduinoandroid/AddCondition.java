package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
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

//        timeCondition = (Button) findViewById(R.id.timeCondition);
//        timeCondition.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            obj = (Data) getIntent().getExtras().getSerializable("object");
            informationBoolean = (Boolean) getIntent().getExtras().get("btnSettingsDPS");
            addRule = (Boolean) getIntent().getExtras().get("addRule");
        }


        //Start ListRules
        listView = (ListView) findViewById(R.id.listCondition);

        labelListItems = getResources().getStringArray(R.array.conditionsArray);

        conditionArrayList = new ArrayList<Condition>();
        conditionArrayList.add(new Condition(1, labelListItems[0]));
        conditionArrayList.add(new Condition(2, labelListItems[1]));
        conditionArrayList.add(new Condition(3, labelListItems[2]));
        conditionArrayList.add(new Condition(4, labelListItems[3]));
        conditionArrayList.add(new Condition(5, labelListItems[4]));

        ConditionAdapter adapter = new ConditionAdapter(getApplicationContext(), conditionArrayList);
        listView.setAdapter(adapter);

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
                startActivity(intent);
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
