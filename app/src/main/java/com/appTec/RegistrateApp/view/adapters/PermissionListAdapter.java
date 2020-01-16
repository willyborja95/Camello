package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PermissionListAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater;
    Context context;
    ArrayList<Permission> lstPermission;


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
        TextView txtPermissionEndDate = (TextView) view.findViewById(R.id.txtEndDate);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        System.out.println("=====================================");
        System.out.println("PRINTEANDO!!!!!!!!!!!!!!!!!!!!!!!");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        String date = inputFormat.format(new Date());
        System.out.println(date);


        txtPermissionType.setText(lstPermission.get(position).getPermissionType().toString());
        txtPermissionStatus.setText(lstPermission.get(position).getPermissionStatus().toString());
        txtPermissionStartDate.setText(inputFormat.format(lstPermission.get(position).getStartDate()).toString());
        txtPermissionEndDate.setText(inputFormat.format(lstPermission.get(position).getEndDate()).toString());

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
