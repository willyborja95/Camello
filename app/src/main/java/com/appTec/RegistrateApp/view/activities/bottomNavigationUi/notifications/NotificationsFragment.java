package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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


    // ? Presenter instance here. Not sure if this is the best way.
    NotificationPresenterImpl notificationPresenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.fragment_device, container, false);

        Bundle bundle = this.getArguments();


        notificationsListVIew = fragmentDeviceView.findViewById(R.id.notification_list_view);


        // Attached the presenter
        notificationPresenter = new NotificationPresenterImpl(this);

        // Calling the presenter
        notificationPresenter.getNotifications();


        return fragmentDeviceView;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }


    @Override
    public void getNotifications() {
        // Call to presenter
        notificationPresenter.getNotifications();
    }

    @Override
    public void showNotifications(ArrayList<Notification> notifications) {
        /*
        * Show notifications on screen
        * */
        notificationsListVIew.setAdapter(new NotificationsListAdapter(getContext(), notifications));
    }
}
