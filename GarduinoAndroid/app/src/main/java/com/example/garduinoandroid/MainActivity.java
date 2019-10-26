package com.example.garduinoandroid;

import androidx.appcompat.app.AppCompatActivity;


import android.app.admin.DeviceAdminInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] labelListItems;
    ArrayList<Data> dataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listData);
        labelListItems = getResources().getStringArray(R.array.devicesArray);

        dataArrayList = new ArrayList<Data>();
        dataArrayList.add(new Data(1, labelListItems[0], "Last irrigation: today at 11:00", R.drawable.plant1 ));
        dataArrayList.add(new Data(2, labelListItems[1], "Last irrigation: today at 09:00", R.drawable.plant2 ));
        dataArrayList.add(new Data(3, labelListItems[2], "Last irrigation: today at 14:00", R.drawable.plant3 ));
        dataArrayList.add(new Data(4, labelListItems[3], "Last irrigation: today at 21:00", R.drawable.plant4 ));

        Adapter adapter = new Adapter(getApplicationContext(), dataArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data obj = (Data) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DeviceProfile.class);
                intent.putExtra("object", (Serializable) obj);
                Intent i = new Intent(getApplicationContext(), DeviceProfileStart.class);
                i.putExtra("object", (Serializable) obj);
                startActivity(intent);
            }
        });
    }
}
