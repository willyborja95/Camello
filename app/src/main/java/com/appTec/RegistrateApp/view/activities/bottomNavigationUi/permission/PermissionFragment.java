package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.permission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class PermissionFragment extends Fragment {

    private FloatingActionButton fabAddPermission;
    private ArrayList<PermissionType> lstPermissionType;
    private ArrayList<Permission> lstPermission;
    private ListView lvPermission;

    public void addArrayListPermissionType(ArrayList<PermissionType> lstPermissionType){
        this.lstPermissionType = lstPermissionType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_permission, container, false);
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

    public void addPermissionList(ArrayList<Permission> lstPermission){
        this.lstPermission = lstPermission;
        updateListView();
    }

    private void updateListView(){
        lvPermission.setAdapter(new PermissionListAdapter(getContext(), lstPermission ));
    }
}