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

    // If an error occurs



    // Instancing the presenter her
    MainPresenterImpl mainPresenter = new MainPresenterImpl();

    public SharedViewModel(@NonNull Application application) {
        super(application);

        // Load here the live data needed
        mNotifications = RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
        mUser = RoomHelper.getAppDatabaseInstance().userDao().getLiveDataUser();
        mActiveFragmentName = new MutableLiveData<>();


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


    public MutableLiveData<String> getActiveFragmentName() {
        return mActiveFragmentName;
    }

    public void setActiveFragmentName(String value) {
        this.mActiveFragmentName.setValue(value);
    }



    public MutableLiveData<Boolean> registerDevice(){
        /**
         *      The activity will be observing this to request new device information or not
         */
        // TODO:
        return new MutableLiveData<>(false);
    }


    //
    public boolean isNeededRequestDeviceInformation() {
        /**
         * Calling the presenter
         * This logic is explained in the flowchart:
         * https://app.diagrams.net/#G1tW39YJ03qZdo2Q2cIN5sRUmWxBkAN9YF
         *
         * if you don't have access, contact Renato by email: renatojobal@gmail.com
         */
        if (mainPresenter.isTheFirstLogin()) {
            return true;
        }
        if(! mainPresenter.isTheLoginFromTheSameUser()){
            return true;
        }

        return false;

    }

}
