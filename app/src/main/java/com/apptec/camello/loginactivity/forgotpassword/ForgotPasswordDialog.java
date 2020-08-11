package com.apptec.camello.loginactivity.forgotpassword;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.camello.R;
import com.apptec.camello.loginactivity.LoginViewModel;

import timber.log.Timber;

/**
 * Fragment to let the user recover the password
 */
public class ForgotPasswordDialog extends DialogFragment {


    // UI elements
    private EditText targetEmail;

    // Get a reference to the login view model
    LoginViewModel loginViewModel;

    /**
     * This method should be called before the dialog is show.
     * Otherwise would appear a NPE.
     */
    public ForgotPasswordDialog() {

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
        View viewDialog = inflater.inflate(R.layout.dialog_recover_password, null);

        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);              // Getting the view model

        // Binding the UI elements
        targetEmail = viewDialog.findViewById(R.id.input_email);


        // Set a ok button to close the dialog
        builder.setView(viewDialog)
                .setPositiveButton(getString(R.string.recover_password_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Timber.d("Ok clicked");
                        loginViewModel.recoverPassword(targetEmail.getText().toString());
                        ForgotPasswordDialog.this.getDialog().cancel();


                    }
                });

        return builder.create();
    }

}
