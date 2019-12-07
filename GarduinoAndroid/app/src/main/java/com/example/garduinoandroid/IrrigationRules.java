package com.example.garduinoandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.Serializable;
import java.util.ArrayList;

public class IrrigationRules extends AppCompatActivity implements View.OnClickListener
{
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

        //Start ListRules
        listView = (ListView) findViewById(R.id.listRules);

        labelListItems = getResources().getStringArray(R.array.rulesArray);

        ruleArrayList = new ArrayList<Rule>();
        ruleArrayList.add(new Rule(1, labelListItems[0]));
        ruleArrayList.add(new Rule(2, labelListItems[1]));
        ruleArrayList.add(new Rule(3, labelListItems[2]));
        ruleArrayList.add(new Rule(4, labelListItems[3]));

        adapter = new RuleAdapter(getApplicationContext(), ruleArrayList);
        listView.setAdapter(adapter);


        //End ListView


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
        if (addRule == null){
            addRule = false;
        }else if (addRule) {
            irrigationRuleAdded.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rule RuleObject = (Rule) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplication(), EditIrrigationRule.class);
                intent.putExtra("RuleObject", (Serializable) RuleObject);
                intent.putExtra("object", (Serializable) obj);
                intent.putExtra("btnSettingsDPS", informationBoolean);
                startActivity(intent);
            }
        });

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
