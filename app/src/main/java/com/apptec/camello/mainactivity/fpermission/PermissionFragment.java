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
import com.apptec.camello.mainactivity.fpermission.ui.DialogPermission;
import com.apptec.camello.mainactivity.fpermission.ui.PermissionAdapter;

import java.util.List;

import timber.log.Timber;

public class PermissionFragment extends BaseFragment {
    /**
     * PermissionFragment
     */


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
        permissionAdapter = new PermissionAdapter(mainViewModel.getPermissionFullList());
        // Create a layout manager
        binding.recyclerViewPermissionsList.setLayoutManager(new LinearLayoutManager(getContext()));


        mainViewModel.getPermissionFullList().observe(getViewLifecycleOwner(), new Observer<List<PermissionFull>>() {
            @Override
            public void onChanged(List<PermissionFull> permissionFulls) {
                if (permissionFulls.size() > 0) {
                    // Change the result list now
                    binding.recyclerViewPermissionsList.setAdapter(permissionAdapter);

                }
            }
        });

        permissionViewModel.addNewPermission().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    /**
                     * Open the dialog fragment to add a permission
                     */
                    Timber.d("Open dialog");

                    DialogPermission dialogPermission = new DialogPermission();
                    dialogPermission.show(getFragmentManager(), PermissionFragment.class.getSimpleName());


                }
            }
        });


        return binding.getRoot();
    }

}
