package com.apptec.camello.mainactivity.fpermission.ui;


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

    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtComment;


    private DatePicker dpStartDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.PATTERN_DATE_FORMAT);
    final Calendar startDate = Calendar.getInstance();
    final Calendar endDate = Calendar.getInstance();
    String strStartDate;
    String strEndDate;

    MainViewModel mainViewModel;


    /**
     * This method is called for create the dialog.
     * When the method show is called.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);              // Getting the view model


        View viewDialog = inflater.inflate(R.layout.dialog_permission, null);
        txtStartDate = (EditText) viewDialog.findViewById(R.id.txtStartDate);
        txtEndDate = (EditText) viewDialog.findViewById(R.id.txtEndDate);
        spnPermissionType = (Spinner) viewDialog.findViewById(R.id.spnPermissionType);
        txtComment = viewDialog.findViewById(R.id.dialog_permission_comment);

        RoomHelper.getAppDatabaseInstance().permissionTypeDao().getPermissionTypes().observe(this, new Observer<List<PermissionType>>() {
            @Override
            public void onChanged(List<PermissionType> permissionTypeList) {

                adapterPermissionType = new ArrayAdapter<PermissionType>(viewDialog.getContext(), R.layout.permission_type_element, R.id.textView3, permissionTypeList);

                spnPermissionType.setAdapter(adapterPermissionType);
            }
        });

        setUpTimesPicker();

        builder.setView(viewDialog)              // Add action buttons
                .setPositiveButton(getString(R.string.permission_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Save the permission requested
                        Timber.d("onClick: Ok");
                        PermissionType permissionType = (PermissionType) spnPermissionType.getSelectedItem();
                        if (isValidPermission()) {
                            Timber.d("Valid data, saving permission");
                            mainViewModel.savePermission(permissionType, startDate, endDate, txtComment.getText().toString());
                        } else {
                            Timber.w("Invalid data, do not save permission");
                            txtStartDate.setError("Inválido");
                            txtEndDate.setError("Inválido");
                        }

                    }
                })
                .setNegativeButton(getString(R.string.permission_negative_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialogPermission.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }

    /**
     * Method that validates the permission entered
     *
     * @return true when the start date is before end date
     */
    private boolean isValidPermission() {
        Timber.d("Validating data");
        if (txtStartDate.getText().length() < 1 || txtEndDate.getText().length() < 1) {
            return false;
        }
        return startDate.before(endDate);
    }


    /**
     * Set up the method to show a Time Picker dialog when the startDate and the endDate are clicked
     */
    private void setUpTimesPicker() {

        txtStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(new CustomTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(int selectedHour, int selectedMinute) {
                        Timber.d("onTimeSet");
                        Timber.d("Selected hour: " + selectedHour + " selected minute: " + selectedMinute);

                    }
                });



                DatePickerDialog dpStartDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        startDate.set(year, monthOfYear, dayOfMonth);
                        strStartDate = dateFormat.format(startDate.getTime());
                        txtStartDate.setText(strStartDate);
                        timePickerDialog.show(getChildFragmentManager(), "Tag");

                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

                dpStartDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        System.out.println(startDate.getTime());
                        startDate.add(Calendar.MONTH, 1);
                        strStartDate = dateFormat.format(startDate.getTime());
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
                        strEndDate = dateFormat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                        endDate.add(Calendar.MONTH, -1);
                    }
                }, endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE), false);//Yes 24 hour time
                tpEndDate.hide();


                final DatePickerDialog dpEndDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        endDate.set(year, monthOfYear, dayOfMonth);
                        strEndDate = dateFormat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                        tpEndDate.show();
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

                dpEndDate.setOnCancelListener(new DatePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        endDate.add(Calendar.MONTH, 1);
                        strEndDate = dateFormat.format(endDate.getTime());
                        txtEndDate.setText(strEndDate);
                    }
                });
                dpEndDate.show();

            }
        });
    }
}
