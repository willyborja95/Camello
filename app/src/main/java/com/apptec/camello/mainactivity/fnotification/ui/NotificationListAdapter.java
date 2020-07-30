package com.apptec.camello.mainactivity.fnotification.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.apptec.camello.R;
import com.apptec.camello.models.NotificationModel;

import java.util.List;

import timber.log.Timber;

/**
 * NotificationListAdapter
 */
public class NotificationListAdapter extends BaseAdapter {

    // Attributes
    Context context;
    LiveData<List<NotificationModel>> notifications;

    /**
     * Constructor
     */
    public NotificationListAdapter(Context context, LiveData<List<NotificationModel>> notifications) {

        this.context = context;
        this.notifications = notifications;
    }

    /**
     * This method will be used to attach the card view of notification to the list view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater
                .from(context)
                .inflate(R.layout.notification_item, null);

        // Getting the object by the position
        NotificationModel notification = getItem(position);
        Timber.d("Notification ot be placed: " + notification.toString());
        // Binding UI elements
        TextView title = convertView.findViewById(R.id.notification_title);
        TextView sentDate = convertView.findViewById(R.id.notification_sent_date);

        title.setText(notification.getTitle());
        sentDate.setText(notification.getReadableSentDate());


        return convertView;
    }


    @Override
    public int getCount() {
        return notifications.getValue().size();
    }

    @Override
    public NotificationModel getItem(int position) {
        return notifications.getValue().get(position);
    }

    @Override
    public long getItemId(int position) {
        return notifications.getValue().indexOf(position);
    }


}