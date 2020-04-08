package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Notification;

import java.util.ArrayList;

public class NotificationsListAdapter extends BaseAdapter {

    // Attributes
    Context context;
    ArrayList<Notification> notifications;


    public NotificationsListAdapter(Context context, ArrayList<Notification> notifications ){
        /*
        * Constructor
        * */
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
         * This method will be used to attach the card view of notification to the list view.
         * */
        convertView = LayoutInflater.from(context).inflate(R.layout.notifications_card_view, null);

        // Getting the object by the position
        Notification notification = getItem(position);

        // Binding UI elements
        TextView title = convertView.findViewById(R.id.notification_title);
        TextView sentDate = convertView.findViewById(R.id.notification_sent_date);

        title.setText(notification.getTitle());
        sentDate.setText(notification.getSentDate().toString());

        return convertView;
    }


    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notifications.indexOf(position);
    }



}