package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.presenter.NotificationPresenterImpl;
import com.appTec.RegistrateApp.view.adapters.NotificationsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements NotificationView {


    //UI elements
    private ListView notificationsListVIew;
    private TextView notificationTextView;
    ProgressDialog progressDialog;


    // ? Presenter instance here. Not sure if this is the best way.
    NotificationPresenterImpl notificationPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.fragment_device, container, false);

        Bundle bundle = this.getArguments();


        // Attached the presenter
        notificationPresenter = new NotificationPresenterImpl(this);

        // Calling the presenter
        notificationPresenter.getNotifications();



        // Linking UI elements
        progressDialog = new ProgressDialog(getContext());
        notificationsListVIew = fragmentDeviceView.findViewById(R.id.notification_list_view);
        notificationTextView = fragmentDeviceView.findViewById(R.id.notification_text_view);

        return fragmentDeviceView;
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
        notificationsListVIew.setAdapter(new NotificationsListAdapter(getContext(), notifications));
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
        if(notificationTextView != null){
            notificationTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notificationPresenter.detachView();
        notificationPresenter.detachJob();
    }
}
