package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.adapters.DeviceListAdapter;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FloatingActionButton fabAgregarEquipo;
    private EditText txtDeviceName;
    private EditText txtDeviceModel;
    private ListView lvDevices;
    private ArrayList<Device> lstDevice = new ArrayList<Device>();;

    public void addDeviceToList(Device device){
        lstDevice.clear();
        lstDevice.add(device);
        updateListView();
        if(lstDevice.size()>0){
            fabAgregarEquipo.hide();
        }else{
            fabAgregarEquipo.show();
        }
    }

    private void updateListView(){
        lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice ));
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        txtDeviceName = root.findViewById(R.id.txtDeviceName);
        txtDeviceModel = root.findViewById(R.id.txtDeviceModel);
        lvDevices = (ListView) root.findViewById(R.id.lvEquipos);
        lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice ));
        fabAgregarEquipo = (FloatingActionButton) root.findViewById(R.id.fabAgregarDispositivo);

        fabAgregarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = new DialogDevice();
                df.show(getFragmentManager(), "nani");
            }
        });


        return root;
    }
}