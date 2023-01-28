package com.android.flickr.exercise.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlickrPhotoItem(
    val url: String,
    val title: String,
) : Parcelable

@Keep
data class FlickrPhotosResponse(
    @Json(name = "photos") val photos: FlickrPhotoList? = null,
    @Json(name = "stat") val stat: String? = null
)

@Keep
data class FlickrPhotoList(
    @Json(name = "photo") val photo: List<FlickrPhoto>,
)

@Keep
data class FlickrPhoto(
    @Json(name = "id") val id: String,
    @Json(name = "secret") val secret: String,
    @Json(name = "server") val server: String,
    @Json(name = "title") val title: String,
)

fun FlickrPhoto.toFlickrPhotoItem(): FlickrPhotoItem = FlickrPhotoItem(this.getPhotoUrl(), title)

@VisibleForTesting
fun FlickrPhoto.getPhotoUrl(): String {
    val symbol = "_"
    return "https://live.staticflickr.com/$server/$id${symbol}$secret.jpg"
}
