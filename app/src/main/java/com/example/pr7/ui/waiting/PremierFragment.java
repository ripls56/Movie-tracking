package com.example.pr7.ui.waiting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr7.MainActivity;
import com.example.pr7.databinding.FragmentPremierBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
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

public class PremierFragment extends Fragment {

    private ApiInterface apiInterface;
    private FragmentPremierBinding binding;
    private boolean isDate = true;
    TextView errorText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPremierBinding.inflate(inflater, container, false);
        apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
        EditText edtYear = binding.premierYear;
        EditText edtMonth = binding.premierMonth;
        Button btnDateSet = binding.premierDateSet;
        Button btnDateChange = binding.premierDateChange;
        RecyclerView premierRecycler = binding.premierRecycler;
        errorText = binding.premierError;
        AtomicInteger year = new AtomicInteger(1);
        AtomicInteger month = new AtomicInteger(1);
        Month Month = new Month();
        ArrayList<String> monthArrayList = Month.getMonthList();

        btnDateChange.setOnClickListener(view -> {
            isDate = true;
            binding.premierDateContainer.setVisibility(View.VISIBLE);
            binding.premierDateChange.setVisibility(View.INVISIBLE);
            premierRecycler.setVisibility(View.INVISIBLE);
        });
        if (isDate) {
            btnDateSet.setOnClickListener(view -> {
                if (errorText.getText().toString().equals("") && !edtYear.getText().toString().equals("") && !edtMonth.getText().toString().equals("")){
                    MainActivity.isIndeterminate.set(true);
                    year.set(Integer.parseInt(edtYear.getText().toString()));
                    month.set(Integer.parseInt(edtMonth.getText().toString()));
                    getAwaitFilms(premierRecycler, year.get(), month.get(), monthArrayList);
                }
            });
            edtMonthTextChangedListener(edtYear, edtMonth);
            edtYearTextChangedListener(edtYear, edtMonth);
        }

        return binding.getRoot();
    }

    private void getAwaitFilms(RecyclerView premierRecycler, int year, int month, ArrayList<String> monthArrayList) {
        Call<Premier> getPremierFilms = apiInterface.getAwaitFilms(year, monthArrayList.get(month - 1));
        getPremierFilms.enqueue(new Callback<Premier>() {
            @Override
            public void onResponse(@NonNull Call<Premier> call, @NonNull Response<Premier> response) {
                if(response.isSuccessful()){
                    ArrayList<Item> items = response.body().getItems();
                    binding.premierDateContainer.setVisibility(View.INVISIBLE);
                    isDate = false;
                    PremierAdapter adapter = new PremierAdapter(requireContext(), items);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    premierRecycler.setLayoutManager(linearLayoutManager);
                    premierRecycler.setAdapter(adapter);
                    premierRecycler.setVisibility(View.VISIBLE);
                    binding.premierDateChange.setVisibility(View.VISIBLE);
                    MainActivity.isIndeterminate.set(false);
                }else{
                    Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    MainActivity.isIndeterminate.set(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Premier> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                MainActivity.isIndeterminate.set(false);
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}