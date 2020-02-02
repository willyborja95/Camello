package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.device;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.adapters.DeviceListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.getSystemService;


public class DeviceFragment extends Fragment {

    //UI elements
    private FloatingActionButton fabAddDevice;
    private EditText txtDeviceName;
    private EditText txtDeviceModel;
    private ListView lvDevices;

    //Business logic elements
    private ArrayList<Device> lstDevice = new ArrayList<Device>();
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;
    TelephonyManager telephonyManager;


    public void addDeviceToList(Device device) {
        lstDevice.clear();
        lstDevice.add(device);
        updateListView();
        if (lstDevice.size() > 0) {
            fabAddDevice.hide();
        } else {
            fabAddDevice.show();
        }
    }

    private void updateListView() {
        lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View fragmentDeviceView = inflater.inflate(R.layout.fragment_device, container, false);
        fabAddDevice = (FloatingActionButton) fragmentDeviceView.findViewById(R.id.fabAgregarDispositivo);
        txtDeviceName = fragmentDeviceView.findViewById(R.id.txtDeviceName);
        txtDeviceModel = fragmentDeviceView.findViewById(R.id.txtDeviceModel);
        lvDevices = (ListView) fragmentDeviceView.findViewById(R.id.lvEquipos);
        lvDevices.setAdapter(new DeviceListAdapter(getContext(), lstDevice));
        pref = getContext().getSharedPreferences("RegistrateApp", 0);
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(getContext());
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        fabAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDevice df = new DialogDevice();
                df.show(getFragmentManager(),"DialogDevice");
            }
        });
        return fragmentDeviceView;
    }

    public void saveDevice(Device device) {
        String deviceImei = null;
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    deviceImei = telephonyManager.getDeviceId();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceImei = telephonyManager.getImei();
                }
            }
        }
        device.setImei(deviceImei);
        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.post(pref.getString("token", null), device);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                String deviceName = response.body().getAsJsonObject("data").get("nombre").getAsString();
                String deviceModel = response.body().getAsJsonObject("data").get("modelo").getAsString();
                String deviceImei = response.body().getAsJsonObject("data").get("imei").getAsString();
                boolean deviceStatus = response.body().getAsJsonObject("data").get("estado").getAsBoolean();
                Device device = new Device(id, deviceName, deviceModel, deviceImei, deviceStatus);
                databaseAdapter.insertDevice(device);
                addDeviceToList(device);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}
