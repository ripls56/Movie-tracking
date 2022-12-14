package com.example.pr7;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;

import com.example.pr7.databinding.ActivityMainBinding;
import com.example.pr7.ui.waiting.DatePickerFragment;
import com.example.pr7.ui.top.TopFragment;
import com.example.pr7.ui.waiting.DateViewModel;
import com.example.pr7.ui.waiting.PremierFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static ObservableField<Boolean> isIndeterminate = new ObservableField<>();
    DateViewModel dateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        dateViewModel = DateViewModel.getInstance();
        setContentView(binding.getRoot());
        TopFragment topFragment = new TopFragment();
        PremierFragment premierFragment = new PremierFragment();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.top);
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_activity_main, topFragment).commit();
        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.premier:
                    if (dateViewModel.isPremierFragment()){
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, premierFragment).commit();
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, datePickerFragment).commit();
                    }
                    break;
                case R.id.top:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, topFragment).commit();
                    break;
            }
            return true;
        });
    }

}