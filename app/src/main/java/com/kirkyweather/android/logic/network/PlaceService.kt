package com.kirkyweather.android.logic.network

import com.kirkyweather.android.KirkyWeatherApplication
import com.kirkyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("ws/place/v1/suggestion/?region=湖南&key=${KirkyWeatherApplication.QQ_TOKEN}")
    fun searchPlaces(@Query("keyword") keyword: String): Call<PlaceResponse>
}