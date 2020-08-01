package com.apptec.camello.mainactivity.fdevice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.camello.R;
import com.apptec.camello.mainactivity.MainViewModel;

import timber.log.Timber;

/**
 * DialogDevice
 */
public class DialogDevice extends DialogFragment {


    // UI elements
    EditText et_name;
    EditText et_model;
    ProgressDialog progressDialog;

    // View model
    private MainViewModel mainViewModel;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);                    // Getting the view model

        progressDialog = new ProgressDialog(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_device, null);

        et_name = (EditText) viewDialog.findViewById(R.id.edit_text_device_name);
        et_model = (EditText) viewDialog.findViewById(R.id.edit_text_device_model);

        builder.setView(viewDialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Timber.d("Ok button clicked");

                        String name = et_name.getText().toString();
                        String model = et_model.getText().toString();

                        mainViewModel.saveThisDevice(name, model);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogDevice.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
