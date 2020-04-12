package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.presenter.NotificationPresenterImpl;
import com.appTec.RegistrateApp.view.adapters.NotificationsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements NotificationView {

    private final String TAG = "NotificationsFragment";

    //UI elements
    private ListView notificationsListView;
    private TextView notificationTextView;
    ProgressDialog progressDialog;


    // ? Presenter instance here. Not sure if this is the best way.
    NotificationPresenterImpl notificationPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.notifications_fragment_notification, container, false);

        Bundle bundle = this.getArguments();


        return fragmentDeviceView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Attached the presenter
        notificationPresenter = new NotificationPresenterImpl(this);

        // Calling the presenter
        notificationPresenter.getNotifications();

        // Linking UI elements
        progressDialog = new ProgressDialog(getContext());
        notificationsListView = view.findViewById(R.id.notification_list_view);
        notificationTextView = view.findViewById(R.id.notification_text_view);

        showNotNewNotificationsMessage();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }


    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        /*
         * Show notifications on screen
         * */


        notificationsListView.setAdapter(new NotificationsListAdapter(getContext(), notifications));
    }

    //Dialogs
    @Override
    public void showAssistanceProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideAssistanceProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showConnectionErrorMessage() {
        showDialog("Error de conexión", "Al parecer no hay conexión a Internet.");
    }

    @Override
    public void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.show();
    }

    @Override
    public void showNotNewNotificationsMessage() {
        if (notificationTextView != null) {

            notificationTextView.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "notificationTextView it is null");
        }
        if (notificationsListView != null) {

            notificationsListView.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "notificationListView it is null");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notificationPresenter.detachView();
        notificationPresenter.detachJob();
    }
}
