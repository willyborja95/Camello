package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications;

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
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.adapters.DeviceListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FloatingActionButton fabAgregarEquipo;
    private EditText txtDeviceName;
    private EditText txtDeviceModel;
    private ListView lvDevices;
    private ArrayList<Device> lstDevice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        txtDeviceName = root.findViewById(R.id.txtDeviceName);
        txtDeviceModel = root.findViewById(R.id.txtDeviceModel);
        lvDevices = (ListView) root.findViewById(R.id.lvEquipos);

        lstDevice = new ArrayList<Device>();
        lstDevice.add(new Device("1", "huawei", "123"));
        lstDevice.add(new Device("2", "samsung", "456"));
        lstDevice.add(new Device("3", "samsung", "456"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));
        lstDevice.add(new Device("4", "samsung", "123"));

        lvDevices.setAdapter(new DeviceListAdapter(root.getContext(), lstDevice ));


        fabAgregarEquipo = (FloatingActionButton) root.findViewById(R.id.fabAgregarDispositivo);
        fabAgregarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cheetah!!!!");
                DialogFragment df = new DialogDevice();
                df.show(getFragmentManager(), "nani");

            }
        });


        return root;
    }
}