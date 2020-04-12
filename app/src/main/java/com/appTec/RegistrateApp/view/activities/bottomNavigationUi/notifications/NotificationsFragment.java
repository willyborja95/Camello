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
import com.appTec.RegistrateApp.view.adapters.NotificationsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {


    //UI elements
    private ListView notificationsListVIew;

    //Business logic elements
    private ArrayList<Notification> notifications = new ArrayList<Notification>();



    private void updateListView() {
        notificationsListVIew.setAdapter(new NotificationsListAdapter(getContext(), notifications));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.fragment_device, container, false);

        Bundle bundle = this.getArguments();

        Notification device = (Notification) bundle.getSerializable("notification");



        notificationsListVIew = (ListView) fragmentDeviceView.findViewById(R.id.notification_list_view);
        notificationsListVIew.setAdapter(new NotificationsListAdapter(getContext(), notifications));

        updateListView();


        return fragmentDeviceView;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }



}
