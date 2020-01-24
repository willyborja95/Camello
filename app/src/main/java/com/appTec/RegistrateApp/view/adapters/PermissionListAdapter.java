package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PermissionListAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater;
    Context context;
    ArrayList<Permission> lstPermission;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public PermissionListAdapter(Context context, ArrayList<Permission> lstPermission ){
        this.context = context;
        this.lstPermission = lstPermission;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.permission_element, null);
        TextView txtPermissionType = (TextView) view.findViewById(R.id.txtPermissionType);
        TextView txtPermissionStatus = (TextView) view.findViewById(R.id.txtPermissionStatus);
        TextView txtPermissionStartDate = (TextView) view.findViewById(R.id.txtStartDate);
        TextView txtPermissionStartTime = (TextView) view.findViewById(R.id.txtStartTime);
        TextView txtPermissionEndDate = (TextView) view.findViewById(R.id.txtEndDate);
        TextView txtPermissionEndTime = (TextView) view.findViewById(R.id.txtEndTime);
        String [] strStartDateTime = dateformat.format(lstPermission.get(position).getStartDate().getTime()).split(" ");
        String [] strEndDateTime = dateformat.format(lstPermission.get(position).getEndDate().getTime()).split(" ");
        txtPermissionType.setText(lstPermission.get(position).getPermissionType().toString());
        txtPermissionStatus.setText(lstPermission.get(position).getPermissionStatus().toString());
        txtPermissionStartDate.setText(strStartDateTime[0]);
        txtPermissionStartTime.setText(strStartDateTime[1]);
        txtPermissionEndDate.setText(strEndDateTime[0]);
        txtPermissionEndTime.setText(strEndDateTime[1]);
        return view;
    }

    @Override
    public int getCount() {
        return lstPermission.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
