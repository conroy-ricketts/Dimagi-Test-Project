package com.example.myapplication.network.datamodels

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("catchPhrase")
    val catchPhrase: String,
    @SerializedName("bs")
    val bs: String
)