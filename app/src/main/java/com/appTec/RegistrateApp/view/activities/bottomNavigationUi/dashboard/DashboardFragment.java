package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.appTec.RegistrateApp.view.activities.bottomNavigationUi.notifications.NotificationsViewModel;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.appTec.RegistrateApp.view.adapters.DeviceListAdapter;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DashboardFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FloatingActionButton fabAddPermission;
    private ArrayList<PermissionType> lstPermissionType;
    private ArrayList<Permission> lstPermission;
    private ListView lvPermission;
    private DashboardViewModel dashboardViewModel;

    public void addArrayListPermissionType(ArrayList<PermissionType> lstPermissionType){
        this.lstPermissionType = lstPermissionType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        lstPermission = new ArrayList<Permission>();
        lvPermission = (ListView) root.findViewById(R.id.lvPermission);
        fabAddPermission = (FloatingActionButton) root.findViewById(R.id.fabAddPermission);
        fabAddPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getContext(), DialogPermission.class);

                System.out.println("Cheetah!!!!");
                DialogPermission df = new DialogPermission();
                df.addArrayListPerissionType(lstPermissionType);
                df.show(getFragmentManager(), "nani");
            }
        });
        return root;
    }

    public void addPermissionToList(Permission permission){
        lstPermission.add(permission);
        updateListView();
    }

    private void updateListView(){
        lvPermission.setAdapter(new PermissionListAdapter(getContext(), lstPermission ));
    }
}