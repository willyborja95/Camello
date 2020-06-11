package com.apptec.registrateapp.loginactivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.apptec.registrateapp.R;

public class ResultDialog extends DialogFragment {
    /**
     * This dialog will be used to show the result of the login process in case it fails
     */

    // UI elements
    TextView title_message, message;

    Integer title_resource, message_resource;


    public ResultDialog(Integer title_resource, Integer message_resource) {
        this.title_resource = title_resource;
        this.message_resource = message_resource;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /**
         * This method is called for create the dialog.
         * When the method show is called.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_login_result, null);

        title_message = viewDialog.findViewById(R.id.login_result_title);
        message = viewDialog.findViewById(R.id.login_result_message);

        title_message.setText(title_resource);
        message.setText(message_resource);

        builder.setView(viewDialog);

        return builder.create();
    }
}
