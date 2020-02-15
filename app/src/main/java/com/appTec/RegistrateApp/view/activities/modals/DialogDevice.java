package com.appTec.RegistrateApp.view.activities.modals;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.Manifest.permission;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.DeviceRetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDevice extends DialogFragment {
    NoticeDialogListener listener;
    EditText txtDeviceName;
    EditText txtDeviceModel;
    String token;
    SharedPreferences pref;
    DatabaseAdapter databaseAdapter;
    TelephonyManager telephonyManager;

    public interface NoticeDialogListener {
        public void onDeviceSaved(Device device);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //token = databaseAdapter.getToken();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_device, null);

        txtDeviceName = (EditText) viewDialog.findViewById(R.id.txtDeviceName);
        txtDeviceModel = (EditText) viewDialog.findViewById(R.id.txtDeviceModel);

        pref = getContext().getSharedPreferences("RegistrateApp", 0);
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(getContext());
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        builder.setView(viewDialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Device deviceDialog = new Device(txtDeviceName.getText().toString(), txtDeviceModel.getText().toString());
                        saveDevice(deviceDialog);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogDevice.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void saveDevice(Device deviceDialog) {
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
        deviceDialog.setImei(deviceImei);
        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);
        Call<JsonObject> call = deviceRetrofitInterface.post(pref.getString("token", null), deviceDialog);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println(response.body());
                if(response.code()==200){
                    int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                    String deviceName = response.body().getAsJsonObject("data").get("nombre").getAsString();
                    String deviceModel = response.body().getAsJsonObject("data").get("modelo").getAsString();
                    String deviceImei = response.body().getAsJsonObject("data").get("imei").getAsString();
                    boolean deviceStatus = response.body().getAsJsonObject("data").get("estado").getAsBoolean();

                    Device device = new Device(id, deviceName, deviceModel, deviceImei, deviceStatus);
                    databaseAdapter.insertDevice(device);
                    listener.onDeviceSaved(device);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
