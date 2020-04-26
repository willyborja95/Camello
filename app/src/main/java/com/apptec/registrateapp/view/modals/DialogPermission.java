package com.apptec.registrateapp.view.modals;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.Permission;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.localDatabase.DatabaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DialogPermission extends DialogFragment {

    //UI elements
    private EditText txtStartDate;
    private EditText txtEndDate;
    private DatePicker dpStartDate;
    private Spinner spnPermissionType;
    private ArrayAdapter<PermissionType> adapterPermissionType;


    PermissionDialogListener listener;
    ArrayList<PermissionType> lstPermissionType;

    DatabaseAdapter databaseAdapter;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    final Calendar startDate = Calendar.getInstance();
    final Calendar endDate = Calendar.getInstance();
    String strStartDate;
    String strEndDate;

    public interface PermissionDialogListener {
        public void onPermissionSaved(Permission permission);
    }


    public void addArrayListPerissionType(ArrayList<PermissionType> lstPermissionType) {
        this.lstPermissionType = lstPermissionType;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_permission, null);
        txtStartDate = (EditText) viewDialog.findViewById(R.id.txtStartDate);
        txtEndDate = (EditText) viewDialog.findViewById(R.id.txtEndDate);
        txtStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final TimePickerDialog tpStartDate;
                tpStartDate = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        strStartDate = dateformat.format(startDate.getTime());
                        txtStartDate.setText(strStartDate);
                        startDate.add(Calendar.MONTH, -1);

                    }
                }, startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE), true);//Yes 24 hour time
                tpStartDate.hide();


                DatePickerDialog dpStartDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        startDate.set(year, monthOfYear, dayOfMonth);
                        strStartDate = dateformat.format(startDate.getTime());
                        txtStartDate.setText(strStartDate);
                        tpStartDate.show();
                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

                dpStartDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.out.println(startDate.getTime());
                        startDate.add(Calendar.MONTH, 1);
                        strStartDate = dateformat.format(startDate.getTime());
                        txtStartDate.setText(strStartDate);
                    }
                });
                dpStartDate.show();

            }
        });


        txtEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final TimePickerDialog tpEndDate;
                tpEndDate = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        strEndDate = dateformat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                        endDate.add(Calendar.MONTH, -1);
                    }
                }, endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE), true);//Yes 24 hour time
                tpEndDate.hide();


                final DatePickerDialog dpEndDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        endDate.set(year, monthOfYear, dayOfMonth);
                        strEndDate = dateformat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                        tpEndDate.show();
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

                dpEndDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        endDate.add(Calendar.MONTH, 1);
                        strEndDate = dateformat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                    }
                });
                dpEndDate.show();

            }
        });


        spnPermissionType = (Spinner) viewDialog.findViewById(R.id.spnPermissionType);

        adapterPermissionType = new ArrayAdapter<PermissionType>(viewDialog.getContext(), R.layout.permission_type_element, R.id.textView3, lstPermissionType);


        spnPermissionType.setAdapter(adapterPermissionType);

        spnPermissionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Se ha seleccionado la opcion: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewDialog)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PermissionType permissionType = (PermissionType)spnPermissionType.getSelectedItem();
                        Permission permission = new Permission(permissionType, startDate, endDate);
                        listener.onPermissionSaved(permission);
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

    @Override
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