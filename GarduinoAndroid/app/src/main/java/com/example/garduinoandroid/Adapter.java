package com.example.garduinoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends BaseAdapter
{
    Context context;
    List<Data> listObjects;
    ArrayList<Data> arrayList;

    public Adapter(Context context, List<Data> listObjects) {
        this.context = context;
        this.listObjects = listObjects;
        this.arrayList = new ArrayList<Data>();
        this.arrayList.addAll(listObjects);
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        listObjects.clear();
        if (charText.length()==0){
            listObjects.addAll(arrayList);
        }
        else {
            for (Data data : arrayList){
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
        view = inflate.inflate(R.layout.deviceslist, null);

        ImageView image = (ImageView) view.findViewById(R.id.iv_1);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        TextView description = (TextView) view.findViewById(R.id.tv_description);
        Switch sw = (Switch) view.findViewById(R.id.sw_1);

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
        description.setText(listObjects.get(position).getDescription());
        image.setImageResource(listObjects.get(position).getImage());

        return view;
    }
}
