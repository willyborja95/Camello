package com.apptec.registrateapp.mainactivity.fpermission.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.mainactivity.MainViewModel;
import com.apptec.registrateapp.models.PermissionModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PermissionFragment extends Fragment {
    /**
     * PermissionFragment
     */
    private static final String TAG = "PermissionFragment";


    // Instance of ViewModel
    private MainViewModel mainViewModel;

    // Ui elements
    FloatingActionButton floatingActionButton;
    ListView permissionsListView;
    PermissionListAdapter permissionListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);                    // Getting the view model
        mainViewModel.setActiveFragmentName(getString(R.string.permissions_fragment_title));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        floatingActionButton = view.findViewById(R.id.add_permission_floating_button);
        // TODO: Observe the mPermissionList
        mainViewModel.getPermissionsList().observe(this, new Observer<List<PermissionModel>>() {
            @Override
            public void onChanged(List<PermissionModel> permissionModels) {
                // TODO

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Open the dialog fragment to add a permission
                 */
                Log.d(TAG, "Open dialog");

                DialogPermission dialogPermission = new DialogPermission();
                dialogPermission.show(getFragmentManager(), TAG);


            }
        });


        // When the user scroll down, then syn the permissions
        view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollX - oldScrollX > 1) {
                    // If scroll down
                    mainViewModel.syncPermissions();
                }
            }
        });

        return view;
    }

}
