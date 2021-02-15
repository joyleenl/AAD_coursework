package com.example.llesson1.pillReminder.data.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;

import com.example.llesson1.R;

public class DayViewCheckBox extends AppCompatCheckBox {

    private final Context context;

    public DayViewCheckBox(Context context) {
        super(context);
        this.context = context;
    }

    public DayViewCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void setChecked(boolean t){
        if(t) {
            this.setTextColor(Color.BLACK);
        } else {
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.pillButton));
        }
        super.setChecked(t);
    }
}
