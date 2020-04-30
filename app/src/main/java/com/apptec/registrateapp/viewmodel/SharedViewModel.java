package com.apptec.registrateapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.models.User;
import com.apptec.registrateapp.presenter.MainPresenterImpl;
import com.apptec.registrateapp.repository.localDatabase.RoomHelper;

import java.util.List;


public class SharedViewModel extends AndroidViewModel {

    // To show the notifications
    private final LiveData<List<Notification>> mNotifications; // List of notifications

    // Toolbar name according the active fragment
    private MutableLiveData<String> mActiveFragmentName;

    // This info will be on the drawer
    private final LiveData<User> mUser;

    // To handle if needed the first login
    private MutableLiveData<Boolean> isNeededRegisterDevice;



    // Instancing the presenter her
    MainPresenterImpl mainPresenter;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        // Initialize the presenter
        mainPresenter  = new MainPresenterImpl();

        // Load here the live data needed
        mNotifications = RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
        mUser = RoomHelper.getAppDatabaseInstance().userDao().getLiveDataUser();
        mActiveFragmentName = new MutableLiveData<>();

        // Handle the first login if is needed
        isNeededRegisterDevice = new MutableLiveData<>(false);
        initializeDeviceVerification();

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

    ;

    public LiveData<User> getCurrentUser() {
        /** Exposing the user */
        return mUser;
    }

    // Control th ui toolbar name
    public MutableLiveData<String> getActiveFragmentName() {
        return mActiveFragmentName;
    }
    public void setActiveFragmentName(String value) {
        this.mActiveFragmentName.setValue(value);
    }


    public MainPresenterImpl getMainPresenter(){
        return this.mainPresenter;
    }

    public void setIsNeededRegisterDevice(MutableLiveData<Boolean> value){
        this.isNeededRegisterDevice = value;
    }


    public MutableLiveData<Boolean> getIsNeededRegisterDevice(){
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
}
