package com.apptec.camello.mainactivity.fpermission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apptec.camello.R;
import com.apptec.camello.databinding.FragmentPermissionBinding;
import com.apptec.camello.mainactivity.BaseFragment;
import com.apptec.camello.mainactivity.MainViewModel;
import com.apptec.camello.mainactivity.fpermission.ui.CustomDialogPermission;
import com.apptec.camello.mainactivity.fpermission.ui.PermissionAdapter;
import com.apptec.camello.models.PermissionModel;

import timber.log.Timber;

/**
 * PermissionFragment
 */
public class PermissionFragment extends BaseFragment {

    // Instance of ViewModel
    private MainViewModel mainViewModel;
    private PermissionViewModel permissionViewModel;

    // Using data binding
    FragmentPermissionBinding binding;

    // Ui elements
    PermissionAdapter permissionAdapter;


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

        // Create the adapter
        permissionAdapter = new PermissionAdapter(
                mainViewModel.getPermissionFullList(),
                permissionModel -> mainViewModel.deletePermission(permissionModel));
        // Create a layout manager
        binding.recyclerViewPermissionsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mainViewModel.getPermissionFullList().observe(getViewLifecycleOwner(), permissionFulls -> {
            if (permissionFulls.isEmpty()) {
                // Change the result list now
                // If is  not empty
                // If the list is empty
                binding.recyclerViewPermissionsList.setVisibility(View.GONE);
                binding.noPermissions.setVisibility(View.VISIBLE);


            } else {
                // If is  not empty
                Timber.d("List of permission is empty");
                binding.recyclerViewPermissionsList.setVisibility(View.VISIBLE);
                binding.noPermissions.setVisibility(View.GONE);
                binding.recyclerViewPermissionsList.setAdapter(permissionAdapter);
            }
        });

        //
        // Open the dialog fragment to add a permission
        //
        permissionViewModel.addNewPermission().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                    Timber.d("Open dialog");

                    CustomDialogPermission dialogPermission = new CustomDialogPermission();
                    dialogPermission.show(getFragmentManager(), PermissionFragment.class.getSimpleName());


                }
            }
        });


        return binding.getRoot();
    }


    public static interface DeleteButtonListener {

        void onDeleteClicked(PermissionModel permissionModel);

    }

}
