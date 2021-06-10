package com.kirkyweather.android.logic.model

import com.google.gson.annotations.SerializedName

// 腾讯api服务
data class PlaceResponse(val status: Int,
                         @SerializedName("data") val places: List<Place>)

data class Place(@SerializedName("title") val name: String,
                 val address: String,
                 val location: Location,)

data class Location(val lng: String, val lat: String)
