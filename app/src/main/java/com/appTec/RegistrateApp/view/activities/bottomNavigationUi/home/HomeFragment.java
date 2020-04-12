package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.services.webServices.ApiClient;
import com.appTec.RegistrateApp.services.webServices.interfaces.AssistanceRetrofitInterface;
import com.appTec.RegistrateApp.services.webServices.interfaces.TimeRetrofit;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.BottomNavigation;
import com.appTec.RegistrateApp.view.activities.generic.DayViewContainer;
import com.appTec.RegistrateApp.view.activities.generic.InformationDialog;
import com.appTec.RegistrateApp.view.activities.generic.MonthHeaderViewContainer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.gson.JsonObject;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        OnMapReadyCallback {

    public static final Locale LOCALE_ES = Locale.forLanguageTag("es-419");
    private HomeViewModel homeViewModel;

    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.start_timer_btn)
    Button startTimerButton;


    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Circle circle;

    private Context context;
    private static boolean isWithinRadius;

    //private AlarmManager alarmManager;
    //private PendingIntent alarmIntent;
    private BottomNavigation bottomNavigationActivity;
    private Company company = new Company();
    private SharedPreferences pref;
    private Location location = new Location("");
    ProgressDialog dialog;
    Device device;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /*
    =======================================
    FRAGMENT LYFECYCLE METHODS
    =======================================
     */

    //Create methods
    @Override
    public void onAttach(Context context) {
        Log.d("isAttached", "Si esta attachado!");
        super.onAttach(context);
        this.context = (Context) context;
        pref = this.context.getSharedPreferences("RegistrateApp", 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        AndroidThreeTen.init(getActivity());
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                cArg.setText(String.format("%02d:%02d:%02d", h, m, s));
            }
        });
        timer.setText("00:00:00");
        bottomNavigationActivity = (BottomNavigation) getActivity();
        dialog = new ProgressDialog(getActivity());

        // Google maps setup
        buildGoogleApiClient();
        googleApiClient.connect();

        // Set button text to the current state of the worker
        startTimerButton.setText(getButtonTextForState(getCurrentWorkingState()));

        // Calendar
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay calendarDay) {
                container.getDayText().setText(calendarDay.getDate().getDayOfMonth() + "");
                String dayName =
                        calendarDay.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, LOCALE_ES);
                container.getDayNameText().setText(dayName);
            }
        });
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderViewContainer>() {
            @NonNull
            @Override
            public MonthHeaderViewContainer create(@NonNull View view) {
                return new MonthHeaderViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthHeaderViewContainer container, @NonNull CalendarMonth calendarMonth) {
                String month =
                        calendarMonth.getYearMonth().getMonth().getDisplayName(TextStyle.FULL, LOCALE_ES);
                String monthYearTxt = String.format("%s de %d", month, calendarMonth.getYear());
                container.getMonthText().setText(monthYearTxt);
                // Week scroll buttons
                container.getLeftMonthButton().setOnClickListener(view -> {
                    Toast.makeText(getActivity(), "<-", Toast.LENGTH_SHORT).show();
                });
            }
        });
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("visible", "now is visible!");
        updateTimer();
    }

    //Ending methods
    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    /*
    =======================================
    LOCATION METHODS
    =======================================
     */

    protected synchronized void buildGoogleApiClient() {
        Log.d("testgps", "build google apli client!!");
        googleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
    }


    @Override
    public void onLocationChanged(Location location) {
        float[] distance = new float[2];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                company.getLatitude(), company.getLongitude(), distance);
        this.location = location;
        isWithinRadius = distance[0] < company.getRadius();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10);
        locationRequest.setFastestInterval(10);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(0.1F);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this::onLocationChanged);
    }

    @Override
    public void onConnectionSuspended(int i) {
//        Toast.makeText(getContext(), "connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(getContext(), "connection failed", Toast.LENGTH_SHORT).show();
    }


    /*
    =======================================
    BUSINESS LOGIC METHODS
    =======================================
     */

    //Business logic methods
    public void setDevice(Device device) {
        this.device = device;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void updateTimer() {
        timer.setText("00:00:00");
        if (getCurrentWorkingState().equals(Constants.STATE_WORKING)) {

            TimeRetrofit timeRetrofit = ApiClient.getClient().create(TimeRetrofit.class);

            Call<JsonObject> timeCall = timeRetrofit.get(getUserToken());
            timeCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if ((response.code() == 200) && (response != null)) {
                        Log.d("time", response.body().toString());
                        JsonObject timeJson = response.body().get("data").getAsJsonObject();
                        long workingTimeMilis = 0;
                        if (timeJson.has("tiempoLaborado")) {
                            String workingTime = timeJson.get("tiempoLaborado").getAsString();
                            int hours = Integer.valueOf(workingTime.split(":")[0]);
                            int minutes = Integer.valueOf(workingTime.split(":")[1]);
                            int seconds = Integer.valueOf(workingTime.split(":")[2]);
                            workingTimeMilis = TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
                        }
                        timer.setBase(SystemClock.elapsedRealtime() - workingTimeMilis);
                        timer.start();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }

    private void syncAssistances(String lastTimeExited) {
        Log.d("log", "funcion syncAssistances");
        AssistanceRetrofitInterface assistanceRetrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Assistance assistance = new Assistance(device.getId(), this.location.getLatitude(), this.location.getLongitude(), lastTimeExited);
        Call<JsonObject> assistanceCall = assistanceRetrofitInterface.sync(getUserToken(), assistance);
        assistanceCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("*** SYNC ***");
                if (response.code() != 200) {
                    try {
                        JSONObject errorJson = new JSONObject(response.errorBody().string());
                        Log.d("errSync", errorJson.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 200) {
                    Log.d("logDate", lastTimeExited);
                    removeLastTimeExited();
                    registerEntry();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                InformationDialog.createDialog(getActivity());
                InformationDialog.setTitle("Error de conexión");
                InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
                InformationDialog.showDialog();
                dimissEntranceDialog();
            }
        });
    }


    private void registerEntry() {
        AssistanceRetrofitInterface assistanceRetrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Assistance assistanceLog = new Assistance(device.getId(), this.location.getLatitude(), this.location.getLongitude());
        Call<JsonObject> assistanceCall = assistanceRetrofitInterface.post(getUserToken(), assistanceLog);
        assistanceCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dimissEntranceDialog();
                if (response.code() == 200) {
                    showEntryMessage();
                    changeWorkingState(Constants.STATE_WORKING);
                    // Cambiar texto de boton
                    startTimerButton.setText(getButtonTextForState(getCurrentWorkingState()));
                    // Register geofencing
                    bottomNavigationActivity.addGeofencesHandler();
                    updateTimer();
                } else if (response.code() == 400) {
                    try {
                        JSONObject errorJson = new JSONObject(response.errorBody().string());
                        if (errorJson.getInt("code") == 1) {
                            showDeviceDisabledMessage();
                        } else if (errorJson.getInt("code") == 2) {
                            showOutScheduleMessage();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject errorJson = new JSONObject(response.errorBody().string());
                        Log.d("log", errorJson.toString());
                        Log.d("log", "deviceId");
                        Log.d("log", String.valueOf(device.getId()));
                        Log.d("log", String.valueOf(device.getName()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Register alarm manager
                /*
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                int alarmDelaySecs = 60; // Cambiar valor para determinar el intervalo de repeticion en segundos
                long interval = alarmDelaySecs * 1_000L;
                long alarmTime = SystemClock.elapsedRealtime() + interval;
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        alarmTime, interval, alarmIntent);

                 */


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                InformationDialog.createDialog(getActivity());
                InformationDialog.setTitle("Error de conexión");
                InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
                InformationDialog.showDialog();
                dimissEntranceDialog();
            }
        });
    }

    private void registerExit() {
        AssistanceRetrofitInterface assistanceRetrofitInterface = ApiClient.getClient().create(AssistanceRetrofitInterface.class);
        Assistance assistanceLog = new Assistance(device.getId(), this.location.getLatitude(), this.location.getLongitude());
        Call<JsonObject> assistanceCall = assistanceRetrofitInterface.post(getUserToken(), assistanceLog);
        assistanceCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dimissEntranceDialog();
                changeWorkingState(Constants.STATE_NOT_WORKING);
                startTimerButton.setText(getButtonTextForState(getCurrentWorkingState()));
                // Unregister geofencing
                bottomNavigationActivity.removeGeofencesHandler();
                dimissEntranceDialog();
                timer.stop();
                showExitMessage();

                // Cancelar AlarmManager
                /*
                System.out.println("Tratando de cancelar el intent");
                if (alarmManager != null)
                    System.out.println("Cancelando el intent");
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(alarmIntent);

                 */

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                InformationDialog.createDialog(getActivity());
                InformationDialog.setTitle("Error de conexión");
                InformationDialog.setMessage("Al parecer no hay conexión a Internet.");
                InformationDialog.showDialog();
                dimissEntranceDialog();


            }
        });
    }

    private String getButtonTextForState(String workingState) {
        if (workingState.equals(Constants.STATE_WORKING))
            return "Finalizar";
        return "Iniciar";
    }

    //Shared preferences methods
    private String getLastTimeExited() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.LAST_EXIT_TIME,
                "");
    }

    private void removeLastTimeExited() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        sharedPref.edit().remove(Constants.LAST_EXIT_TIME).commit();
    }

    private String getCurrentWorkingState() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.CURRENT_STATE, "");
    }

    private void changeWorkingState(String state) {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CURRENT_STATE, state);
        editor.commit();
    }

    //Shared preferences methods
    public String getUserToken(){
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.USER_TOKEN,
                "");
    }


    //Layout GUI methods
    @OnClick(R.id.start_timer_btn)
    public void startTimer(View view) {
        Log.d("userLoc", "Comapny values");
        Log.d("userLoc", String.valueOf(this.company.getLatitude()));
        Log.d("userLoc", String.valueOf(this.company.getLongitude()));
        // Identificar si el usuario se encuentra dentro de la empresa
        if (isWithinRadius) {
            //Change to working status
            if (this.device != null) {
                String lastTimeExited = getLastTimeExited();
                boolean timerIsRunning = timer.getText() != "00:00:00";
                if ((getCurrentWorkingState().equals(Constants.STATE_NOT_WORKING)) && !lastTimeExited.equals("") && timerIsRunning) {
                    bottomNavigationActivity.removeGeofencesHandler();
                    showExitMessage();
                    startTimerButton.setText(getButtonTextForState(getCurrentWorkingState()));
                    timer.stop();
                    timer.setText("00:00:00");
                } else if (getCurrentWorkingState().equals(Constants.STATE_NOT_WORKING)) {
                    showProgressDialog("Registrando su entrada ...");
                    // Post server
                    if (lastTimeExited.equals("")) {
                        registerEntry();
                    } else {
                        syncAssistances(lastTimeExited);
                    }
                } else if (getCurrentWorkingState().equals(Constants.STATE_WORKING)) {
                    Log.d("test", "saliendo con post");
                    //Post server
                    registerExit();
                }
            } else {
                showDeviceNotRegisteredMessage();
            }

        } else {
            showOutRangeMessage();
        }
    }


    //Dialogs
    public void showProgressDialog(String message) {
        dialog.setMessage(message);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void dimissEntranceDialog() {
        dialog.dismiss();
    }

    private void showOutRangeMessage() {
        showDialog("Fuera de los límites de la empresa", "Para registrar asistencia, por favor diríjase a la empresa.");
    }

    private void showDeviceDisabledMessage() {
        showDialog("Equipo deshabilitado", "El equipo se encuentra deshabilitado para registrar asistencia. Solicite su activación al administrador de la empresa.");

    }

    private void showOutScheduleMessage() {
        showDialog("Fuera de horario", "Usted se encuentra fuera de horario laboral.");
    }

    private void showEntryMessage() {
        showDialog("Entrada registrada", "Se ha registrado satisfactoriamente su entrada.");

    }

    private void showExitMessage() {
        showDialog("Salida registrada", "Se ha registrado satisfactoriamente su salida.");

    }

    public void showDeviceNotRegisteredMessage() {
        showDialog("Equipo no registrado", "Para registrar asistencia, porfavor registre el equipo en la sección de equipos.");
    }

    public void showDialog(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}