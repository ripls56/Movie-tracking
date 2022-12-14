package com.example.pr7.ui.waiting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pr7.MainActivity;
import com.example.pr7.R;
import com.example.pr7.databinding.FragmentDatePickerBinding;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

public class DatePickerFragment extends Fragment {

    private FragmentDatePickerBinding binding;
    TextView errorText;
    DateViewModel dateViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateViewModel = DateViewModel.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatePickerBinding.inflate(inflater, container, false);
        EditText edtYear = binding.premierYear;
        EditText edtMonth = binding.premierMonth;
        Button btnDateSet = binding.premierDateSet;
        errorText = binding.premierError;
        edtYear.setText(String.valueOf(dateViewModel.getYear()));
        edtMonth.setText(String.valueOf(dateViewModel.getMonth()));
        edtMonthTextChangedListener(edtYear, edtMonth);
        edtYearTextChangedListener(edtYear, edtMonth);
        btnDateSet.setOnClickListener(view -> {
            dateViewModel.loadPremier(errorText, edtYear, edtMonth, this);
        });
        return binding.getRoot();
    }



    private void edtMonthTextChangedListener(EditText edtYear, EditText edtMonth) {
        edtMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorCheck(edtYear, edtMonth);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void edtYearTextChangedListener(EditText edtYear, EditText edtMonth) {
        edtYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorCheck(edtYear, edtMonth);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void errorCheck(EditText edtYear, EditText edtMonth) {
        if (edtYear.getText().toString().equals("") || edtMonth.getText().toString().equals("")) return;
        int month = Integer.parseInt(edtMonth.getText().toString());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));
        cal.setTime(new Date());
        int currentYear = cal.get(Calendar.YEAR);
        int year = Integer.parseInt(edtYear.getText().toString());
        if (year < currentYear || month > 12){
            errorText.setText("Год или месяц указаны не верно");
        }
        else{
            errorText.setText("");
        }
    }
}