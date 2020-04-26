package com.apptec.registrateapp.view.fragments.notifications;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.view.adapters.NotificationsListAdapter;
import com.apptec.registrateapp.viewmodel.SharedViewModel;

import java.util.List;

public class NotificationsFragment extends Fragment implements NotificationView {
    /**
     * NotificationsFragment
     */

    private final String TAG = "NotificationsFragment";

    //UI elements
    private ListView notificationsListView;
    private NotificationsListAdapter notificationsListAdapter;
    private TextView notificationTextView;
    ProgressDialog progressDialog;



    // Instance of ViewModel
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);                    // Getting the view model
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationsListView = view.findViewById(R.id.notification_list_view);

        // Observing the view model mNotification
        notificationsListAdapter = new NotificationsListAdapter(App.getContext(), sharedViewModel.getNotifications());
        sharedViewModel.getNotifications().observe(getActivity(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                notificationsListView.setAdapter(notificationsListAdapter);
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Linking UI elements
        progressDialog = new ProgressDialog(getContext());
        notificationsListView = view.findViewById(R.id.notification_list_view);
        //notificationTextView = view.findViewById(R.id.notification_text_view);

        // showNotNewNotificationsMessage();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

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
        showDialog(getContext().getString(R.string.title_error_connection), getContext().getString(R.string.message_error_connection));
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

    }
}
