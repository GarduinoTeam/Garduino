package com.example.garduinoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RuleAdapter extends BaseAdapter {
    Context context;
    List<Rule> listObjects;
    ArrayList<Rule> arrayList;

    public RuleAdapter(Context context, List<Rule> listObjects) {
        this.context = context;
        this.listObjects = listObjects;
        this.arrayList = new ArrayList<Rule>();
        this.arrayList.addAll(listObjects);
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listObjects.clear();
        if (charText.length()==0){
            listObjects.addAll(arrayList);
        }
        else {
            for (Rule rule : arrayList){
                if (rule.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    listObjects.add(rule);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return listObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listObjects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        LayoutInflater inflate = LayoutInflater.from(context);
        view = inflate.inflate(R.layout.rules_list, null);

        TextView title = (TextView) view.findViewById(R.id.textIrrigationRule);
        Switch sw = (Switch) view.findViewById(R.id.RuleActive);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        title.setText(listObjects.get(position).getTitle());
        return view;
    }
}
