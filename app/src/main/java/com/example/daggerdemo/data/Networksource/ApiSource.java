package com.example.daggerdemo.data.Networksource;

import com.example.daggerdemo.data.Networkmodels.AlbumPhotos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSource {
    @GET("photos")
    Observable<List<AlbumPhotos>> getPhotos(@Query("albumId") Integer id);

}
