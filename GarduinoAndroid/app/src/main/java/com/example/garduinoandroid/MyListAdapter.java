package com.example.garduinoandroid;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] maintitle;
    private final Integer[] imgid;

    public MyListAdapter(Activity context, String[] maintitle, Integer[] imgid) {
        super(context, R.layout.deviceslist, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.deviceslist, null, true);

        Button titleText = (Button) rowView.findViewById(R.id.ButtonDevice);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ImagePlant);


        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;
    }
}
