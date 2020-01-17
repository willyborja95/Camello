package com.appTec.RegistrateApp.view.activities.modals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.DeviceRetrofitInterface;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogPermission extends DialogFragment {
    PermissionDialogListener listener;
    EditText txtStartDate;
    EditText txtEndDate;
    DatabaseAdapter databaseAdapter;
    private DatePicker dpStartDate;
    String token;


    public interface PermissionDialogListener {
        public void onPermissionSaved(Permission permission);
        public void onPermissionNotSaved(Permission permission);
    }

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtStartDate.setText(sdf.format(myCalendar.getTime()));
    }




    public Dialog onCreateDialog(Bundle savedInstanceState) {
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this.getContext());
        //token = databaseAdapter.getToken();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View viewDialog = inflater.inflate(R.layout.dialog_permission, null);

        txtStartDate = (EditText) viewDialog.findViewById(R.id.txtStartDate);
        txtEndDate = (EditText) viewDialog.findViewById(R.id.txtEndDate);

        dpStartDate = (DatePicker) viewDialog.findViewById(R.id.dpStartDate);

        dpStartDate.setVisibility(View.GONE);

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewDialog)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PermissionType permissionType1 = new PermissionType(1, "Intempestivo");
                        Permission p1 = new Permission(permissionType1, PermissionStatus.Aprobado, new Date(), new Date());
                        listener.onPermissionSaved(p1);
                        //listener.onDialogPositiveClick(txtDeviceName.getText().toString());

                        //String deviceName = txtDeviceName.getText().toString();
                        //String deviceDescription = txtDeviceModel.getText().toString();
                        //String deviceImei = "XXXXXXX";


                        //Device device = new Device(deviceName, deviceDescription, deviceImei);

                        /*

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

                         */

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        DialogPermission.this.getDialog().cancel();
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
            listener = (PermissionDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
