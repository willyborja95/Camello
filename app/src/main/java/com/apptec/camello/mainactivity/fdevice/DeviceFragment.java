package com.apptec.camello.mainactivity.fdevice;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.camello.App;
import com.apptec.camello.R;
import com.apptec.camello.mainactivity.BaseFragment;
import com.apptec.camello.mainactivity.MainViewModel;

/**
 * Device fragment
 */
public class DeviceFragment extends BaseFragment {

    private final String TAG = DeviceFragment.class.getSimpleName();

    // Instance of ViewModel
    private MainViewModel mainViewModel;

    // UI elements
    private ListView devicesListView;
    DeviceListAdapter deviceListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);                    // Getting the view model
        mainViewModel.setActiveFragmentName(getString(R.string.devices_fragment_title));


    }

    /**
     * Open the modal/ dialog device dialog
     */
    private void registerNewDeviceDialog() {

        Log.d(TAG, "Opening dialog device.");
        DialogDevice dialogDevice = new DialogDevice();
        dialogDevice.show(getChildFragmentManager(), "DialogDevice");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device, container, false);

        // Observing if is needed to register this device
        mainViewModel.getIsNeededRegisterDevice().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                // Request register device
                if (aBoolean) {
                    registerNewDeviceDialog();
                } else {
                    onSuccessProcess(null, null);
                }
            }
        });

        devicesListView = view.findViewById(R.id.device_list_view);


        // Observe the mDevicesList
        deviceListAdapter = new DeviceListAdapter(App.getContext(), mainViewModel.getDevices());
        mainViewModel.getDevices().observe(getActivity(), devices -> devicesListView.setAdapter(deviceListAdapter));

        hideRefreshButton();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.processConsumed();
        onSuccessProcess(null, null);
    }
}
