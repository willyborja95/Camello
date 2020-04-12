package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Device> lstDevices;

    public DeviceListAdapter(Context context, ArrayList<Device> lstDevices ){
        this.context = context;
        this.lstDevices = lstDevices;
    }

    /*
   =======================================
   ACTIVITY LIFECYCLE METHODS
   =======================================
    */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.device_element, null);
        TextView txtDeviceName = (TextView) convertView.findViewById(R.id.txtDeviceName);
        TextView txtDeviceModel = (TextView) convertView.findViewById(R.id.txtDeviceModel);
        TextView txtDeviceImei = (TextView) convertView.findViewById(R.id.txtDeviceImei);
        TextView txtDeviceStatus = (TextView) convertView.findViewById(R.id.txtDeviceStatus);

        txtDeviceName.setText(lstDevices.get(position).getName());
        txtDeviceModel.setText(lstDevices.get(position).getModel());
        txtDeviceImei.setText(lstDevices.get(position).getImei());
        if(lstDevices.get(position).isStatus()){
            txtDeviceStatus.setText("Habilitado");
        }else{
            txtDeviceStatus.setText("Deshabilitado");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return lstDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return lstDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
