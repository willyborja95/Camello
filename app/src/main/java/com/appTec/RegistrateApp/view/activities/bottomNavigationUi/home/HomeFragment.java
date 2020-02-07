package com.appTec.RegistrateApp.view.activities.bottomNavigationUi.home;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appTec.RegistrateApp.R;
import com.appTec.RegistrateApp.models.Assistance;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.services.alarmmanager.AlarmBroadcastReceiver;
import com.appTec.RegistrateApp.util.Constants;
import com.appTec.RegistrateApp.view.BottomNavigation;
import com.appTec.RegistrateApp.view.activities.generic.DayViewContainer;
import com.appTec.RegistrateApp.view.activities.generic.MonthHeaderViewContainer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        OnMapReadyCallback {

    public static final Locale LOCALE_ES = Locale.forLanguageTag("es-419");
    private HomeViewModel homeViewModel;

    @BindView(R.id.timer_text) TextView timerText;
    @BindView(R.id.calendarView) CalendarView calendarView;
    @BindView(R.id.start_timer_btn) Button startTimerButton;
    @BindView(R.id.map_layout) RelativeLayout mapLayout;

    private GoogleMap map;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Circle circle;

    private FragmentActivity context;
    private static boolean isWithinRadius;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private BottomNavigation bottomNavigationActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AndroidThreeTen.init(getContext());
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        bottomNavigationActivity = (BottomNavigation) getActivity();

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
                    Toast.makeText(getContext(), "<-", Toast.LENGTH_SHORT).show();
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
    public void onAttach(@NonNull Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @OnClick(R.id.start_timer_btn)
    public void startTimer(View view) {
        // Identificar si el usuario se encuentra dentro de la empresa
        if (isWithinRadius) {
            Log.i(HomeFragment.class.getSimpleName(), "Se encuentra dentro de la empresa");
            // Cambiar estado a trabajando
            if (getCurrentWorkingState().equals(Constants.STATE_NOT_WORKING)) {
                changeWorkingState(Constants.STATE_WORKING);

                // Registrar alarm manager
                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                int alarmDelaySecs = 60; // Cambiar valor para determinar el intervalo de repeticion en segundos
                long interval = alarmDelaySecs * 1_000L;
                long alarmTime = SystemClock.elapsedRealtime() + interval;
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        alarmTime, interval, alarmIntent);

                // Register geofencing
                bottomNavigationActivity.addGeofencesHandler();

                // Post server
                updateAssistance();
            }
            else if (getCurrentWorkingState().equals(Constants.STATE_WORKING)) {
                changeWorkingState(Constants.STATE_NOT_WORKING);

                // Cancelar AlarmManager
                if (alarmManager != null)
                    alarmManager.cancel(alarmIntent);

                // Unregister geofencing
                bottomNavigationActivity.removeGeofencesHandler();

                // Post server
                updateAssistance();
            }
        }
        else {
            if (getCurrentWorkingState().equals(Constants.STATE_WORKING)) {
                changeWorkingState(Constants.STATE_NOT_WORKING);
                // Unregister geofencing
                bottomNavigationActivity.removeGeofencesHandler();
            }
            else
                Snackbar.make(view, "No se encuentra dentro de la empresa", Snackbar.LENGTH_SHORT).show();
        }

        // Cambiar texto de boton
        ((Button) view).setText(getButtonTextForState(getCurrentWorkingState()));
    }

    private void updateAssistance() {


    }

    private void changeWorkingState(String state) {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CURRENT_STATE, state);
        editor.commit();
    }

    private String getCurrentWorkingState() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                Constants.SHARED_PREFERENCES_GLOBAL, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.CURRENT_STATE,
                Constants.STATE_NOT_WORKING);
    }

    private String getButtonTextForState(String workingState) {
        if (workingState.equals(Constants.STATE_WORKING))
            return "Finalizar"; // Extraer a values/strings.xml
        return "Iniciar";
    }

    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(getContext(),"Building api client",Toast.LENGTH_SHORT).show();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            MapFragment mapFragment = (MapFragment) context.getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);

        circle();
    }

    public void circle() {
        double radiusInMeters = 100;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        circle = map.addCircle (new CircleOptions()
                .center(new LatLng(Constants.LATITUDE_TESTING, Constants.LONGITUDE_TESTING))
                .radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(1));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("locationtesting", "lat: " + location.getLatitude() + "lon: " +
                location.getLongitude());

        if (circle != null) {
            float[] distance = new float[2];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    circle.getCenter().latitude, circle.getCenter().longitude, distance);

            // Registrar si usuario esta dentro de la empresa mientras la aplicacion esta en primer plano
            isWithinRadius = distance[0] < circle.getRadius();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) { }

    @Override
    public void onProviderEnabled(String s) { }

    @Override
    public void onProviderDisabled(String s) { }

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

    @OnClick(R.id.open_map)
    protected void openMap(View view) {
        mapLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.close_map)
    protected void closeMap(View view) {
        mapLayout.setVisibility(View.INVISIBLE);
    }

}