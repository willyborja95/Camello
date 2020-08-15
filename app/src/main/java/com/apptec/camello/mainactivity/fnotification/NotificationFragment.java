package com.apptec.camello.mainactivity.fnotification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentNotificationBinding;
import com.apptec.camello.mainactivity.MainViewModel;
import com.apptec.camello.mainactivity.fnotification.ui.NotificationAdapter;

import timber.log.Timber;

/**
 * NotificationsFragment
 */
public class NotificationFragment extends Fragment {

    //UI elements
    private NotificationAdapter notificationListAdapter;


    // Instances of ViewModel
    private MainViewModel mainViewModel;
    private NotificationViewModel notificationViewModel;

    // Using data binding
    private FragmentNotificationBinding binding;

    /**
     * Link the view model
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up view models
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);                    // Getting the main view model
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);       // Getting the view model for this fragment

        mainViewModel.setActiveFragmentName(getString(R.string.notifications_fragment_title));
    }

    /**
     * Inflate the view.
     * Observe the notifications Live Data that is got from the Room database
     * Create an onItemClickListener to show a dialog about each notification when is pressed.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);


        // Bind the view models
        //binding.setMainViewModel(mainViewModel);
        binding.setNotificationViewModel(notificationViewModel);

        // Observing the view model mNotification
        notificationListAdapter = new NotificationAdapter(mainViewModel.getNotifications(), getChildFragmentManager());
        binding.notificationListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainViewModel.getNotifications().observe(
                getActivity(), notificationModelList -> {
                    if (notificationModelList.isEmpty()) {
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


        // Set up the refresh button
        setUpRefreshButton();

        return binding.getRoot();
    }

    /**
     * Method for get the ready and visible the refresh button
     */
    private void setUpRefreshButton() {
        // TODO
//        ImageView refreshButton = binding.getRoot().findViewById(R.id.refresh_button);
//        refreshButton.setVisibility(View.VISIBLE);
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainViewModel.syncNotifications();
//            }
//        });
    }


}
