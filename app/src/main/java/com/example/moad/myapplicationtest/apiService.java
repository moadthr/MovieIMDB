package com.example.moad.myapplicationtest;

import com.example.moad.myapplicationtest.model.Movie;
import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.TopRated;
import com.squareup.okhttp.Call;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by moad on 11/11/2017.
 */

public interface apiService {
    @GET("/genre/{genre_id}/movies")
    void getMoviesGenre(@Path("genre_id")String genre_id, Callback<List<Movie>> callback);

    @GET("/movie/top_rated?api_key="+ApiKey.key)
    void getTopRated(Callback<TopRated> callback);
}

