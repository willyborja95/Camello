package com.apptec.camello.mainactivity.fnotification.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.apptec.camello.R;
import com.apptec.camello.databinding.NotificationItemBinding;
import com.apptec.camello.models.NotificationModel;

import java.util.List;

import timber.log.Timber;

/**
 * NotificationListAdapter
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    // Attribute
    LiveData<List<NotificationModel>> notificationList;

    // Fragment manager for showing the dialog for each notification
    FragmentManager childFragmentManager;


    public NotificationAdapter(LiveData<List<NotificationModel>> notificationList, FragmentManager childFragmentManager) {
        this.notificationList = notificationList;
        this.childFragmentManager = childFragmentManager;
    }

    /**
     * Called when RecyclerView needs a new {@link MyViewHolder} of the given type to represent
     * an item.
     * Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Using data binding
        NotificationItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.notification_item, parent, false);


        // Return the view holder with the elements attached
        return new MyViewHolder(binding);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MyViewHolder#itemView} to reflect the item at the given
     * position.
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your data set at this position
        NotificationModel notificationModel = getItem(position);

        // - replace the contents of the view with that element
        holder.bind(notificationModel, childFragmentManager);
    }

    /**
     * THe return the value in the list from the given position
     *
     * @param position target position
     * @return
     */
    private NotificationModel getItem(int position) {
        return notificationList.getValue().get(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return notificationList.getValue().size();
    }

    /**
     * Return the stable ID for the item at <code>position</code>.
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    @Override
    public long getItemId(int position) {
        return notificationList.getValue().indexOf(position);
    }

    /**
     * View holder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // Use data binding
        NotificationItemBinding itemBinding;


        public MyViewHolder(@NonNull NotificationItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        /**
         * Bind this data with the layout
         *
         * @param notificationModel
         * @param childFragmentManager
         */
        public void bind(NotificationModel notificationModel, FragmentManager childFragmentManager) {

            Timber.d("Notification to be placed: %s", notificationModel.toString());

            itemBinding.setNotification(notificationModel);


            itemBinding.notificationCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Show a dialog with extended information about the dialog
                    DialogNotification dialogNotification = new DialogNotification().setNotification(notificationModel);
                    dialogNotification.show(childFragmentManager, DialogNotification.class.getSimpleName());
                }
            });


        }
    }
}