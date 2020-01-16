package com.appTec.RegistrateApp.view.activities.modals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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
    DatabaseAdapter databaseAdapter;
    String token;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String str);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this.getContext());
        //token = databaseAdapter.getToken();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_device, null);

        txtDeviceName = (EditText) viewDialog.findViewById(R.id.txtDeviceName);
        txtDeviceModel = (EditText) viewDialog.findViewById(R.id.txtDeviceModel);


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewDialog)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(txtDeviceName.getText().toString());

                        String deviceName = txtDeviceName.getText().toString();
                        String deviceDescription = txtDeviceModel.getText().toString();
                        String deviceImei = "XXXXXXX";


                        Device device = new Device(deviceName, deviceDescription, deviceImei);

                        DeviceRetrofitInterface deviceRetrofitInterface = ApiClient.getClient().create(DeviceRetrofitInterface.class);

                        Call<JsonObject> call = deviceRetrofitInterface.post(token, device);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                System.out.println("RESPUESTA DEL SERVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println(response.toString());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });






                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogDevice.this.getDialog().cancel();
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
