package com.apptec.registrateapp.auth;

import androidx.annotation.Nullable;

import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.models.WorkZoneModel;

import java.util.List;

public class LoginDataValidator {
    /**
     * This class will hold the logging data response in objects
     */

    public UserModel user;
    public CompanyModel company;
    @Nullable
    public DeviceModel device;
    public List<WorkZoneModel> workZoneModels;
    public String accessToken, refreshToken;

    public LoginDataValidator(UserModel user,
                              CompanyModel company,
                              @Nullable DeviceModel userDevice,
                              List<WorkZoneModel> workzones,
                              String accessToken,
                              String refreshToken) {
        this.user = user;
        this.company = company;
        this.device = userDevice;
        this.workZoneModels = workzones;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public LoginDataValidator getValidData() {
        // Main method of this class, to know if the response is valid or not
        // Validate data her if needed
        // Timber.d("Validating login  data");


        return this;
    }


}
