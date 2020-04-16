package com.appTec.RegistrateApp.view.activities.modals;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.fragment.app.DialogFragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.repository.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.DeviceRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogDevice extends DialogFragment {
    NoticeDialogListener listener;
    EditText txtDeviceName;
    EditText txtDeviceModel;
    ProgressDialog progressDialog;

    DatabaseAdapter databaseAdapter;
    TelephonyManager telephonyManager;

    public interface NoticeDialogListener {
        public void onDeviceSaved(Device device);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_device, null);

        txtDeviceName = (EditText) viewDialog.findViewById(R.id.txtDeviceName);
        txtDeviceModel = (EditText) viewDialog.findViewById(R.id.txtDeviceModel);

        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        builder.setView(viewDialog)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("deviceLog", "click en ok");

                        Device deviceDialog = new Device();
                        deviceDialog.setName(txtDeviceName.getText().toString());
                        deviceDialog.setModel(txtDeviceModel.getText().toString());
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
        Call<JsonObject> call = deviceRetrofitInterface.post(ApiClient.getToken(), deviceDialog);
        showDeviceProgressDialog(Constants.UPDATING_CHANGES);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("deviceLog", "hello from dialog device");
                Log.d("deviceLog", response.toString());
                hideDeviceProgressDialog();
                if(response.code()==200){
                    int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                    String deviceName = response.body().getAsJsonObject("data").get("nombre").getAsString();
                    String deviceModel = response.body().getAsJsonObject("data").get("modelo").getAsString();
                    String deviceImei = response.body().getAsJsonObject("data").get("imei").getAsString();
                    boolean deviceStatus = response.body().getAsJsonObject("data").get("estado").getAsBoolean();

                    Device device = new Device();
                    device.setId(id);
                    device.setName(deviceName);
                    device.setModel(deviceModel);
                    device.setImei(deviceImei);
                    device.setStatus(deviceStatus);
                    Log.d("deviceStatus", String.valueOf(device.getId()));
                    Log.d("deviceStatus", device.getImei());
                    Log.d("deviceStatus", device.getName());
                    Log.d("deviceStatus", device.getModel());
                    Log.d("deviceStatus", String.valueOf(device.isStatus()));
                    databaseAdapter.insertDevice(device);
                    listener.onDeviceSaved(device);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideDeviceProgressDialog();
                showConectionErrorMessage();
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




    //Dialogs
    public void showDeviceProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideDeviceProgressDialog() {
        progressDialog.dismiss();
    }

    public void showConectionErrorMessage() {
        showDialog("Error de conexión", "Al parecer no hay conexión a Internet.");
    }

    public void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
        alertDialog.show();
    }
}
