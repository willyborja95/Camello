package com.apptec.registrateapp.mainactivity.fpermission;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.apptec.registrateapp.repository.webservices.ApiClient;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PermissionPresenterImpl {

    /**
     * Helps the interaction repository - view
     */
    private static final String TAG = "PermissionPresenterImpl";

    public LiveData<List<PermissionModel>> getLiveDataListPermission() {
        /**
         * It return the live of permission into the database
         */
        return RoomHelper.getAppDatabaseInstance().permissionDao().getLiveDataListPermission();
    }

    public void savePermission(PermissionType selectedItem, Calendar startDate, Calendar endDate) {
        /**
         * Save the permission requested
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * Call the service
                 * on success save the permission into database
                 */
//                LoginRetrofitInterface loginRetrofitInterface = ApiClient.getClient().create(LoginRetrofitInterface.class);
//                Call<LoginResponse> call = loginRetrofitInterface.login(userCredential);
//                call.enqueue(new Callback<LoginResponse>() {
//                }}

                Date startDateDate = startDate.getTime();
                Date endDateDate = endDate.getTime();

                Long startDate = DateConverter.toTimestamp(startDateDate);
                Long endDate = DateConverter.toTimestamp(endDateDate);

                // Creating the body
                PermissionModel permission = new PermissionModel("", selectedItem.getId(), 1, startDate, endDate);
                PermissionDto permissionDto = new PermissionDto(permission);

                PermissionRetrofitInterface permissionRetrofitInterface = ApiClient.getClient().create(PermissionRetrofitInterface.class);
                Call<JsonObject> call = permissionRetrofitInterface.createPermission(
                        ApiClient.getAccessToken(),
                        permissionDto
                );
                Timber.d(permissionDto.toString(), permissionDto);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        // Save the permission also into the database

                        if (response.isSuccessful()) {

                            // Get the permission id form the response and also the status by the way
                            int correctId = Integer.parseInt(response.body().get("data").getAsJsonObject().get("id").toString());
                            int correctStatus = Integer.parseInt(response.body().get("data").getAsJsonObject().get("status").toString());
                            Timber.i("CorrectId: " + correctId);
                            Timber.i("CorrectStatus: " + correctStatus);

                            // Update the permission with the id and status before save into database
                            permission.setId(correctId);
                            permission.setFkPermissionStatus(correctStatus);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    RoomHelper.getAppDatabaseInstance().permissionDao().insertOrReplace(permission);
                                }
                            }).start();
                        } else {
                            // TODO:
                            Timber.e("Response not successful");
                        }



                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        // TODO: Handle this
                        Timber.e(t, "onFailure: %s", t.getMessage());

                    }
                });

            }
        }).start();
    }
}
