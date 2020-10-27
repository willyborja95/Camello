package com.apptec.camello.repository.webservices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * In this class we call handle all the onFailures calls that use this response
 * Also we can try the call another time or whatever
 *
 * @param <T>
 */
public abstract class GeneralCallback<T> implements Callback<T> {


    private static final int TOTAL_RETRIES = 2;
    private final Call<T> call;
    private int retryCount = 0;


    /**
     * Constructor
     *
     * @param call call to wrap
     */
    public GeneralCallback(Call<T> call) {
        this.call = call;
    }


    /**
     * Do some validations before call the onFinalResponse
     *
     * @param call     call
     * @param response response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (!response.isSuccessful()) {
                Timber.w("Response is not successful");
            }
            Timber.w("Response: %s", response.toString());
            Timber.w("Request body: %s", call.request().body().toString());
        } catch (Exception e) {
            Timber.w("The response have some problems");
            Timber.w(e);
        }

        // Finally call the onFinalResponse, a method that have to be override by the implementations classes
        this.onFinalResponse(call, response);
    }


    /**
     * We manage all the onFailure callBacks here,
     * but you can override this function if you want
     *
     * @param call call
     * @param t    throwable
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Timber.e("Call failed ");
        Timber.e(t);

        // Retry the call
        if (retryCount++ < TOTAL_RETRIES) {
            Timber.i("Retrying call (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retryCall();
        } else {
            onFinalFailure(call, t);
        }

    }


    /**
     * Method that will be called after the onResponse() default method after doing some validations
     * * see {@link GeneralCallback}
     * This need to be override by the classes that implement GeneralCallback
     *
     * @param call     call
     * @param response response
     */
    abstract public void onFinalResponse(Call<T> call, Response<T> response);

    /**
     * Method to be override by the calling class
     *
     * @param call call
     * @param t    throwable
     */
    public void onFinalFailure(Call<T> call, Throwable t) {
        Timber.i("On final failure no override");
        Timber.e("Call failed ");
        Timber.e(t);
    }

    private void retryCall() {
        Timber.i("Retrying call");
        call.clone().enqueue(this); // clone the original call and enqueue it for retry
    }
}
