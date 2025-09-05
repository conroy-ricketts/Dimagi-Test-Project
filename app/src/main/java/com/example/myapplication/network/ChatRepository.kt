package com.example.myapplication.network

import android.content.Context
import com.example.myapplication.network.datamodels.UserResponse
import com.example.myapplication.storage.AppDatabase
import com.example.myapplication.storage.UserEntity
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

    suspend fun storeFakeUserData(context: Context, fakeUserData: List<UserResponse>) {
        val userEntities = fakeUserData.map {
            UserEntity(
                id = it.id,
                name = it.name,
                username = it.username,
                email = it.email,
                city = it.addressResponse.city,
                company = it.companyResponse.name
            )
        }

        AppDatabase.getDatabase(context).fakeUsersDao().insertUsers(userEntities)
    }

    suspend fun queryDatabaseForFakeUser(context: Context, username: String): UserEntity {
        return AppDatabase.getDatabase(context).fakeUsersDao().queryDatabaseForFakeUser(username)
    }

    companion object Constants {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}