package com.apptec.registrateapp.mainactivity.fnotification.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.databinding.FragmentNotificationBinding;
import com.apptec.registrateapp.mainactivity.MainViewModel;
import com.apptec.registrateapp.mainactivity.fnotification.NotificationViewModel;
import com.apptec.registrateapp.models.NotificationModel;

import java.util.List;

import timber.log.Timber;

public class NotificationFragment extends Fragment {
    /**
     * NotificationsFragment
     */


    //UI elements
    private ListView notificationsListView;
    private NotificationListAdapter notificationListAdapter;


    // Instances of ViewModel
    private MainViewModel mainViewModel;
    private NotificationViewModel notificationViewModel;

    // Using data binding
    private FragmentNotificationBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /**
         * Link the view model
         */
        super.onCreate(savedInstanceState);

        // Setting up view models
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);                    // Getting the main view model
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);       // Getting the view model for this fragment


        mainViewModel.setActiveFragmentName(getString(R.string.notifications_fragment_title));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the view.
         * Observe the notifications Live Data that is got from the Room database
         * Create an onItemClickListener to show a dialog about each notification when is pressed.
         */

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationsListView = view.findViewById(R.id.notification_list_view);

        // Observing the view model mNotification
        notificationListAdapter = new NotificationListAdapter(App.getContext(), mainViewModel.getNotifications());
        mainViewModel.getNotifications().observe(getActivity(), new Observer<List<NotificationModel>>() {
            @Override
            public void onChanged(List<NotificationModel> notifications) {
                notificationsListView.setAdapter(notificationListAdapter);
            }
        });

        // When clicked show a dialog with more information
        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Show a dialog with extended information about the dialog
                 */
                Timber.d("Item clicked");
                NotificationModel notification = notificationListAdapter.getItem(position);
                DialogNotification dialogNotification = new DialogNotification().setNotification(notification);
                dialogNotification.show(getFragmentManager(), DialogNotification.class.getSimpleName());
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * This method could be use for show a message that there are not new notifications.
         * Or maybe the on change Live Data into the observer.
         */

        // Linking UI elements

        notificationsListView = view.findViewById(R.id.notification_list_view);
        // notificationTextView = view.findViewById(R.id.notification_text_view);

        // showNotNewNotificationsMessage();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }


}
