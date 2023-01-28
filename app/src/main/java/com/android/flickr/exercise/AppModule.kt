package com.android.flickr.exercise

import android.app.Application
import android.content.Context
import com.android.flickr.exercise.network.FlickrPhotoApiHelper
import com.android.flickr.exercise.network.FlickrPhotoApiHelperImpl
import com.android.flickr.exercise.network.FlickrPhotoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient().newBuilder().build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .baseUrl(baseUrl) //todo
            .build()

    @Provides
    @Singleton
    fun providePhotoListApiService(retrofit: Retrofit): FlickrPhotoApiService =
        retrofit.create(FlickrPhotoApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: FlickrPhotoApiHelperImpl): FlickrPhotoApiHelper = apiHelper

    private const val BASE_URL = "https://api.flickr.com/services/"
}