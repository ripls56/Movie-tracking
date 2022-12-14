package com.example.pr7.ui.waiting;

import android.os.Bundle;
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
import com.example.pr7.MainActivity;
import com.example.pr7.R;
import com.example.pr7.databinding.FragmentPremierBinding;
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

public class PremierFragment extends Fragment {

    private ApiInterface apiInterface;
    private FragmentPremierBinding binding;
    DatePickerFragment datePickerFragment;
    DateViewModel dateViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateViewModel = DateViewModel.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        int year = dateViewModel.getYear();
        int month = dateViewModel.getMonth();

        binding = FragmentPremierBinding.inflate(inflater, container, false);
        apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);
        Month Month = new Month();
        ArrayList<String> monthArrayList = Month.getMonthList();
        Button btnDateChange = binding.premierDateChange;
        RecyclerView premierRecycler = binding.premierRecycler;
        datePickerFragment = new DatePickerFragment();
        getAwaitFilms(premierRecycler, year, month, monthArrayList);
        btnDateChange.setOnClickListener(view -> {
            dateViewModel.changeDate(this);
        });
        return binding.getRoot();
    }

    private void getAwaitFilms(RecyclerView premierRecycler, int year, int month, ArrayList<String> monthArrayList) {
        Call<Premier> getPremierFilms = apiInterface.getAwaitFilms(year, monthArrayList.get(month - 1));
        getPremierFilms.enqueue(new Callback<Premier>() {
            @Override
            public void onResponse(@NonNull Call<Premier> call, @NonNull Response<Premier> response) {
                if(response.isSuccessful()){
                    ArrayList<Item> items = response.body().getItems();
                    PremierAdapter adapter = new PremierAdapter(requireContext(), items);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    premierRecycler.setLayoutManager(linearLayoutManager);
                    premierRecycler.setAdapter(adapter);
                    premierRecycler.setVisibility(View.VISIBLE);
                    MainActivity.isIndeterminate.set(false);
                }else{
                    Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
                    MainActivity.isIndeterminate.set(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Premier> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
                MainActivity.isIndeterminate.set(false);
            }
        });
    }
}