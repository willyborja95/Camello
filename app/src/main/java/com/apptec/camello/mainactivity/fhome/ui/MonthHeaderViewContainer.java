package com.apptec.camello.mainactivity.fhome.ui;

import android.view.View;
import android.widget.TextView;

import com.apptec.camello.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

public class MonthHeaderViewContainer extends ViewContainer {
    private TextView monthText;


    public MonthHeaderViewContainer(View view) {
        super(view);
        monthText = view.findViewById(R.id.calendarMonthText);

    }

    public TextView getMonthText() {
        return monthText;
    }


}
