package com.example.pr7.ui.filmdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.pr7.R;
import com.example.pr7.databinding.ActivityFilmDetailBinding;
import com.example.pr7.databinding.ActivityMainBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.filmdetail.models.FilmDetailModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetail extends AppCompatActivity {

    ActivityFilmDetailBinding binding;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilmDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        int filmId = bundle.getInt("filmId");
        apiInterface = RepositoryBuilder.buildRequest().create(ApiInterface.class);

        Call<FilmDetailModel> getFilmById = apiInterface.getFilmByID(filmId);


        getFilmById.enqueue(new Callback<FilmDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<FilmDetailModel> call, @NonNull Response<FilmDetailModel> response) {
                FilmDetailModel filmDetailModel = response.body();
                Picasso.get().load(filmDetailModel.getCoverUrl()).into(binding.filmDetailImg);
                binding.toolbar.setTitle(filmDetailModel.getNameRu());
                binding.filmDetailDescription.setText(filmDetailModel.getDescription());
                Uri uri = Uri.parse("https://www.youtube.com/v/NRT11KuW6L0");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }

            @Override
            public void onFailure(@NonNull Call<FilmDetailModel> call, @NonNull Throwable t) {

            }
        });
    }
}