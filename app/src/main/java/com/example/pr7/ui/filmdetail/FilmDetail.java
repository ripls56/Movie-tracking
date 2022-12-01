package com.example.pr7.ui.filmdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pr7.databinding.ActivityFilmDetailBinding;
import com.example.pr7.repository.ApiInterface;
import com.example.pr7.repository.RepositoryBuilder;
import com.example.pr7.ui.filmdetail.models.filmdetailmodel.FilmDetailModel;
import com.example.pr7.ui.filmdetail.models.videomodel.FilmVideos;
import com.example.pr7.ui.filmdetail.models.videomodel.Item;
import com.example.pr7.utils.Utils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.internal.Util;
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

        getFilmsVideo(filmId);
        getFilmById(filmId);
    }

    private void getFilmById(int filmId){
        Call<FilmDetailModel> getFilmById = apiInterface.getFilmByID(filmId);
        getFilmById.enqueue(new Callback<FilmDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<FilmDetailModel> call, @NonNull Response<FilmDetailModel> response) {
                if (response.isSuccessful())
                {
                    FilmDetailModel filmDetailModel = response.body();
                    if (filmDetailModel == null) {
                        return;
                    }
                    Picasso.get().load(filmDetailModel.getCoverUrl()).into(binding.filmDetailImg);
                    Picasso.get().load(filmDetailModel.getPosterUrlPreview()).into(binding.filmDetailPoster);
                    binding.toolbar.setTitle(filmDetailModel.getNameRu());
                    binding.filmDetailShortDescription.setText(filmDetailModel.getShortDescription());
                    binding.filmDetailRatingKinopoisk.setText(String.format("Рейтинг на кинопоиске: %s", filmDetailModel.getRatingKinopoisk()));
                    binding.filmDetailRatingIMDB.setText(String.format("Рейтинг на IMDB: %s", filmDetailModel.getRatingImdb()));
                    binding.filmDetailDescription.setText(filmDetailModel.getDescription());
                }
                else
                {
                    if (response.errorBody() != null) {
                        Toast.makeText(getBaseContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilmDetailModel> call, @NonNull Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilmsVideo(int filmId){
        Call<FilmVideos> getVideosByFilmId = apiInterface.getVideosByFilmID(filmId);
        getVideosByFilmId.enqueue(new Callback<FilmVideos>() {
            @Override
            public void onResponse(@NonNull Call<FilmVideos> call, @NonNull Response<FilmVideos> response) {
                if (response.isSuccessful())
                {
                    if (response.body() == null) {
                        return;
                    }
                    ArrayList<Item> items = response.body().getItems();
                    Item item = Utils.getYoutubeItemFromArray(items);
                    getLifecycle().addObserver(binding.filmDetailTrailer);
                    binding.filmDetailTrailer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            super.onReady(youTubePlayer);
                            if (item != null && Utils.getIdFromUrl(item.getUrl()) != null) {
                                String id = Utils.getIdFromUrl(item.getUrl());
                                youTubePlayer.cueVideo(id, 0);
                            }
                            else
                            {
                                String id = Utils.getIdFromUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
                                youTubePlayer.loadVideo(id, 0);
                            }
                        }
                    });
                }
                else
                {
                    if (response.errorBody() != null) {
                        Toast.makeText(getBaseContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilmVideos> call, @NonNull Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.filmDetailTrailer.release();
    }
}