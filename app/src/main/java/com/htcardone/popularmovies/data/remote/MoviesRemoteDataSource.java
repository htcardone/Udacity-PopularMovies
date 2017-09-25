package com.htcardone.popularmovies.data.remote;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.htcardone.popularmovies.BuildConfig;
import com.htcardone.popularmovies.data.MoviesDataSource;

import java.util.Locale;

import okhttp3.OkHttpClient;
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .client(okHttpClient)
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

    @Override
    public void getVideos(int movieId, @NonNull final LoadVideosCallback callback) {
        tmdbHttpApi.getVideos(movieId, API_KEY, language).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if(response.isSuccessful()) {
                    //noinspection ConstantConditions
                    callback.onVideosLoaded(response.body().getResults());
                } else {
                    callback.onDataNotAvailable();
                    //TODO handle response status code
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getReviews(int movieId, @NonNull final LoadReviewsCallback callback) {
        tmdbHttpApi.getReviews(movieId, API_KEY, language).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if(response.isSuccessful()) {
                    //noinspection ConstantConditions
                    callback.onReviewsLoaded(response.body().getResults());
                } else {
                    callback.onDataNotAvailable();
                    //TODO handle response status code
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    private interface TmdbHttpApi {
        @GET("movie/{sort}")
        Call<PopularResponse> getMovies(
                @Path("sort") String sort,
                @Query("api_key") String apiKey,
                @Query("language") String language);

        @GET("movie/{movieId}/videos")
        Call<VideosResponse> getVideos(
                @Path("movieId") int movieId,
                @Query("api_key") String apiKey,
                @Query("language") String language);

        @GET("movie/{movieId}/reviews")
        Call<ReviewsResponse> getReviews(
                @Path("movieId") int movieId,
                @Query("api_key") String apiKey,
                @Query("language") String language);
    }
}
