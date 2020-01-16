package com.appTec.RegistrateApp.view;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.models.WorkingPeriod;
import com.appTec.RegistrateApp.services.localDatabase.DatabaseAdapter;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.LoginRetrofitInterface;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements  View.OnClickListener {

    //UI elements
    private ProgressBar progressBar;
    private ImageButton btnLogin;
    private EditText txtEmail;
    private EditText txtPassword;

    //App elements
    private String email;
    private String password;
    DatabaseAdapter databaseAdapter ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (ImageButton) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(this);
        hideProgress();
        databaseAdapter = DatabaseAdapter.getDatabaseAdapterInstance(this);
        System.out.println("(((((((((((((((((((((((((((((((( INICIANDO ))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
        System.out.println("CONSULTA BASE: "+databaseAdapter.getLoginStatus());
        System.out.println("(((((((((((((((((((((((((((((((( INICIANDO ))))))))))))))))))))))))))))))))))))))))))))))))))))))))");

        if(databaseAdapter.getLoginStatus()==1){
            navidateToDashboard();

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                email = txtEmail.getText().toString().replaceAll("\\s", "");
                password = txtPassword.getText().toString().replaceAll("\\s", "");
                if ((TextUtils.isEmpty(email) || (TextUtils.isEmpty(password)))) {
                    txtEmail.setError("Parámetro requerido");
                    txtPassword.setError("Parámetro requerido");
                } else {
                    UserCredential userCredential = new UserCredential(email, password);
                    showProgress();
                    login(userCredential);
                }
                break;
        }

    }

    public void login(final UserCredential userCredential) {

        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
        Call<JsonObject> call = loginRetrofitInterface.login(userCredential);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200){
                    User user = new User();
                    Company company = new Company();
                    ArrayList<WorkingPeriod> workingPeriodList = new ArrayList<WorkingPeriod>();
                    double companyLatitude = response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("latitud").getAsDouble();
                    double companyLongitude = response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("longitud").getAsDouble();
                    Location companyLocation = new Location("dummyprovider");
                    companyLocation.setLatitude(companyLatitude);
                    companyLocation.setLongitude(companyLongitude);
                    user.setNames(response.body().getAsJsonObject("data").get("nombres").toString());
                    user.setLastnames(response.body().getAsJsonObject("data").get("apellidos").toString());
                    user.setEmail(response.body().getAsJsonObject("data").get("email").toString());
                    company.setName(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("nombre").toString());
                    company.setLocation(companyLocation);
                    company.setRadius(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("radio").getAsDouble());
                    user.setCompany(company);

                    JsonArray a = response.body().getAsJsonObject("data").getAsJsonObject("cargo").getAsJsonArray("periodos");

                    for (int i = 0; i < a.size(); i++) {
                        JsonObject workingPeriodJson = a.get(i).getAsJsonObject();
                        JsonObject sJson = a.get(i).getAsJsonObject();

                        Calendar calendarStart = Calendar.getInstance();

                        calendarStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(workingPeriodJson.get("horainicio").getAsString().split(":")[0]));
                        calendarStart.set(Calendar.MINUTE, Integer.parseInt(workingPeriodJson.get("horainicio").getAsString().split(":")[1]));
                        calendarStart.set(Calendar.SECOND, Integer.parseInt(workingPeriodJson.get("horainicio").getAsString().split(":")[2]));

                        Calendar calendarEnd = Calendar.getInstance();
                        calendarEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(workingPeriodJson.get("horafin").getAsString().split(":")[0]));
                        calendarEnd.set(Calendar.MINUTE, Integer.parseInt(workingPeriodJson.get("horafin").getAsString().split(":")[1]));
                        calendarEnd.set(Calendar.SECOND, Integer.parseInt(workingPeriodJson.get("horafin").getAsString().split(":")[2]));

                        String StringDay = workingPeriodJson.getAsJsonObject("dia").get("nombre").getAsString();
                        int intDay;

                        switch (StringDay){
                            case "Lunes":
                                intDay = 1;
                                break;
                            case "Martes":
                                intDay = 2;
                                break;
                            case "Miércoles":
                                intDay = 3;
                                break;
                            case "Jueves":
                                intDay = 4;
                                break;
                            case "Viernes":
                                intDay = 5;
                                break;
                            case "Sábado":
                                intDay = 6;
                                break;
                            case "Domingo":
                                intDay = 7;
                                break;
                            default:
                                intDay = 1;
                                break;
                        }

                        workingPeriodList.add(new WorkingPeriod(calendarStart, calendarEnd, intDay));


                    }
                    databaseAdapter.insertUser(user);
                    User myUser = databaseAdapter.getUser();
                    System.out.println("(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
                    System.out.println(myUser.getNames());
                    System.out.println(myUser.getLastnames());
                    System.out.println(myUser.getEmail());
                    System.out.println("(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
                    databaseAdapter.insertLoginStatus(1);

                    navidateToDashboard();

                }else if(response.code()==404 || response.code()==401){
                    System.out.println("USUARIO NO ENCONTRADO ==================================================");
                    showCredentialsErrorMessage();
                }

                System.out.println(response.toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("NO TENEMOS RESPUESTA");
                System.out.println(call.toString());
                System.out.println(t.toString());
                showConectionErrorMessage();
            }

        });
    }

    public void showProgress() {
        btnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showCredentialsErrorMessage() {
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Autenticación fallida");
        InformationDialog.setMessage("El usuario y contraseña proporcionados no son correctos.");
        InformationDialog.showDialog();
        hideProgress();
    }

    public void showConectionErrorMessage() {
        System.out.println("error de conexion -------------------------------------------------------------");
        InformationDialog.createDialog(this);
        InformationDialog.setTitle("Error de conexión");
        InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
        InformationDialog.showDialog();
        hideProgress();
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
    }

    public void navidateToDashboard() {
        Intent intent = new Intent(this, BottomNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
