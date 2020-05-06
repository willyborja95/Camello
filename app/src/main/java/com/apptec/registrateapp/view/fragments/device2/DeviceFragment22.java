package com.apptec.registrateapp.view.fragments.device2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.presenter.DevicePresenterImpl;
import com.apptec.registrateapp.repository.localdatabase.DatabaseAdapter;
import com.apptec.registrateapp.view.modals.DialogDevice2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class DeviceFragment22 extends Fragment implements DeviceView2 {

    //UI elements
    private FloatingActionButton fabAddDevice;
    private EditText txtDeviceName;
    private EditText txtDeviceModel;
    private ListView lvDevices;

    //Business logic elements
    private ArrayList<Device> lstDevice = new ArrayList<Device>();
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;
    TelephonyManager telephonyManager;

    // ? Instance of device presenter here.
    DevicePresenterImpl devicePresenter;


    public void addDeviceToList(Device device) {
        lstDevice.clear();
        lstDevice.add(device);
        updateListView();
        checkDisplayingButton();
    }

    private void updateListView() {
        // lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.fragment_device2, container, false);

        Bundle bundle = this.getArguments();
        Device device = (Device) bundle.getSerializable("device");

        // Initialize the presenter
        devicePresenter = new DevicePresenterImpl(this);


        fabAddDevice = (FloatingActionButton) fragmentDeviceView.findViewById(R.id.fabAgregarDispositivo);
        txtDeviceName = fragmentDeviceView.findViewById(R.id.edit_text_device_name);
        txtDeviceModel = fragmentDeviceView.findViewById(R.id.edit_text_device_model);
        lvDevices = (ListView) fragmentDeviceView.findViewById(R.id.lvEquipos);
        // lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice));
        if(device!=null){
            lstDevice.clear();
            lstDevice.add(device);
        }
        updateListView();
        pref = getContext().getSharedPreferences("RegistrateApp", 0);
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDevice2 df = new DialogDevice2();
                df.show(getFragmentManager(),"DialogDevice");
            }
        });
        checkDisplayingButton();
        return fragmentDeviceView;
    }

    public void saveDevice(Device device) {
        addDeviceToList(device);
    }

    public void checkDisplayingButton(){
        if(lstDevice.size()>0){
            fabAddDevice.hide();
        }else{
            fabAddDevice.show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void getDevices() {
        /** Call the presenter */

    }

    @Override
    public void showDevices(ArrayList<Device> devices) {
        /** Show the devices on screen */

    }
}

