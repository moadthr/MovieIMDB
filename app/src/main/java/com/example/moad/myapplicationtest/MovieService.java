package com.example.moad.myapplicationtest;

import com.example.moad.myapplicationtest.model.PopularTvShows;
import com.example.moad.myapplicationtest.model.SearchResult;
import com.example.moad.myapplicationtest.model.TopRated;
import com.example.moad.myapplicationtest.model.TopRatedMovies;
import com.example.moad.myapplicationtest.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;

/**
 * Created by moad on 11/11/2017.
 */

public interface MovieService {

    @GET("movie/top_rated")
    Call<TopRatedMovies> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );
    @GET("tv/popular")
    Call<PopularTvShows> getTvSeries(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );
    @GET("search/movie")
    Call<SearchResult> searchMovie(@Query("query") String query,
                                   @Query("api_key") String apiKey,
                                   @Query("language") String language,
                                   @Query("page") int pageIndex);
    @GET("movie/{movie_id}/videos")
    Call<Videos> getVideo(@Path("movie_id") int movie_id,
                          @Query("api_key") String apiKey,
                          @Query("language") String language,
                          @Query("page") int pageIndex);

    @GET("tv/{tv_id}/videos")
     Call<Videos> getVideoTvshow(@Path("tv_id") int tv_id,
                          @Query("api_key") String apiKey,
                          @Query("language") String language,
                          @Query("page") int pageIndex);


}

