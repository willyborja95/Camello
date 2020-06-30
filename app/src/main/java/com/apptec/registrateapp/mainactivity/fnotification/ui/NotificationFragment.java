package com.apptec.registrateapp.mainactivity.fnotification.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.R;
import com.apptec.registrateapp.databinding.FragmentNotificationBinding;
import com.apptec.registrateapp.mainactivity.MainViewModel;
import com.apptec.registrateapp.mainactivity.fnotification.NotificationViewModel;
import com.apptec.registrateapp.models.NotificationModel;

import timber.log.Timber;

public class NotificationFragment extends Fragment {
    /**
     * NotificationsFragment
     */


    //UI elements
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);


        // Bind the view models
        //binding.setMainViewModel(mainViewModel);
        binding.setNotificationViewModel(notificationViewModel);

        // Observing the view model mNotification
        notificationListAdapter = new NotificationListAdapter(App.getContext(), mainViewModel.getNotifications());
        mainViewModel.getNotifications().observe(
                getActivity(),
                notificationModels -> {
                    if (notificationModels.isEmpty()) {
                        Timber.d("List notifications is empty");
                        // If the list is empty
                        binding.notificationListView.setVisibility(View.GONE);
                        binding.noNotifications.setVisibility(View.VISIBLE);
                    } else {
                        Timber.d("List notifications is not empty");
                        // If is  not empty
                        binding.notificationListView.setVisibility(View.VISIBLE);
                        binding.noNotifications.setVisibility(View.GONE);
                        binding.notificationListView.setAdapter(notificationListAdapter);
                    }
                }


        );

        // When clicked show a dialog with more information
        binding.notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        return binding.getRoot();
    }


}
