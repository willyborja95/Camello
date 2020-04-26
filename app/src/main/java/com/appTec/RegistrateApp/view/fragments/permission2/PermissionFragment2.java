package com.appTec.RegistrateApp.view.fragments.permission2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.modals.DialogPermission;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PermissionFragment2 extends Fragment {

    //UI elements
    ProgressDialog progressDialog;
    private ListView lvPermission;
    private FloatingActionButton fabAddPermission;

    //Bussiness logic elements
    private ArrayList<PermissionType> lstPermissionType;
    private ArrayList<Permission> lstPermission = new ArrayList<Permission>();;
    private User user;

    public static PermissionFragment2 newInstance() {
        return new PermissionFragment2();
    }

    /*
    =======================================
    ACTIVITY LIFECYCLE METHODS
    =======================================
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");
        lstPermissionType = (ArrayList<PermissionType>) bundle.getSerializable("lstPermissionType");
        progressDialog = new ProgressDialog(context);
        updatePermissions();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permission2, container, false);
        lvPermission = (ListView) root.findViewById(R.id.lvPermission);
        fabAddPermission = (FloatingActionButton) root.findViewById(R.id.fabAddPermission);
        fabAddPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getContext(), DialogPermission.class);
                DialogPermission df = new DialogPermission();
                df.addArrayListPerissionType(lstPermissionType);
                df.show(getFragmentManager(), "");
            }
        });
        return root;
    }

    /*
    =======================================
    BUSINESS LOGIC METHODS
    =======================================
     */

    //Save new permission
    public void savePermission(Permission permission){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        permission.getStartDate().getTime().setMonth(permission.getStartDate().getTime().getMonth()-1);
        permission.getEndDate().getTime().setMonth(permission.getEndDate().getTime().getMonth()-1);
        String strStartDate = dateformat.format(permission.getStartDate().getTime());
        final String strEndDate = dateformat.format(permission.getEndDate().getTime());
        JsonObject jsonPermission = new JsonObject();
        jsonPermission.addProperty("fechainicio", strStartDate);
        jsonPermission.addProperty("fechafin", strEndDate);
        jsonPermission.addProperty("permisoid", permission.getPermissionType().getId());
        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Call<JsonObject> permissionCall = permissionRetrofitInterface.post(ApiClient.getAccessToken(), jsonPermission);
        showPermissionProgressDialog(Constants.UPDATING_CHANGES);
        permissionCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hidePermissionProgressDialog();
                int id = response.body().getAsJsonObject("data").get("id").getAsInt();
                String strStartDate = response.body().getAsJsonObject("data").get("fechainicio").getAsString();
                String strEndDate = response.body().getAsJsonObject("data").get("fechafin").getAsString();
                int idPermission = Integer.parseInt(response.body().getAsJsonObject("data").get("permisoid").getAsString());
                String strPermissionStatus = response.body().getAsJsonObject("data").get("estado").getAsString();
                String[] arrayStartDateTime = strStartDate.split(" ");
                String[] arrayStartTime = arrayStartDateTime[1].split(":");
                String[] arrayStartDate = arrayStartDateTime[0].split("/");
                String[] arrayEndDateTime = strEndDate.split(" ");
                String[] arrayEndTime = arrayEndDateTime[1].split(":");
                String[] arrayEndDate = arrayEndDateTime[0].split("/");
                Calendar calendarStartDate = Calendar.getInstance();
                calendarStartDate.set(Integer.parseInt(arrayStartDate[2]), Integer.parseInt(arrayStartDate[1]), Integer.parseInt(arrayStartDate[0]), Integer.parseInt(arrayStartTime[0]), Integer.parseInt(arrayStartTime[1]));
                Calendar calendarEndDate = Calendar.getInstance();
                calendarEndDate.set(Integer.parseInt(arrayEndDate[2]), Integer.parseInt(arrayEndDate[1]), Integer.parseInt(arrayEndDate[0]), Integer.parseInt(arrayEndTime[0]), Integer.parseInt(arrayEndTime[1]));
                PermissionType permission = null;
                PermissionStatus permissionStatus = null;

                for (int i = 0; i < lstPermissionType.size(); i++) {
                    if (idPermission == lstPermissionType.get(i).getId()) {
                        permission = lstPermissionType.get(i);
                    }
                }

                if (strPermissionStatus.equals("enrevision")) {
                    permissionStatus = PermissionStatus.Revisando;
                } else if (strPermissionStatus.equals("aprobado")) {
                    permissionStatus = PermissionStatus.Aprobado;
                } else if (strPermissionStatus.equals("rechazado")) {
                    permissionStatus = PermissionStatus.Rechazado;
                }
                Permission permission1 = new Permission(id, permission, permissionStatus, calendarStartDate, calendarEndDate);
                addPermissionList(permission1);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hidePermissionProgressDialog();
                showConectionErrorMessage();
            }
        });
    }

    public void addPermissionList(Permission permission){
        lstPermission.add(permission);
        updateListView();
    }

    private void updateListView(){
        lvPermission.setAdapter(new PermissionListAdapter(getContext(), lstPermission));
    }

    public void updatePermissions(){
        lstPermission.clear();
        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Call<JsonObject> permissionCall = permissionRetrofitInterface.get(ApiClient.getAccessToken(), user.getId());
        showPermissionProgressDialog(Constants.UPDATING_CHANGES);
        permissionCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hidePermissionProgressDialog();
                JsonArray permissionListJson = response.body().getAsJsonArray("data");
                lstPermission.clear();
                for (int i = 0; i < permissionListJson.size(); i++) {
                    JsonObject permissionJson = permissionListJson.get(i).getAsJsonObject();
                    int id = permissionJson.get("id").getAsInt();
                    String strStartDate = permissionJson.get("fechainicio").getAsString();
                    String strEndDate = permissionJson.get("fechafin").getAsString();
                    String strPermissionType = permissionJson.getAsJsonObject("permiso").get("nombre").getAsString();
                    String strPermissionStatus = permissionJson.get("estado").getAsString();
                    String[] arrayStartDateTime = strStartDate.split(" ");
                    String[] arrayStartTime = arrayStartDateTime[1].split(":");
                    String[] arrayStartDate = arrayStartDateTime[0].split("/");
                    String[] arrayEndDateTime = strEndDate.split(" ");
                    String[] arrayEndTime = arrayEndDateTime[1].split(":");
                    String[] arrayEndDate = arrayEndDateTime[0].split("/");
                    Calendar calendarStartDate = Calendar.getInstance();
                    calendarStartDate.set(Integer.parseInt(arrayStartDate[2]), Integer.parseInt(arrayStartDate[1]), Integer.parseInt(arrayStartDate[0]), Integer.parseInt(arrayStartTime[0]), Integer.parseInt(arrayStartTime[1]));
                    Calendar calendarEndDate = Calendar.getInstance();
                    calendarEndDate.set(Integer.parseInt(arrayEndDate[2]), Integer.parseInt(arrayEndDate[1]), Integer.parseInt(arrayEndDate[0]), Integer.parseInt(arrayEndTime[0]), Integer.parseInt(arrayEndTime[1]));
                    PermissionType permissionType = null;
                    PermissionStatus permissionStatus = null;

                    for (int j = 0; j < lstPermissionType.size(); j++) {
                        if (strPermissionType.equals(lstPermissionType.get(j).getNombe())) {
                            permissionType = lstPermissionType.get(j);
                        }
                    }

                    if (strPermissionStatus.equals("enrevision")) {
                        permissionStatus = PermissionStatus.Revisando;
                    } else if (strPermissionStatus.equals("aprobado")) {
                        permissionStatus = PermissionStatus.Aprobado;
                    } else if (strPermissionStatus.equals("rechazado")) {
                        permissionStatus = PermissionStatus.Rechazado;
                    }
                    Permission permission = new Permission(id, permissionType, permissionStatus, calendarStartDate, calendarEndDate);
                    lstPermission.add(permission);
                }
                updateListView();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hidePermissionProgressDialog();
                showConectionErrorMessage();
            }
        });
    }



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