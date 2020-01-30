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
    private static LayoutInflater layoutInflater;
    Context context;
    ArrayList<Device> lstDevices;

    public DeviceListAdapter(Context context, ArrayList<Device> lstDevices ){
        this.context = context;
        this.lstDevices = lstDevices;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.device_element, null);
        TextView txtDeviceName = (TextView) view.findViewById(R.id.txtDeviceName);
        TextView txtDeviceModel = (TextView) view.findViewById(R.id.txtDeviceModel);
        TextView txtDeviceImei = (TextView) view.findViewById(R.id.txtDeviceImei);
        txtDeviceName.setText(lstDevices.get(position).getNombre());
        txtDeviceModel.setText(lstDevices.get(position).getModelo());
        txtDeviceImei.setText(lstDevices.get(position).getImei());

        return view;
    }

    @Override
    public int getCount() {
        return lstDevices.size();
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
