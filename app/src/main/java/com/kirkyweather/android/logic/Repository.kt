package com.kirkyweather.android.logic

import androidx.lifecycle.liveData
import com.kirkyweather.android.logic.model.Place
import com.kirkyweather.android.logic.network.KirkyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = KirkyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == 0) {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result) // 抛出异常
    }

}