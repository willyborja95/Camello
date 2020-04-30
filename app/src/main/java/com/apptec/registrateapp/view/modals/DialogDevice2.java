package com.apptec.registrateapp.view.modals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.apptec.registrateapp.R;

public class DialogDevice2 extends DialogFragment {

    // UI elements
    DialogDevice.NoticeDialogListener listener;
    EditText txtDeviceName;
    EditText txtDeviceModel;
    ProgressDialog progressDialog;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_device, null);

        txtDeviceName = (EditText) viewDialog.findViewById(R.id.edit_text_device_name);
        txtDeviceModel = (EditText) viewDialog.findViewById(R.id.edit_text_device_model);

        builder.setView(viewDialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("deviceLog", "click en ok");


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogDevice2.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
