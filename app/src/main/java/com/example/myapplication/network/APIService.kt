package com.example.myapplication.network

import com.example.myapplication.network.datamodels.UserResponse
import retrofit2.http.GET

interface APIService {
    @GET("users")
    suspend fun getFakeUsers(): List<UserResponse>
}