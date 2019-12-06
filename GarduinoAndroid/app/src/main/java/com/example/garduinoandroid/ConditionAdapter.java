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

import java.util.List;

public class ConditionAdapter extends BaseAdapter {
    Context context;
    List<Condition> listObjects;

    public ConditionAdapter(Context context, List<Condition> listObjects) {
        this.context = context;
        this.listObjects = listObjects;
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
        view = inflate.inflate(R.layout.conditions_list, null);

        TextView title = (TextView) view.findViewById(R.id.textConditions);

        title.setText(listObjects.get(position).getTitle());
        return view;
    }
}