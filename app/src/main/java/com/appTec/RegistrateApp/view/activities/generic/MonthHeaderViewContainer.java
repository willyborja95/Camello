package com.appTec.RegistrateApp.view.activities.generic;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appTec.RegistrateApp.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

public class MonthHeaderViewContainer extends ViewContainer {
    private TextView monthText;
    private ImageButton leftMonthButton;
    private ImageButton rightMonthButton;

    public MonthHeaderViewContainer(View view) {
        super(view);
        monthText = view.findViewById(R.id.calendarMonthText);
        leftMonthButton = view.findViewById(R.id.monthLeftBtn);
        rightMonthButton = view.findViewById(R.id.monthRightBtn);
    }

    public TextView getMonthText() {
        return monthText;
    }

    public ImageButton getLeftMonthButton() {
        return leftMonthButton;
    }

    public ImageButton getRightMonthButton() {
        return rightMonthButton;
    }
}
