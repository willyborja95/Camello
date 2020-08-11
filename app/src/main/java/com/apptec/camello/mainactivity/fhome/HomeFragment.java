package com.apptec.camello.mainactivity.fhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentHomeBinding;
import com.apptec.camello.mainactivity.BaseFragment;
import com.apptec.camello.util.Constants;

import java.util.Locale;

import timber.log.Timber;

/**
 * Home fragment
 */
public class HomeFragment extends BaseFragment {


    // Using data binding
    private FragmentHomeBinding binding;

    // Constant for the calendar view
    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-419");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel.setActiveFragmentName(getString(R.string.home_fragment_title));


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false); // Inflate the view

        // Set the main viewmodel instance to the layout
        binding.setMainViewModel(mainViewModel);


        this.setupCalendar();                        // Setting up the calendar view

        this.setupCentralButtonManager();                       // Setting up the button

        this.requestLocationPermissions();


        binding.fragmentHomeStartButton.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Verify first if the location service is available and the if
                     * the user is in a work zone
                     */
                    @Override
                    public void onClick(View v) {
                        Timber.d("Button clicked");

                        mainViewModel.changeLastWorkingState();

                    }
                });


        return binding.getRoot();
    }


    /**
     * Setting up an observer to the mLastWorkingPeriod
     */
    private void setupCentralButtonManager() {

        mainViewModel.getLastWorkingPeriod().observe(getViewLifecycleOwner(), workingPeriod -> {
            try {
                Timber.d("Working period status: %s", workingPeriod.getStatus());
                if (workingPeriod.getStatus() == Constants.INT_NOT_INIT_STATUS) {
                    // The user is working
                    Timber.d("Update the UI to not working");
                    binding.fragmentHomeStartButton.setText(getString(R.string.home_button_start_message));
                    binding.fragmentHomeButtonMessage.setText(getString(R.string.home_text_view_start_message));

                } else {
                    // It is not
                    Timber.d("Update the UI to working");
                    binding.fragmentHomeStartButton.setText(getString(R.string.home_button_finish_message));
                    binding.fragmentHomeButtonMessage.setText(getString(R.string.home_text_view_finish_message));

                }
            } catch (NullPointerException npe) {
                // It is not working
                Timber.d("Update the UI to not working");
                binding.fragmentHomeStartButton.setText(getString(R.string.home_button_start_message));
                binding.fragmentHomeButtonMessage.setText(getString(R.string.home_text_view_start_message));

            }

        });

    }


    /**
     * Here is the logic for setup the calendar
     */
    private void setupCalendar() {

        // TODO

    }


    /**
     * Method to create a dialog and request the permission about location
     */
    private void requestLocationPermissions() {
        mainViewModel.isNeededToRequestLocationPermissions().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            }
        });
    }


}
