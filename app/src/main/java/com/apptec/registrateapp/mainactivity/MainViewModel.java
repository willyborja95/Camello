package com.apptec.registrateapp.mainactivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.models.User;
import com.apptec.registrateapp.models.WorkingPeriod;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;
import com.apptec.registrateapp.repository.workers.WorkingPeriod.ChangeWorkingStatus;
import com.apptec.registrateapp.repository.workers.retreshtoken.RefreshTokenWorker;
import com.apptec.registrateapp.util.Constants;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    // To show the notifications
    private final LiveData<List<Notification>> mNotifications; // List of notifications

    // To show devices
    private final LiveData<List<Device>> mDevices;          // List of user devices

    // Toolbar name according the active fragment
    private MutableLiveData<String> mActiveFragmentName;

    // This info will be on the drawer
    private final LiveData<User> mUser;

    // This boolean variable show if the user is working or not
    private LiveData<WorkingPeriod> mLastWorkingPeriod;

    // To handle if needed the first login
    private MutableLiveData<Boolean> isNeededRegisterDevice;

    // Work manager
    private WorkManager workManager = WorkManager.getInstance(App.getContext());


    // Instancing the presenter her
    MainPresenterImpl mainPresenter;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // Initialize the presenter
        mainPresenter = new MainPresenterImpl();

        // Load here the live data needed
        mNotifications = RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
        mDevices = RoomHelper.getAppDatabaseInstance().deviceDao().loadAllDevicesLiveData();
        mUser = RoomHelper.getAppDatabaseInstance().userDao().getLiveDataUser();
        mActiveFragmentName = new MutableLiveData<>();
        mLastWorkingPeriod = RoomHelper.getAppDatabaseInstance().workingPeriodDao().getLiveDataLastWorkingPeriod();


        // Handle the first login if is needed
        isNeededRegisterDevice = new MutableLiveData<>(false);
        initializeDeviceVerification();

        // Start the auto refresh token
        this.initRefreshToken();

    }


    private void initializeDeviceVerification() {
        mainPresenter.initializeDeviceVerification(isNeededRegisterDevice);
    }


    /**
     * Expose the LiveData so the UI can observe it for the fragment Notification
     */
    public LiveData<List<Notification>> getNotifications() {
        /** Exposing the notifications */
        return mNotifications;
    }

    public LiveData<User> getCurrentUser() {
        /** Exposing the user */
        return mUser;
    }

    public LiveData<List<Device>> getDevices() {
        /** Exposing the list of devices */
        return mDevices;
    }

    public LiveData<WorkingPeriod> getLastWorkingPeriod() {
        /** Exposing the last working period */
        return mLastWorkingPeriod;
    }


    public void changeLastWorkingState() {
        /**
         * If the user is working change to no working and vice versa
         */

//        // The user is working, change to resting
//        mainPresenter.changeLastWorkingState(Constants.INT_WORKING_STATUS);


        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        OneTimeWorkRequest changeWorkingStateRequest = new OneTimeWorkRequest.Builder(
                ChangeWorkingStatus.class)
                .setConstraints(constraints)
                .build();

        workManager.enqueue(changeWorkingStateRequest);


    }


    // Control th ui toolbar name
    public MutableLiveData<String> getActiveFragmentName() {
        return mActiveFragmentName;
    }

    public void setActiveFragmentName(String value) {
        this.mActiveFragmentName.setValue(value);
    }


    public MainPresenterImpl getMainPresenter() {
        return this.mainPresenter;
    }

    public void setIsNeededRegisterDevice(MutableLiveData<Boolean> value) {
        this.isNeededRegisterDevice = value;
    }


    public MutableLiveData<Boolean> getIsNeededRegisterDevice() {
        /**
         *      The activity will be observing this to request new device information or not
         */
        // TODO:
        return this.isNeededRegisterDevice;
    }


    public void saveThisDevice(String name, String model) {
        /**
         * Method to save this device to the server
         */
        mainPresenter.saveThisDevice(name, model, this.isNeededRegisterDevice);

    }


    private void initRefreshToken() {
        /**
         * This method we got a worker for refresh the token periodically
         */

        // Constraints: Do the work if the the network is connected
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Create a work request
        PeriodicWorkRequest refreshTokenRequest = new PeriodicWorkRequest.Builder(
                RefreshTokenWorker.class,
                Constants.ACCESS_TOKEN_EXPIRATION,
                Constants.ACCESS_TOKEN_EXPIRATION_UNIT)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniquePeriodicWork(
                "refresher",
                ExistingPeriodicWorkPolicy.REPLACE,
                refreshTokenRequest);

    }


}
