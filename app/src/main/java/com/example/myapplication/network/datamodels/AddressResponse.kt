package com.example.myapplication.network.datamodels

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("street")
    val street: String,
    @SerializedName("suite")
    val suite: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("zipcode")
    val zipcode: String,
    @SerializedName("geo")
    val geoResponse: GeoResponse
)