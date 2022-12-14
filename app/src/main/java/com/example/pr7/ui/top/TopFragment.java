package com.example.pr7.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pr7.MainActivity;
import com.example.pr7.databinding.FragmentTopBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.top.models.Film;
import com.example.pr7.ui.top.models.Top;
import com.example.pr7.ui.top.recycler.TopAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopFragment extends Fragment {

    private FragmentTopBinding binding;
    private ApiInterface apiInterface;
    RecyclerView filmRecycler;
    private int page = 1;
    ArrayList<Film> films;
    TopAdapter adapter;
    boolean isUpdate = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTopBinding.inflate(inflater, container, false);
        apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
        MainActivity.isIndeterminate.set(true);
        filmRecycler = binding.topRecycler;
        page = 1;
        Call<Top> getTopFilms = apiInterface.getTopFilms(page);
        getTopFilms.enqueue(new Callback<Top>() {
            @Override
            public void onResponse(@NonNull Call<Top> call, @NonNull Response<Top> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    films = response.body().getFilms();
                    adapter = new TopAdapter(getContext(), films);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    filmRecycler.setLayoutManager(linearLayoutManager);
                    filmRecycler.setAdapter(adapter);
                    MainActivity.isIndeterminate.set(false);
                }
                else
                {
                    Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    MainActivity.isIndeterminate.set(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Top> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        filmRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    page++;
                    Call<Top> getTop = apiInterface.getTopFilms(page);
                    getTop.enqueue(new Callback<Top>() {
                        @Override
                        public void onResponse(@NonNull Call<Top> call, @NonNull Response<Top> response) {
                            if (!isUpdate && response.body() != null && response.isSuccessful()){
                                isUpdate = true;
                                films.addAll(response.body().getFilms());
                                adapter.notifyItemRangeChanged(adapter.getItemCount(), films.size());
                                isUpdate = false;
                                System.gc();
                            }
                            if (!response.isSuccessful()){
                                Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Top> call, @NonNull Throwable t) {
                            Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}