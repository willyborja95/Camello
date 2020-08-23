package com.apptec.camello.mainactivity.fpermission.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.apptec.camello.R;
import com.apptec.camello.databinding.PermissionItemBinding;
import com.apptec.camello.mainactivity.fpermission.PermissionFragment;
import com.apptec.camello.mainactivity.fpermission.PermissionFull;

import java.util.List;

/**
 * This is the adapter for the recycler view list of permission
 * We use live data and data binding
 */
public class PermissionAdapter extends
        RecyclerView.Adapter<PermissionAdapter.MyViewHolder> {

    // Attribute
    LiveData<List<PermissionFull>> permissionFullListLiveData;

    // Listener for erase permission button listener
    PermissionFragment.DeleteButtonListener listener;

    // Fragment manager for showing the dialog for each notification
    FragmentManager childFragmentManager;

    /**
     * Constructor
     *
     * @param permissionFullListLiveData LIst of permissions
     * @param listener                   delete button action
     * @param childFragmentManager       child fragment manager to open the confirmation dialog
     */
    public PermissionAdapter(
            LiveData<List<PermissionFull>> permissionFullListLiveData,
            PermissionFragment.DeleteButtonListener listener,
            FragmentManager childFragmentManager) {
        this.permissionFullListLiveData = permissionFullListLiveData;
        this.listener = listener;
        this.childFragmentManager = childFragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        PermissionItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.permission_item, parent, false);


        // Return the view holder with the elements attached
        MyViewHolder viewHolder = new MyViewHolder(binding, listener);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your data set at this position
        PermissionFull permissionFull = getItem(position);

        // - replace the contents of the view with that element
        holder.bind(permissionFull, this.childFragmentManager);

    }

    public PermissionFull getItem(int position) {
        return permissionFullListLiveData.getValue().get(position);
    }

    @Override
    public long getItemId(int position) {
        return permissionFullListLiveData.getValue().indexOf(position);
    }


    @Override
    public int getItemCount() {
        return permissionFullListLiveData.getValue().size();
    }

    /**
     * View holder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Using data binding
        PermissionItemBinding itemBinding;

        // Listener for erase permission button listener
        PermissionFragment.DeleteButtonListener listener;

        /**
         * Constructor
         *
         * @param itemBinding Binding class
         * @param listener    button listener action
         */
        public MyViewHolder(@NonNull PermissionItemBinding itemBinding, PermissionFragment.DeleteButtonListener listener) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            this.listener = listener;
        }


        /**
         * Method to bind the item with the information
         *
         * @param permissionFull       target information
         * @param childFragmentManager useful to open the confirmation dialog
         */
        public void bind(PermissionFull permissionFull, FragmentManager childFragmentManager) {
            // Set a click listener for the button to delete the permission
            itemBinding.btnDeletePermission.setOnClickListener(v -> {

                // Open a dialog to confirm the user action
                ConfirmActionDialog dialog = new ConfirmActionDialog(
                        R.string.delete_permission_title,
                        R.string.delte_permission_message,
                        new ConfirmActionDialog.ResponseListener() {
                            @Override
                            public void onAccepted() {
                                listener.onDeleteClicked(permissionFull.getPermissionModel());
                            }

                            @Override
                            public void onCanceled() {

                            }
                        });
                dialog.show(childFragmentManager, "ConfirmationDialog");

            });

            itemBinding.setPermissionFull(permissionFull);

        }

    }

}
