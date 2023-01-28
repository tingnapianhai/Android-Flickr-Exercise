package com.android.flickr.exercise.data

import android.util.Log
import com.android.flickr.exercise.ui.MainViewModel
import com.android.flickr.exercise.network.FlickrPhotoApiHelper
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.Throwable

class FlickrPhotoRepo @Inject constructor(
    private val apiHelper: FlickrPhotoApiHelper,
) {
    suspend fun getFlickrPhotoList(searchQuery: String): MainViewModel.State {
        return try {
            val response = apiHelper.flickrPhotosList(searchQuery)
            if (response.isSuccessful && response.body() != null && response.body() is FlickrPhotosResponse) {
                val flickrPhotoItemList =
                    response.body()!!.photos!!.photo.map { it.toFlickrPhotoItem() }
                MainViewModel.State.Result.Success(flickrPhotoItemList)
            } else {
                MainViewModel.State.Result.Error(
                    ERROR_UNEXPECTED_ERROR
                )
            }
        } catch (e: UnknownHostException) {
            Log.e(TAG, "UnknownHostException", e)
            MainViewModel.State.Result.Error(ERROR_NO_NETWORK_ERROR)
        } catch (e: ConnectException) {
            Log.e(TAG, "ConnectException", e)
            MainViewModel.State.Result.Error(ERROR_NETWORK)
        } catch (e: Throwable) {
            Log.e(TAG, "Exception", e)
            MainViewModel.State.Result.Error(ERROR_UNEXPECTED_ERROR)
        }
    }

    companion object {
        private const val TAG = "PhotoRepo"
        private const val ERROR_UNEXPECTED_ERROR = "Unexpected Error.\nPlease retry!"
        private const val ERROR_NO_NETWORK_ERROR =
            "Network not available.\nPlease check network and retry!"
        private const val ERROR_NETWORK = "Network Error.\nPlease check network and retry!"
    }
}