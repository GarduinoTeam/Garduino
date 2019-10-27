package com.example.garduinoandroid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class TimeConditions extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        ((AppCompatActivity) this).getSupportActionBar().setTitle("Irrigation rules");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_conditions);
    }

    @Override
    public void onClick(View v) {

    }
}
