package com.android.flickr.exercise

import com.android.flickr.exercise.data.FlickrPhoto
import com.android.flickr.exercise.data.getPhotoUrl
import org.junit.Assert
import org.junit.Test

class FlickrPhotoEntityTest {

    @Test
    fun `getPhotoUrl - Url is correct`() {
        // given
        val photo = FlickrPhoto(
            id = "a",
            secret = "b",
            server = "c",
            title = "d",
        )

        // when
        val url = photo.getPhotoUrl()

        // then
        Assert.assertEquals("https://live.staticflickr.com/c/a_b.jpg", url)
    }
}