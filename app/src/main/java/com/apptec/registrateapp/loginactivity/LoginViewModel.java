package com.apptec.registrateapp.loginactivity;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apptec.registrateapp.R;

import timber.log.Timber;

public class LoginViewModel extends AndroidViewModel {
    /**
     * View model for the login activity
     */

    // This variable help to show error is the form in the ui is wrong
    public MutableLiveData<LoginFormState> loginFormState;


    private MutableLiveData<LoginResult> loginResult;


    // Presenter that do hard work
    private LoginPresenter loginPresenter;


    public LoginViewModel(@NonNull Application application) {
        /** Constructor */
        super(application);

        loginFormState = new MutableLiveData<>();
        loginResult = new MutableLiveData<>();

        loginPresenter = new LoginPresenter();
        Timber.d("Login view model view model built");

        // Verify if is there a previous user session active
        verifyPreviousLogin();

    }

    private void verifyPreviousLogin() {
        /**
         * Verify if is there a previous user session active
         *
         * Change the login result if yes and the login activity will know that she should navigate
         * to the main activity
         */
        loginPresenter.verifyPreviousLogin(loginResult);

    }

    /**
     * Expose the data
     */
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     *
     */
    public void loginClicked(String email, String password) {
        /**
         * Called when the login button is clicked
         * - Validates username and password
         *
         */
        // Validates data
        if (!isUserNameValid(email) || !isPasswordValid(password)) {
            Timber.d("Invalid form");
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, R.string.invalid_password));
        } else {
            Timber.d("Valid form");

            // This event will be listen by the activity that will show the progress bar
            loginFormState.setValue(new LoginFormState(true));


            // Call the presenter to verify the credentials and the log the user
            // Send the result so the activity can observe automatically the result
            loginPresenter.handleLogin(loginResult, email, password);
        }


    }


    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }


}
