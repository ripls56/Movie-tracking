package com.example.pr7.repository;

import com.example.pr7.ui.filmdetail.models.filmdetailmodel.FilmDetailModel;
import com.example.pr7.ui.filmdetail.models.videomodel.FilmVideos;
import com.example.pr7.ui.top.models.Top;
import com.example.pr7.ui.waiting.models.Premier;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("X-API-KEY: d5f7d477-adb4-406a-9fb0-079906050c15")
    @GET("top")
    Call<Top> getTopFilms(@Query("page") int page);


    @Headers("X-API-KEY: d5f7d477-adb4-406a-9fb0-079906050c15")
    @GET("premieres")
    Call<Premier> getAwaitFilms(@Query("year") int year, @Query("month") String month);

    @Headers("X-API-KEY: d5f7d477-adb4-406a-9fb0-079906050c15")
    @GET("{id}")
    Call<FilmDetailModel> getFilmByID(@Path("id") int filmId);

    @Headers("X-API-KEY: d5f7d477-adb4-406a-9fb0-079906050c15")
    @GET("{id}/videos")
    Call<FilmVideos> getVideosByFilmID(@Path("id") int filmId);


}
