package com.example.pr7.ui.waiting;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pr7.MainActivity;
import com.example.pr7.R;
import com.example.pr7.databinding.FragmentPremierBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.waiting.models.Item;
import com.example.pr7.ui.waiting.models.Month;
import com.example.pr7.ui.waiting.models.Premier;
import com.example.pr7.ui.waiting.recycler.PremierAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PremierFragment extends Fragment {
    private ApiInterface apiInterface;
    private FragmentPremierBinding binding;
    DateViewModel dateViewModel;
    PremierViewModel premierViewModel;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateViewModel = DateViewModel.getInstance();
        premierViewModel = PremierViewModel.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int year = dateViewModel.getYear();
        int month = dateViewModel.getMonth();

        binding = FragmentPremierBinding.inflate(inflater, container, false);
        apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
        swipeRefreshLayout = binding.premierSwipeRefresh;
        ArrayList<String> monthArrayList = Month.getMonthList();
        FloatingActionButton btnDateChange = binding.premierDateChange;
        RecyclerView premierRecycler = binding.premierRecycler;
        btnDateChange.setOnClickListener(view -> {
            dateViewModel.changeDate(this);
        });
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            premierViewModel.getAwaitFilms(premierRecycler, year, month, getContext(), this);
            swipeRefreshLayout.setRefreshing(false);
        });
        premierViewModel.getAwaitFilms(premierRecycler, year, month, getContext(), this);
        return binding.getRoot();
    }

}