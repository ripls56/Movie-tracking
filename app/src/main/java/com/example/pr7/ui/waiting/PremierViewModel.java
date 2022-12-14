package com.example.pr7.ui.waiting;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr7.MainActivity;
import com.example.pr7.R;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.waiting.models.Item;
import com.example.pr7.ui.waiting.models.Month;
import com.example.pr7.ui.waiting.models.Premier;
import com.example.pr7.ui.waiting.recycler.PremierAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PremierViewModel extends ViewModel {
    private final ApiInterface apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
    private static PremierViewModel instance;
    private ArrayList<String> monthArrayList;
    DatePickerFragment datePickerFragment;

    public static synchronized PremierViewModel getInstance() {
        if (instance == null) {
            instance = new PremierViewModel();
        }
        return instance;
    }
    public void getAwaitFilms(RecyclerView premierRecycler, int year, int month, Context context, Fragment fragment) {
        monthArrayList = Month.getMonthList();
        datePickerFragment = new DatePickerFragment();
        Call<Premier> getPremierFilms = apiInterface.getAwaitFilms(year, monthArrayList.get(month - 1));
        getPremierFilms.enqueue(new Callback<Premier>() {
            @Override
            public void onResponse(@NonNull Call<Premier> call, @NonNull Response<Premier> response) {
                if(response.isSuccessful()){
                    ArrayList<Item> items = response.body().getItems();
                    PremierAdapter adapter = new PremierAdapter(context, items);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    premierRecycler.setLayoutManager(linearLayoutManager);
                    premierRecycler.setAdapter(adapter);
                    premierRecycler.setVisibility(View.VISIBLE);
                    MainActivity.isIndeterminate.set(false);
                }else{
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    fragment.getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
                    MainActivity.isIndeterminate.set(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Premier> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                fragment.getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
                MainActivity.isIndeterminate.set(false);
            }
        });
    }

}
