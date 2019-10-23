package com.example.garduinoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ImageView imageView;
    String[] labelListItems;
    Drawable[] imageListItems = new Drawable[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.DeviceList);
        imageListItems[0] = getResources().getDrawable(R.drawable.plant1);
        imageListItems[1] = getResources().getDrawable(R.drawable.plant2);
        imageListItems[2] = getResources().getDrawable(R.drawable.plant3);
        imageListItems[3] = getResources().getDrawable(R.drawable.plant4);
        //imageView = (ImageView) findViewById(R.id.ImagePlant);
        labelListItems = getResources().getStringArray(R.array.devicesArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.deviceslist,R.id.ItemName, labelListItems);
        listView.setAdapter(adapter);

    }
}
