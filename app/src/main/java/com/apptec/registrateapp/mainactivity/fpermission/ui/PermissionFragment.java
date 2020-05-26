package com.apptec.registrateapp.mainactivity.fpermission.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.databinding.FragmentPermissionBinding;
import com.apptec.registrateapp.mainactivity.MainViewModel;
import com.apptec.registrateapp.mainactivity.fpermission.PermissionViewModel;
import com.apptec.registrateapp.models.PermissionModel;

import java.util.List;

import timber.log.Timber;

public class PermissionFragment extends Fragment {
    /**
     * PermissionFragment
     */
    private static final String TAG = "PermissionFragment";


    // Instance of ViewModel
    private MainViewModel mainViewModel;
    private PermissionViewModel permissionViewModel;

    FragmentPermissionBinding binding;

    // Ui elements


    PermissionListAdapter permissionListAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);                    // Getting the main view model
        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);        // Getting the view model exclusive of this fragment

        mainViewModel.setActiveFragmentName(getString(R.string.permissions_fragment_title));

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_permission, container, false);
        binding.setPermissionViewModel(permissionViewModel);


        // TODO: Observe the mPermissionList
        mainViewModel.getPermissionsList().observe(this, new Observer<List<PermissionModel>>() {
            @Override
            public void onChanged(List<PermissionModel> permissionModels) {
                // TODO
                if (permissionModels.size() == 0) {
                    Timber.d("No permissions found");

                }

            }
        });

        permissionViewModel.addNewPermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    /**
                     * Open the dialog fragment to add a permission
                     */
                    Timber.d("Open dialog");

                    DialogPermission dialogPermission = new DialogPermission();
                    dialogPermission.show(getFragmentManager(), TAG);


                }
            }
        });

        return binding.getRoot();
    }

}
