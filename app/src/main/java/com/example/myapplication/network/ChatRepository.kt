package com.example.myapplication.network

import com.example.myapplication.network.datamodels.UserResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatRepository {
    private val apiService: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    suspend fun fetchFakeUsers(): List<UserResponse> {
        return apiService.getFakeUsers()
    }

    companion object Constants {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}