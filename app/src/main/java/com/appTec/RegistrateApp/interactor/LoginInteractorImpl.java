package com.appTec.RegistrateApp.interactor;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.models.UserCredential;
import com.appTec.RegistrateApp.models.WorkingPeriod;
import com.appTec.RegistrateApp.presenter.LoginPresenterImpl;
import com.appTec.RegistrateApp.repository.webServices.ApiClient;
import com.appTec.RegistrateApp.repository.webServices.interfaces.LoginRetrofitInterface;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractorImpl implements LoginInteractor {
    /**
     * Implementation of the interface
     */

    // Attributes
    private LoginPresenterImpl loginPresenter; // Got as an attribute


    // Constructor
    public LoginInteractorImpl(LoginPresenterImpl loginPresenter) {
        /**
         * Constructor
         */
        this.loginPresenter = loginPresenter;
    }


    @Override
    public void getInitialData() {
        /**
         * Get the data needed
         */
        // ToDo: Get tha data needed to the login case
    }

    @Override
    public void loadInitialData() {
        /**
         * Call the presenter
         */
        loginPresenter.loadInitialData();

    }

    @Override
    public void handleLogin(String email, String password) {
        /**
         * Created a UserCredentials Instance
         */
        UserCredential userCredential = new UserCredential(email, password);

        LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);

        Call<JsonObject> call = loginRetrofitInterface.login(userCredential);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                /**
                 * On success:
                 * Set the logged user.
                 *
                 * On failure:
                 * Advise the view
                 */
                if(response.code() == 200){


                }else{
                    // ToDo: Order this code
//                    if (android.os.Build.VERSION.SDK_INT >= 23) {
//                        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 225);
//                        } else {
//                            if (android.os.Build.VERSION.SDK_INT >= 23 && android.os.Build.VERSION.SDK_INT < 26) {
//                                deviceImei = telephonyManager.getDeviceId();
//                            }
//                            if (android.os.Build.VERSION.SDK_INT >= 26) {
//                                deviceImei = telephonyManager.getImei();
//                            }
//                        }
//                    }


                    User user = new User();
                    Company company = new Company();
                    ArrayList<WorkingPeriod> workingPeriodList = new ArrayList<WorkingPeriod>();
                    user.setId(response.body().getAsJsonObject("data").get("id").getAsInt());
                    user.setName(response.body().getAsJsonObject("data").get("nombres").getAsString());
                    user.setLastName(response.body().getAsJsonObject("data").get("apellidos").getAsString());
                    user.setEmail(response.body().getAsJsonObject("data").get("email").getAsString());
                    company.setName(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("nombre").getAsString());
                    company.setLatitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("latitud").getAsDouble());
                    company.setLongitude(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("longitud").getAsDouble());
                    company.setRadius(response.body().getAsJsonObject("data").getAsJsonObject("empresa").get("radio").getAsFloat());
                    user.setCompany(company);

                    // Get the token
                    String token = response.body().get("token").toString().replace("\"", "");

                    // Save data on local database

                    databaseAdapter.insertUser(user);
                    databaseAdapter.insertCompany(company);
                    changeWorkingState(Constants.STATE_NOT_WORKING);
                    setLoggedUser();
                    findUserDevice();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }
}

