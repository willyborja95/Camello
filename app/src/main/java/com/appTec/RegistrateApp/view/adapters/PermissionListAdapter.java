package com.appTec.RegistrateApp.view.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.AssistanceRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionListAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater;
    Context context;
    ArrayList<Permission> lstPermission;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SharedPreferences pref;

    public PermissionListAdapter(Context context, ArrayList<Permission> lstPermission) {
        this.context = context;
        this.lstPermission = lstPermission;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.permission_element, null);
        pref = convertView.getContext().getSharedPreferences("RegistrateApp", 0);
        ImageButton imgBtnDeletePermission = (ImageButton) convertView.findViewById(R.id.btnDeletePermission);
        imgBtnDeletePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
                Call<JsonObject> permissionCall = permissionRetrofitInterface.delete(pref.getString("token", null), lstPermission.get(position).getId());
                permissionCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 200) {
                            System.out.println(pref.getString("token", null));
                            System.out.println(response);
                            lstPermission.remove(position);
                            notifyDataSetChanged();
                        }else{
                            InformationDialog.createDialog(context);
                            InformationDialog.setTitle("Error");
                            InformationDialog.setMessage("No ha sido posible eliminar el permiso.");
                            InformationDialog.showDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        showConectionErrorMessage(v.getContext());
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

    public void showConectionErrorMessage(Context context) {
        InformationDialog.createDialog(context);
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
    }

}