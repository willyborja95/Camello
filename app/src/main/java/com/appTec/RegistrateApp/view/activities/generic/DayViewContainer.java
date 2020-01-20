package com.appTec.RegistrateApp.view.activities.generic;

import android.view.View;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

public class DayViewContainer extends ViewContainer {

    private TextView dayText;
    private TextView dayNameText;

    public DayViewContainer(View view) {
        super(view);
        dayText = view.findViewById(R.id.calendarDayText);
        dayNameText = view.findViewById(R.id.calendarDayNameText);
    }

    public TextView getDayText() {
        return dayText;
    }

    public TextView getDayNameText() {
        return dayNameText;
    }
}
