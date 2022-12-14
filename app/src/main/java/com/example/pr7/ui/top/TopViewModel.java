package com.example.pr7.ui.top;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr7.MainActivity;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.top.models.Film;
import com.example.pr7.ui.top.models.Top;
import com.example.pr7.ui.top.recycler.TopAdapter;
import com.example.pr7.ui.waiting.DateViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopViewModel extends ViewModel {
    private TopAdapter adapter;
    private final ApiInterface apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
    private ArrayList<Film> films;
    private static TopViewModel instance;
    private TopViewModel(){}

    public static synchronized TopViewModel getInstance() {
        if (instance == null) {
            instance = new TopViewModel();
        }
        return instance;
    }

    public void getTopList(int page, Context context, RecyclerView filmRecycler){
        Call<Top> getTopFilms = apiInterface.getTopFilms(page);
        getTopFilms.enqueue(new Callback<Top>() {
            @Override
            public void onResponse(@NonNull Call<Top> call, @NonNull Response<Top> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    films = response.body().getFilms();
                    adapter = new TopAdapter(context, films);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    filmRecycler.setLayoutManager(linearLayoutManager);
                    filmRecycler.setAdapter(adapter);
                    MainActivity.isIndeterminate.set(false);
                }
                else
                {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    MainActivity.isIndeterminate.set(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Top> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<Film> getFilms() {
        return films;
    }

    public TopAdapter getAdapter() {
        return adapter;
    }
}
