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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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

        if(convertView == null){
            LayoutInflater inflate = LayoutInflater.from(context);
            convertView = inflate.inflate(R.layout.deviceslist, null, true);
        }


        ImageView image = (ImageView) convertView.findViewById(R.id.iv_1);
        Picasso.get().load(listObjects.get(position).getImagePath()).transform(new RoundedCornersTransformation(80,5)).into(image);

        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        title.setText(listObjects.get(position).getTitle());

        TextView description = (TextView) convertView.findViewById(R.id.tv_description);
        description.setText(listObjects.get(position).getDescription());

        Switch sw = (Switch) convertView.findViewById(R.id.sw_1);

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

        return convertView;
    }
}
