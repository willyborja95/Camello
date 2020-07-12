package com.apptec.camello.mainactivity.fpermission.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.apptec.camello.R;

import timber.log.Timber;

/**
 * Custom dialog of a time picker
 */
public class CustomTimePickerDialog extends DialogFragment {


    // UI elements


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


        // Set a ok button to close the dialog
        builder.setView(viewDialog)
                .setPositiveButton(getString(R.string.notification_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Timber.d("Ok clicked");
                        CustomTimePickerDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
