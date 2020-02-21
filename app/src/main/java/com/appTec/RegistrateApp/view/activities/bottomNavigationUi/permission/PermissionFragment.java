package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.permission;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.models.PermissionStatus;
import com.appTec.RegistrateApp.models.PermissionType;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.appTec.RegistrateApp.view.activities.modals.DialogPermission;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PermissionFragment extends Fragment {

    private FloatingActionButton fabAddPermission;
    private ArrayList<PermissionType> lstPermissionType;
    private ArrayList<Permission> lstPermission = new ArrayList<Permission>();;
    private ListView lvPermission;
    private SharedPreferences pref;
    private User user;


    public static PermissionFragment newInstance() {
        return new PermissionFragment();
    }

    public void addArrayListPermissionType(ArrayList<PermissionType> lstPermissionType){
        this.lstPermissionType = lstPermissionType;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("hello","Hello from onCreate (1)! ");
        Bundle bundle = this.getArguments();
        user = (User) bundle.getSerializable("user");
        Log.d("hello","Hello from onCreate (2)! ");

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        updatePermissions();
        Log.d("hello","Hello from onCreateView!");




        View root = inflater.inflate(R.layout.fragment_permission, container, false);


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

    public void addPermissionToList(Permission permission){
        lstPermission.add(permission);
        updateListView();
    }

    public void addPermissionList(ArrayList<Permission> lstPermission){
        this.lstPermission = lstPermission;
        updateListView();
    }

    private void updateListView(){
        lvPermission.setAdapter(new PermissionListAdapter(getContext(), lstPermission));
    }

    public void updatePermissions(){
        SharedPreferences pref = getActivity().getSharedPreferences("RegistrateApp", 0);

        lstPermission.clear();
        PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
        Call<JsonObject> permissionCall = permissionRetrofitInterface.get(pref.getString("token", null), user.getId());
        permissionCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
                showConectionErrorMessage();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void showConectionErrorMessage() {
        InformationDialog.createDialog(getContext());
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
    }
}