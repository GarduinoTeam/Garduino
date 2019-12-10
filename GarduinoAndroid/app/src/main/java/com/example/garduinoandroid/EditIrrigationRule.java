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

public class EditIrrigationRule extends AppCompatActivity implements View.OnClickListener{


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
        listViewEdit = (ListView) findViewById(R.id.listEditTextConditions);

        labelListItems = getResources().getStringArray(R.array.conditionLabel);
        valueListItems = getResources().getStringArray(R.array.valueCondition);
        measureListItems = getResources().getStringArray(R.array.measureCondition);

        conditionArrayList = new ArrayList<EditTextCondition>();
        conditionArrayList.add(new EditTextCondition(1, labelListItems[0],valueListItems[0],measureListItems[0]));
        conditionArrayList.add(new EditTextCondition(2, labelListItems[1],valueListItems[1],measureListItems[1]));
        conditionArrayList.add(new EditTextCondition(3, labelListItems[2],valueListItems[2],measureListItems[2]));


        adapter = new EditTextConditionAdapter(getApplicationContext(), conditionArrayList);
        listViewEdit.setAdapter(adapter);

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
                startActivity(intent);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), IrrigationRules.class);
        myIntent.putExtra("object", (Serializable) obj);
        myIntent.putExtra("btnSettingsDPS", informationBoolean);
        myIntent.putExtra("addRule", addRule);
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
                startActivity(intentSave);
                break;

            case R.id.addCondition:
                Intent intentAdd = new Intent(this, AddCondition.class);
                intentAdd.putExtra("object", (Serializable) obj);
                intentAdd.putExtra("btnSettingsDPS", informationBoolean);
                intentAdd.putExtra("addRule", addRule);
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
