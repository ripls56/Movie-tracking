package com.example.pr7.ui.waiting;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.pr7.MainActivity;
import com.example.pr7.R;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateViewModel extends ViewModel {

    private boolean isPremierFragment = false;
    private int year = GregorianCalendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow"))).get(Calendar.YEAR);
    private int month = GregorianCalendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow"))).get(Calendar.MONTH);
    private static DateViewModel instance;

    private DateViewModel(){}

    public static synchronized DateViewModel getInstance() {
        if (instance == null) {
            instance = new DateViewModel();
        }
        return instance;
    }

    public void loadPremier(TextView errorText, EditText edtYear, EditText edtMonth, Fragment fragment){
        if (errorText.getText().toString().equals("") && !edtYear.getText().toString().equals("") && !edtMonth.getText().toString().equals("")){
            MainActivity.isIndeterminate.set(true);
            year = Integer.parseInt(edtYear.getText().toString());
            month = Integer.parseInt(edtMonth.getText().toString());
            PremierFragment premierFragment = new PremierFragment();
            fragment.getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, premierFragment).commit();
            isPremierFragment = true;
        }
    }

    public void changeDate(Fragment fragment) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        fragment.getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
        isPremierFragment = false;
    }

    public boolean isPremierFragment() {
        return isPremierFragment;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
