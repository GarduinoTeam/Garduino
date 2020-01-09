package com.example.garduinoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class DeletePopUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("*******OnCreatePopUP********");
        super.onCreate(savedInstanceState);
    }

    public void showPopUp(final IrrigationRules context, final View mView, final RuleAdapter adapter, final int position, final String urlDelete){
        System.out.println("*******ShowPopUP********");
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context).setCancelable(false);
        //View mView = getLayoutInflater().inflate(R.layout.delete_pop_up, null);
        Button buttonYes = (Button) mView.findViewById(R.id.btnyes);
        Button buttonNo = (Button) mView.findViewById(R.id.btnno);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("*******YesPopUP********");
                adapter.doPositiveClick(position,urlDelete);
                dialog.dismiss();
                ((ViewGroup)mView.getParent()).removeView(mView);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((ViewGroup)mView.getParent()).removeView(mView);
            }
        });

    }
}
