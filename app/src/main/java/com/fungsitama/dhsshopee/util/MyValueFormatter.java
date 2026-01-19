package com.fungsitama.dhsshopee.util;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter implements IValueFormatter
{

    private final DecimalFormat mFormat;

/*    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }*/
    public MyValueFormatter() {
        mFormat = new DecimalFormat("0");
    }
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " Rp";
    }
    /*public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " $";
    }*/
}
