package com.apptec.camello.mainactivity.fnotification.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.apptec.camello.R;
import com.apptec.camello.models.NotificationModel;

import timber.log.Timber;

public class DialogNotification extends DialogFragment {
    /**
     * Class for present the notification on a dialog
     */

    // UI elements
    private TextView sent_date, title_content, message_content;

    // Data
    private NotificationModel notification;


    public DialogNotification setNotification(NotificationModel notification) {
        /**
         * This method should be called before the dialog is show.
         * Otherwise would appear a NPE.
         */
        this.notification = notification;
        return this;
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
        View viewDialog = inflater.inflate(R.layout.dialog_notification, null);

        // Binding the UI elements
        sent_date = viewDialog.findViewById(R.id.text_view_sent_date);
        title_content = viewDialog.findViewById(R.id.text_view_title_content);
        message_content = viewDialog.findViewById(R.id.text_view_message_content);


        // Setting the content for the elements (Be sure the method setNotification i called before this happens)
        sent_date.setText(notification.getReadableSentDate());
        title_content.setText(notification.getTitle());
        message_content.setText(notification.getText());

        // Set a ok button to close the dialog
        builder.setView(viewDialog)
                .setPositiveButton(getString(R.string.notification_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Timber.d("Ok clicked");
                        DialogNotification.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
