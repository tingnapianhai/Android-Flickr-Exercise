package com.android.flickr.exercise.network

import com.android.flickr.exercise.data.FlickrPhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrPhotoApiService {

    @GET("rest/")
    suspend fun flickrPhotosList(
        @Query("tags") tags: String,
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") api_key: String = "5a2cc90782760b3a6b3eca570dfaf5c3", //todo move to gradle.properties
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: String = "1",
    ): Response<FlickrPhotosResponse>
}

