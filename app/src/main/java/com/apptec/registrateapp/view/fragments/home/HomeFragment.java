package com.apptec.registrateapp.view.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apptec.registrateapp.R;
import com.apptec.registrateapp.view.generic.DayViewContainer;
import com.apptec.registrateapp.view.generic.MonthHeaderViewContainer;
import com.apptec.registrateapp.viewmodel.SharedViewModel;
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

    // Instance of ViewModel
    private SharedViewModel sharedViewModel;

    private static final Locale LOCALE_ES = Locale.forLanguageTag("es-419");


    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);                    // Getting the view model
        sharedViewModel.setActiveFragmentName(getString(R.string.home_fragment_title));


    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false); // Inflate the view

        ButterKnife.bind(this, view);

        setupCalendar();                        // Setting up the calendar view

        return view;
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
