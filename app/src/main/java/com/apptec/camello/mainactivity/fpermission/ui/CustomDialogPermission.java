package com.apptec.camello.mainactivity.fpermission.ui;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.apptec.camello.R;
import com.apptec.camello.mainactivity.MainViewModel;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.localdatabase.RoomHelper;
import com.apptec.camello.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

/**
 * This dialog is for let the user create a new permission
 */
public class CustomDialogPermission extends DialogFragment {


    // UI elements
    private Spinner spnPermissionType;
    private ArrayAdapter<PermissionType> adapterPermissionType;

    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private EditText editTextComment;


    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
    final Calendar calendarStartDate = Calendar.getInstance();
    final Calendar calendarEndDate = Calendar.getInstance();
    String startDateRepresentation;
    String endDateRepresentation;

    MainViewModel mainViewModel;


    /**
     * This method is called for create the dialog.
     * When the method show is called.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);              // Getting the view model


        View viewDialog = inflater.inflate(R.layout.dialog_permission, null);
        editTextStartDate = viewDialog.findViewById(R.id.txtStartDate);
        editTextEndDate = viewDialog.findViewById(R.id.txtEndDate);
        spnPermissionType = viewDialog.findViewById(R.id.spnPermissionType);
        editTextComment = viewDialog.findViewById(R.id.dialog_permission_comment);

        RoomHelper.getAppDatabaseInstance().permissionTypeDao().getPermissionTypes().observe(this, new Observer<List<PermissionType>>() {
            @Override
            public void onChanged(List<PermissionType> permissionTypeList) {

                adapterPermissionType = new ArrayAdapter<PermissionType>(viewDialog.getContext(), R.layout.permission_type_element, R.id.textView3, permissionTypeList);

                spnPermissionType.setAdapter(adapterPermissionType);
            }
        });

        setUpTimesPicker();

        builder.setView(viewDialog)              // Add action buttons
                .setPositiveButton(R.string.permission_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Save the permission requested
                        Timber.d("onClick: Ok");
                        PermissionType permissionType = (PermissionType) spnPermissionType.getSelectedItem();

                        Timber.d("Valid data, saving permission");
                        mainViewModel.savePermission(permissionType, calendarStartDate, calendarEndDate, editTextComment.getText().toString());


                    }
                })
                .setNegativeButton(R.string.permission_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialogPermission.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }


    /**
     * Set up the method to show a Time Picker dialog when the startDate and the endDate are clicked
     */
    private void setUpTimesPicker() {

        editTextStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CustomTimePickerDialog tpStartDate;
                tpStartDate = new CustomTimePickerDialog(new CustomTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(int selectedHour, int selectedMinute) {
                        calendarStartDate.set(calendarStartDate.get(Calendar.YEAR), calendarStartDate.get(Calendar.MONTH), calendarStartDate.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        startDateRepresentation = dateFormat.format(calendarStartDate.getTime());
                        editTextStartDate.setText(startDateRepresentation);
                        calendarStartDate.add(Calendar.MONTH, -1);
                    }
                });


                DatePickerDialog dpStartDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        calendarStartDate.set(year, monthOfYear, dayOfMonth);
                        startDateRepresentation = dateFormat.format(calendarStartDate.getTime());
                        editTextStartDate.setText(startDateRepresentation);
                        tpStartDate.show(getChildFragmentManager(), "TimePicker");

                    }
                }, calendarStartDate.get(Calendar.YEAR), calendarStartDate.get(Calendar.MONTH), calendarStartDate.get(Calendar.DAY_OF_MONTH));
                dpStartDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpStartDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.out.println(calendarStartDate.getTime());
                        calendarStartDate.add(Calendar.MONTH, 1);
                        startDateRepresentation = dateFormat.format(calendarStartDate.getTime());
                        editTextStartDate.setText(startDateRepresentation);
                    }
                });
                dpStartDate.show();

            }
        });


        editTextEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CustomTimePickerDialog tpEndDate;
                tpEndDate = new CustomTimePickerDialog(new CustomTimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(int selectedHour, int selectedMinute) {
                        calendarEndDate.set(calendarEndDate.get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH), calendarEndDate.get(Calendar.DAY_OF_MONTH), selectedHour, selectedMinute);
                        endDateRepresentation = dateFormat.format(calendarEndDate.getTime());
                        editTextEndDate.setText(endDateRepresentation);
                        calendarEndDate.add(Calendar.MONTH, -1);
                    }
                });


                final DatePickerDialog dpEndDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        calendarEndDate.set(year, monthOfYear, dayOfMonth);
                        endDateRepresentation = dateFormat.format(calendarEndDate.getTime());
                        editTextEndDate.setText(endDateRepresentation);
                        tpEndDate.show(getChildFragmentManager(), "TimePickerEndHour");
                    }
                }, calendarEndDate.get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH), calendarEndDate.get(Calendar.DAY_OF_MONTH));
                dpEndDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpEndDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        calendarEndDate.add(Calendar.MONTH, 1);
                        endDateRepresentation = dateFormat.format(calendarEndDate.getTime());
                        editTextEndDate.setText(endDateRepresentation);
                    }
                });
                dpEndDate.show();

            }
        });
    }
}
