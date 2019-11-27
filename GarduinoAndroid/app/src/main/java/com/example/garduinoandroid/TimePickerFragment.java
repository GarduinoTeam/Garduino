package com.example.garduinoandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimePickerDialog.OnTimeSetListener mCallback;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Returns time to main activity
        mCallback.onTimeSet(view, hourOfDay, minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // The callback interface. If not, it throws an exception.
        try {
            mCallback = (TimePickerDialog.OnTimeSetListener) (Activity) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnTimeSetListener");
        }
    }


}
