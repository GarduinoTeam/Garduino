package com.example.garduinoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditTextConditionAdapter extends BaseAdapter
{
    Context context;
    List<EditTextCondition> listObjects;
    ArrayList<EditTextCondition> arrayList;

    public EditTextConditionAdapter(Context context, List<EditTextCondition> listObjects) {
        this.context = context;
        this.listObjects = listObjects;
        this.arrayList = new ArrayList<EditTextCondition>();
        this.arrayList.addAll(listObjects);
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listObjects.clear();
        if (charText.length()==0){
            listObjects.addAll(arrayList);
        }
        else {
            for (EditTextCondition data : arrayList){
                if (data.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    listObjects.add(data);
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
        view = inflate.inflate(R.layout.edit_condition_list, null);

        EditText edit = (EditText) view.findViewById(R.id.editTextEditCondition);
        TextView title = (TextView) view.findViewById(R.id.textVieweEditCondition);
        TextView measure = (TextView) view.findViewById(R.id.measureEditCondition);
        Switch sw = (Switch) view.findViewById(R.id.switchEditCondition);

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
        edit.setText(listObjects.get(position).getEdit());
        measure.setText(listObjects.get(position).getMeasure());

        return view;
    }
}
