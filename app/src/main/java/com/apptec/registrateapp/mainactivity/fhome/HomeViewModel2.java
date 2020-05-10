package com.apptec.registrateapp.mainactivity.fhome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

@Deprecated
public class HomeViewModel2 extends ViewModel {
    /**
     * This is not used anymore
     *
     * @deprecated
     */

    private MutableLiveData<String> mText;

    public HomeViewModel2() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}