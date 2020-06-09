package com.apptec.registrateapp.repository.webservices;

import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

public abstract class GeneralCallback<T> implements Callback {


    @Override
    public void onFailure(Call call, Throwable t) {
        // We manage all the onFailure callBacks here,
        // but you can override this function if you want
        Timber.e(t);

    }
}
