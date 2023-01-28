package com.android.flickr.exercise.network

import com.android.flickr.exercise.data.FlickrPhotosResponse
import retrofit2.Response

interface FlickrPhotoApiHelper {
    suspend fun flickrPhotosList(tags: String): Response<FlickrPhotosResponse>
}