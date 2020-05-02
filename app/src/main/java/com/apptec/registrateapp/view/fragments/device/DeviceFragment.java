package com.apptec.registrateapp.view.fragments.device;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.view.modals.DialogDevice2;
import com.apptec.registrateapp.viewmodel.SharedViewModel;

public class DeviceFragment extends Fragment {
    /**
     * Device fragment
     */

    private final String TAG = DeviceFragment.class.getSimpleName();

    // Instance of ViewModel
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);                    // Getting the view model
        sharedViewModel.setActiveFragmentName(getString(R.string.devices_fragment_title));

        // Observing if is needed to register this device
        sharedViewModel.getIsNeededRegisterDevice().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // Request register device
                if (aBoolean) {
                    registerNewDeviceDialog();
                }
            }
        });
    }

    private void registerNewDeviceDialog() {
        /**
         * Open the modal/ dialog device dialog
         */
        Log.d(TAG, "Opening dialog device.");
        DialogDevice2 dialogDevice = new DialogDevice2();
        dialogDevice.show(getFragmentManager(), "DialogDevice");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device, container, false);

        // TODO: Observe the mDevicesList


        return view;
    }
}
