package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.assistance;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Permission;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.AssistanceRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.PermissionRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.appTec.RegistrateApp.view.activities.modals.DialogDevice;
import com.appTec.RegistrateApp.view.adapters.AssistanceListAdapter;
import com.appTec.RegistrateApp.view.adapters.DeviceListAdapter;
import com.appTec.RegistrateApp.view.adapters.PermissionListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AssistanceFragment extends Fragment {

    ArrayList<Assistance> lstAssistances;
    ListView lvAssistance;
    EditText txtAssistanceSelectedDate;
    TextView lblNotAssistanceFound;
    ImageButton imgBtnSearchAssistance;
    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    Calendar selectedDate = Calendar.getInstance();
    String strSelectedDate;
    SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assistance, container, false);
        pref = getContext().getSharedPreferences("RegistrateApp", 0);
        lstAssistances = new ArrayList<Assistance>();
        lvAssistance = (ListView) view.findViewById(R.id.lvAssistance);
        txtAssistanceSelectedDate = (EditText) view.findViewById(R.id.txtAssistanceSelectedDate);
        imgBtnSearchAssistance = (ImageButton) view.findViewById(R.id.imgBtnSearchAssistance);
        lblNotAssistanceFound = (TextView) view.findViewById(R.id.lblNotAssistanceFound);
        lblNotAssistanceFound.setVisibility(view.GONE);
        txtAssistanceSelectedDate.setOnClickListener(new View.OnClickListener() {
            Calendar selectedDate = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog dpDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        strSelectedDate = dateformat.format(selectedDate.getTime());
                        txtAssistanceSelectedDate.setText(strSelectedDate);
                    }
                }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                dpDate.show();
            }
        });

        imgBtnSearchAssistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clickeado!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                AssistanceRetrofitInterface assistanceRetrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
                Call<JsonObject> assistanceCall = assistanceRetrofitInterface.get(pref.getString("token", null), strSelectedDate);
                assistanceCall.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        lstAssistances.clear();
                        JsonArray assistanceListJson = response.body().getAsJsonArray("data");

                        if (assistanceListJson.size() == 0) {
                            lblNotAssistanceFound.setVisibility(view.VISIBLE);
                        }else{
                            lblNotAssistanceFound.setVisibility(view.GONE);
                            for (int i = 0; i < assistanceListJson.size(); i++) {
                                Calendar assistanceDate = Calendar.getInstance();
                                JsonObject assistanceJson = assistanceListJson.get(i).getAsJsonObject();
                                String strDateTime = assistanceJson.get("formateddate").getAsString();
                                String strDate = strDateTime.split(" ")[0];
                                String strTime = strDateTime.split(" ")[1];
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                try {
                                    assistanceDate.setTime(dateformat.parse(strDateTime));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String event = assistanceJson.get("evento").getAsString();
                                Assistance assistance = new Assistance(event, assistanceDate);
                                lstAssistances.add(assistance);
                            }
                            updateListView();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        showConectionErrorMessage();
                    }
                });
            }
        });
        return view;
    }

    private void updateListView() {
        lvAssistance.setAdapter(new AssistanceListAdapter(getContext(), lstAssistances));
    }

    public void showConectionErrorMessage() {
        InformationDialog.createDialog(getContext());
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
    }
}