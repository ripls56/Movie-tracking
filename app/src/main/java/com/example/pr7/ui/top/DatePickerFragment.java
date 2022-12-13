package com.example.pr7.ui.top;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr7.MainActivity;
import com.example.pr7.R;
import com.example.pr7.databinding.FragmentDatePickerBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.waiting.PremierFragment;
import com.example.pr7.ui.waiting.models.Item;
import com.example.pr7.ui.waiting.models.Month;
import com.example.pr7.ui.waiting.models.Premier;
import com.example.pr7.ui.waiting.recycler.PremierAdapter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatePickerFragment extends Fragment {

    private FragmentDatePickerBinding binding;
    private boolean isDate = true;
    TextView errorText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDatePickerBinding.inflate(inflater, container, false);
        EditText edtYear = binding.premierYear;
        EditText edtMonth = binding.premierMonth;
        Button btnDateSet = binding.premierDateSet;
        errorText = binding.premierError;
        AtomicInteger year = new AtomicInteger(1);
        AtomicInteger month = new AtomicInteger(1);

        if (isDate) {
            btnDateSet.setOnClickListener(view -> {
                if (errorText.getText().toString().equals("") && !edtYear.getText().toString().equals("") && !edtMonth.getText().toString().equals("")){
                    MainActivity.isIndeterminate.set(true);
                    year.set(Integer.parseInt(edtYear.getText().toString()));
                    month.set(Integer.parseInt(edtMonth.getText().toString()));
                    PremierFragment premierFragment = new PremierFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("year", year.intValue());
                    bundle.putInt("month", month.intValue());
                    premierFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, premierFragment).commit();
                }
            });
            edtMonthTextChangedListener(edtYear, edtMonth);
            edtYearTextChangedListener(edtYear, edtMonth);
        }
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