package com.example.myapplication.network.datamodels

import com.google.gson.annotations.SerializedName

data class GeoResponse(
    @SerializedName("lat")
    val latitude: String,
    @SerializedName("lng")
    val longitude: String
)