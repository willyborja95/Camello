package com.apptec.registrateapp.view.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.models.WorkingPeriod;
import com.apptec.registrateapp.util.Constants;
import com.apptec.registrateapp.view.generic.DayViewContainer;
import com.apptec.registrateapp.view.generic.MonthHeaderViewContainer;
import com.apptec.registrateapp.viewmodel.MainViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    /**
     * Home fragment
     */

    private final String TAG = HomeFragment.class.getSimpleName();

    // Instance of ViewModel
    private MainViewModel mainViewModel;

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-419");


    @BindView(R.id.calendarView)
    CalendarView calendarView;
    Button centralButton;
    TextView centralMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);                    // Getting the view model
        mainViewModel.setActiveFragmentName(getString(R.string.home_fragment_title));


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false); // Inflate the view

        ButterKnife.bind(this, view);

        // Binding UI elements (THis can be replaced with data binding)
        centralButton = view.findViewById(R.id.fragment_home_start_button);
        centralMessage = view.findViewById(R.id.fragment_home_button_message);

        this.setupCalendar();                        // Setting up the calendar view

        this.setupCentralButtonManager();                       // Setting up the button


        // Creating a lister for the button
        centralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.changeLastWorkingState();
            }
        });


        return view;
    }


    private void setupCentralButtonManager() {
        /**
         * Setting up an observer to the mLastWorkingPeriod
         */

        mainViewModel.getLastWorkingPeriod().observe(this, new Observer<WorkingPeriod>() {
            @Override
            public void onChanged(WorkingPeriod workingPeriod) {
                try {
                    if (workingPeriod.getStatus() == Constants.INT_NOT_INIT_STATUS) {
                        // The user is working
                        centralButton.setText(getString(R.string.home_button_start_message));
                        centralMessage.setText(getString(R.string.home_text_view_start_message));

                    } else {
                        // It is not
                        Log.w(TAG, "No working period create yet");
                        centralButton.setText(getString(R.string.home_button_finish_message));
                        centralMessage.setText(getString(R.string.home_text_view_finish_message));

                    }
                } catch (NullPointerException npe) {
                    // It is not
                    centralButton.setText(getString(R.string.home_button_start_message));
                    centralMessage.setText(getString(R.string.home_text_view_start_message));

                }

            }
        });

    }


    private void setupCalendar() {
        /**
         * Here is the logic for setup the calendar
         */
        // Calendar
        AndroidThreeTen.init(getActivity());
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
    }

}
