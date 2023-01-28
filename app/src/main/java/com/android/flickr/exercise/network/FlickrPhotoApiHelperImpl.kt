package com.android.flickr.exercise.network

import javax.inject.Inject

class FlickrPhotoApiHelperImpl @Inject constructor(
    private val apiService: FlickrPhotoApiService
) : FlickrPhotoApiHelper {
    override suspend fun flickrPhotosList(tags: String) = apiService.flickrPhotosList(tags)
}