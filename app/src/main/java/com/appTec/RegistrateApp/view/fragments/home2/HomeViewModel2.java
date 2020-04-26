package com.appTec.RegistrateApp.view.fragments.home2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel2 extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel2() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}