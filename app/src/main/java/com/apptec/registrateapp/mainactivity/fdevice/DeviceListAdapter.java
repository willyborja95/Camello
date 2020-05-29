package com.apptec.registrateapp.mainactivity.fdevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.DeviceModel;

import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    Context context;
    LiveData<List<DeviceModel>> devices;

    public DeviceListAdapter(Context context, LiveData<List<DeviceModel>> devices) {
        this.context = context;
        this.devices = devices;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * This method will be used to attach the card view of device to the list view.
         * */
        convertView = LayoutInflater.from(context).inflate(R.layout.card_view_device, null);

        // Getting the object by the position
        DeviceModel device = getItem(position);

        // Binding UI elements (Maybe with could use data binding in the future)
        TextView txtDeviceName = convertView.findViewById(R.id.text_view_device_name);
        TextView txtDeviceModel = convertView.findViewById(R.id.text_view_device_model);
        TextView txtDeviceImei = convertView.findViewById(R.id.text_view_device_imei);
        TextView txtDeviceStatus = convertView.findViewById(R.id.text_view_device_status);


        txtDeviceName.setText(device.getName());
        txtDeviceModel.setText(device.getModel());
        txtDeviceImei.setText(device.getIdentifier());
        txtDeviceStatus.setText(device.isActive() ? "Activo" : "Inactivo");



        return convertView;
    }

    @Override
    public int getCount() {
        return devices.getValue().size();
    }

    @Override
    public DeviceModel getItem(int position) {
        return devices.getValue().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
