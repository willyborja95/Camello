package com.apptec.registrateapp.mainactivity.fpermission.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.mainactivity.MainViewModel;
import com.apptec.registrateapp.models.PermissionModel;

import java.util.List;

public class PermissionFragment extends Fragment {
    /**
     * PermissionFragment
     */

    // Instance of ViewModel
    private MainViewModel mainViewModel;

    // Ui elements


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);                    // Getting the view model
        mainViewModel.setActiveFragmentName(getString(R.string.permissions_fragment_title));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device2, container, false);

        // TODO: Observe the mPermissionList
        mainViewModel.getPermissionsList().observe(this, new Observer<List<PermissionModel>>() {
            @Override
            public void onChanged(List<PermissionModel> permissionModels) {
                // TODO
            }
        });

        return view;
    }

}
