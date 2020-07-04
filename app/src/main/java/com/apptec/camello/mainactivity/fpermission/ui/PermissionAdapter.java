package com.apptec.camello.mainactivity.fpermission.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.apptec.camello.R;
import com.apptec.camello.databinding.PermissionItemBinding;
import com.apptec.camello.mainactivity.fpermission.PermissionFull;

import java.util.List;

public class PermissionAdapter extends
        RecyclerView.Adapter<PermissionAdapter.MyViewHolder> {
    /**
     * This is the adapter for the recycler view list of permission
     * We use live data and data binding
     */


    // Attribute
    LiveData<List<PermissionFull>> permissionFullListLiveData;

    public PermissionAdapter(LiveData<List<PermissionFull>> permissionFullListLiveData) {
        /**
         * Constructor
         */
        this.permissionFullListLiveData = permissionFullListLiveData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        PermissionItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.permission_item, parent, false);


        // Return the view holder with the elements attached
        MyViewHolder viewHolder = new MyViewHolder(binding);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your data set at this position
        PermissionFull permissionFull = getItem(position);

        // - replace the contents of the view with that element
        holder.bind(permissionFull);

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

        public MyViewHolder(@NonNull PermissionItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }


        public void bind(PermissionFull permissionFull) {
            itemBinding.setPermissionFull(permissionFull);
        }

    }

}
