package com.example.swipeassignment.di

import com.example.swipeassignment.api.ProductApi
import com.example.swipeassignment.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    val gson = GsonBuilder()
        .setLenient()
        .create()


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit:Retrofit.Builder):ProductApi{
       return retrofit.build().create(ProductApi::class.java)
    }
}