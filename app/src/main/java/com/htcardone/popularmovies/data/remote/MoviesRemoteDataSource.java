package com.htcardone.popularmovies.data.remote;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.BuildConfig;
import com.htcardone.popularmovies.data.MoviesDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by henrique.cardone on 30/08/2017.
 */

public class MoviesRemoteDataSource implements MoviesDataSource {

    private static MoviesRemoteDataSource INSTANCE;
    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private final TmdbHttpApi tmdbHttpApi;

    private MoviesRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbHttpApi = retrofit.create(TmdbHttpApi.class);
    }

    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getPopularMovies(@NonNull final LoadMoviesCallback callback) {
        tmdbHttpApi.getPopularMovies(API_KEY).enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(Call<PopularResponse> call, Response<PopularResponse> response) {
                if(response.isSuccessful()) {
                    callback.onMoviesLoaded(response.body().getResults());
                } else {
                    callback.onDataNotAvailable();
                    //TODO handle response status code
                }
            }

            @Override
            public void onFailure(Call<PopularResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTopRatedMovies(@NonNull final LoadMoviesCallback callback) {
        tmdbHttpApi.getTopRatedMovies(API_KEY).enqueue(new Callback<TopRatedResponse>() {
            @Override
            public void onResponse(Call<TopRatedResponse> call, Response<TopRatedResponse> response) {
                if (response.isSuccessful()) {
                    callback.onMoviesLoaded(response.body().getResults());
                } else {
                    callback.onDataNotAvailable();
                    //TODO handle response status code
                }
            }

            @Override
            public void onFailure(Call<TopRatedResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    private interface TmdbHttpApi {
        @GET("movie/popular?api_key=" + API_KEY)
        Call<PopularResponse> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/top_rated?api_key=" + API_KEY)
        Call<TopRatedResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    }
}
