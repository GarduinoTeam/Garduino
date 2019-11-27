package com.example.garduinoandroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener mCallback;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Return date to TimeConditions Activity
        mCallback.onDateSet(view, year, month, dayOfMonth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // The callback interface. If not, it throws an exception

        try {
            mCallback = (DatePickerDialog.OnDateSetListener) (Activity) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDateSetListener");
        }
    }
}

