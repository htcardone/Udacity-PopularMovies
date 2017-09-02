package com.htcardone.popularmovies.data.remote;

import android.support.annotation.NonNull;

import com.htcardone.popularmovies.BuildConfig;
import com.htcardone.popularmovies.data.MoviesDataSource;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@SuppressWarnings("CanBeFinal")
public class MoviesRemoteDataSource implements MoviesDataSource {

    private static MoviesRemoteDataSource INSTANCE;
    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private final TmdbHttpApi tmdbHttpApi;
    private String language;

    private MoviesRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbHttpApi = retrofit.create(TmdbHttpApi.class);
        language = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
    }

    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getMovies(final int sortType, @NonNull final LoadMoviesCallback callback) {
        String sort = "";
        if (sortType == TYPE_SORT_BY_POPULAR) {
            sort = "popular";
        } else if (sortType == TYPE_SORT_BY_TOP_RATED) {
            sort = "top_rated";
        }

        tmdbHttpApi.getMovies(sort, API_KEY, language).enqueue(new Callback<PopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<PopularResponse> call, @NonNull Response<PopularResponse> response) {
                if(response.isSuccessful()) {
                    //noinspection ConstantConditions
                    callback.onMoviesLoaded(response.body().getResults(), sortType);
                } else {
                    callback.onDataNotAvailable();
                    //TODO handle response status code
                }
            }

            @Override
            public void onFailure(@NonNull Call<PopularResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    private interface TmdbHttpApi {
        @GET("movie/{sort}?api_key=" + API_KEY)
        Call<PopularResponse> getMovies(
                @Path("sort") String sort,
                @Query("api_key") String apiKey,
                @Query("language") String language);
    }
}
