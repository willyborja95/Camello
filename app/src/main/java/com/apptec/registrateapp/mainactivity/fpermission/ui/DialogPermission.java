package com.apptec.registrateapp.mainactivity.fpermission.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DialogPermission extends DialogFragment {
    /**
     * This dialog is for let the user create a new permission
     */
    private static final String TAG = "DialogPermission";

    // UI elements
    private EditText txtStartDate;
    private EditText txtEndDate;
    private DatePicker dpStartDate;
    private Spinner spnPermissionType;
    private ArrayAdapter<PermissionType> adapterPermissionType;
    SimpleDateFormat dateformat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
    final Calendar startDate = Calendar.getInstance();
    final Calendar endDate = Calendar.getInstance();
    String strStartDate;
    String strEndDate;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /**
         * This method is called for create the dialog.
         * When the method show is called.
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View viewDialog = inflater.inflate(R.layout.dialog_permission, null);
        txtStartDate = (EditText) viewDialog.findViewById(R.id.txtStartDate);
        txtEndDate = (EditText) viewDialog.findViewById(R.id.txtEndDate);
        spnPermissionType = (Spinner) viewDialog.findViewById(R.id.spnPermissionType);

        RoomHelper.getAppDatabaseInstance().permissionTypeDao().getPermissionTypes().observe(this, new Observer<List<PermissionType>>() {
            @Override
            public void onChanged(List<PermissionType> permissionTypeList) {

                adapterPermissionType = new ArrayAdapter<PermissionType>(viewDialog.getContext(), R.layout.permission_type_element, R.id.textView3, permissionTypeList);

                spnPermissionType.setAdapter(adapterPermissionType);
            }
        });

        setUpTimesPicker();
        // Binding the UI elements
        builder.setView(viewDialog)              // Add action buttons
                .setPositiveButton(getString(R.string.permission_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        PermissionType permissionType = (PermissionType) spnPermissionType.getSelectedItem();
//                        PermissionModel permission = new PermissionModel(permissionType, startDate, endDate);
//                        listener.onPermissionSaved(permission);
                    }
                })
                .setNegativeButton(getString(R.string.permission_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//
//                        DialogPermission2.this.getDialog().cancel();
//                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }


    private void setUpTimesPicker() {
        /**
         * Set up the method to show a Time Picker dialog when the startDate and the endDate are clicked
         */


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
    }
}
