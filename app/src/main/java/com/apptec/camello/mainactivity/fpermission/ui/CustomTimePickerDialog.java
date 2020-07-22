package com.apptec.camello.mainactivity.fpermission.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.apptec.camello.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import timber.log.Timber;

/**
 * Custom dialog of a time picker
 */
public class CustomTimePickerDialog extends DialogFragment {


    // UI elements
    NumberPicker hourPicker, minutePicker;
    TextView amButton, pmButton;

    boolean amSelected;

    // Listener instance
    private OnTimeSetListener listener;

    // Listener
    public static interface OnTimeSetListener {

        /**
         * Listener from this dialog
         *
         * @param selectedHour selected hour in format 24h
         * @param selectTime   selected minute
         */
        void onTimeSet(int selectedHour, int selectTime);
    }


    /**
     * Constructor
     */
    public CustomTimePickerDialog(OnTimeSetListener listener) {
        this.listener = listener;
    }

    /**
     * This method is called for create the dialog.
     * When the method show is called.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_time_picker_custom, null);

        // Find ui elements
        findViews(viewDialog);


        setUpInitialValues();

        // Set a ok button to close the dialog
        builder.setView(viewDialog)
                .setPositiveButton(getString(R.string.notification_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onTimeSet(hourPicker.getValue(), minutePicker.getValue());
                        Timber.d("Ok clicked");
                        CustomTimePickerDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    /**
     * Method that put default hour and minutes to the pickers
     */
    private void setUpInitialValues() {

        // Set max and min values for pickers
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(12);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);


        // Get the time now
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        SimpleDateFormat meridianFormat = new SimpleDateFormat("a");

        // Get the hour
        int currentHour = Integer.parseInt(hourFormat.format(calendar.getTime()));
        // Get the minutes
        int currentMinute = Integer.parseInt(minuteFormat.format(calendar.getTime()));
        // Get the meridian (AM or PM)
        String meridian = meridianFormat.format(calendar.getTime());

        Timber.d("Current hour: %s", currentHour);
        Timber.d("Current minute: %s", currentMinute);
        Timber.d("Meridian: %s", meridian);

        // If am is selected
        amSelected = meridian.equals("AM");


        // Put the current time to the pickers
        hourPicker.setValue(currentHour);
        minutePicker.setValue(currentMinute);


        updateButton();
        setUpButtonListener();

    }

    /**
     * Set the listener for the buttons
     */
    private void setUpButtonListener() {
        amButton.setOnClickListener(v -> {
            amSelected = true;
            updateButton();
        });
        pmButton.setOnClickListener(v -> {
            amSelected = false;
            updateButton();
        });
    }

    /**
     * Set the right colors for the selected button
     * AM or PM
     */
    private void updateButton() {
        if (amSelected) {
            amButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            amButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            pmButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            pmButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));
        } else {
            pmButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            pmButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            amButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            amButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));
        }
    }


    /**
     * Just bind the views
     *
     * @param viewDialog
     */
    private void findViews(View viewDialog) {
        hourPicker = viewDialog.findViewById(R.id.number_picker_hour);
        minutePicker = viewDialog.findViewById(R.id.number_picker_minute);
        amButton = viewDialog.findViewById(R.id.text_clickable_am);
        pmButton = viewDialog.findViewById(R.id.text_clickable_pm);

    }


}
