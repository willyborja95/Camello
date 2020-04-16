package com.appTec.RegistrateApp.view.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionListAdapter extends BaseAdapter {
    //UI elements
    ProgressDialog progressDialog;

    //Business logic elements
    Context context;
    ArrayList<Permission> lstPermission;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public PermissionListAdapter(Context context, ArrayList<Permission> lstPermission) {
        this.context = context;
        this.lstPermission = lstPermission;
        progressDialog = new ProgressDialog(context);
    }

    /*
    =======================================
    ADAPTER METHODS
    =======================================
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.permission_element, null);
        ImageButton imgBtnDeletePermission = (ImageButton) convertView.findViewById(R.id.btnDeletePermission);
        imgBtnDeletePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
                Call<JsonObject> permissionCall = permissionRetrofitInterface.delete(ApiClient.getToken(), lstPermission.get(position).getId());
                showPermissionProgressDialog(Constants.UPDATING_CHANGES);
                permissionCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        hidePermissionProgressDialog();
                        if (response.code() == 200) {
                            lstPermission.remove(position);
                            notifyDataSetChanged();
                        } else {
                            showDialog("Error", "No ha sido posible eliminar el permiso.");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        hidePermissionProgressDialog();
                        showConectionErrorMessage();
                    }
                });
            }
        });


        TextView txtPermissionType = (TextView) convertView.findViewById(R.id.txtPermissionType);
        TextView txtPermissionStatus = (TextView) convertView.findViewById(R.id.txtPermissionStatus);
        TextView txtPermissionStartDate = (TextView) convertView.findViewById(R.id.txtStartDate);
        TextView txtPermissionStartTime = (TextView) convertView.findViewById(R.id.txtStartTime);
        TextView txtPermissionEndDate = (TextView) convertView.findViewById(R.id.txtEndDate);
        TextView txtPermissionEndTime = (TextView) convertView.findViewById(R.id.txtEndTime);
        String[] strStartDateTime = dateformat.format(lstPermission.get(position).getStartDate().getTime()).split(" ");
        String[] strEndDateTime = dateformat.format(lstPermission.get(position).getEndDate().getTime()).split(" ");
        txtPermissionType.setText(lstPermission.get(position).getPermissionType().toString());
        txtPermissionStatus.setText(lstPermission.get(position).getPermissionStatus().toString());
        txtPermissionStartDate.setText(strStartDateTime[0]);
        txtPermissionStartTime.setText(strStartDateTime[1]);
        txtPermissionEndDate.setText(strEndDateTime[0]);
        txtPermissionEndTime.setText(strEndDateTime[1]);
        return convertView;
    }

    @Override
    public int getCount() {
        return lstPermission.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
    =======================================
    BUSINESS LOGIC METHODS
    =======================================
     */



    //Dialogs
    public void showPermissionProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void hidePermissionProgressDialog() {
        progressDialog.dismiss();
    }

    public void showConectionErrorMessage() {
        showDialog("Error de conexión", "Al parecer no hay conexión a Internet.");
    }

    public void showDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
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