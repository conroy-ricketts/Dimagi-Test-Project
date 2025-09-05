package com.example.myapplication.storage

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val city: String,
    val company: String
)

@Dao
interface FakeUsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("""
        SELECT user.* 
        FROM UserEntity user
        WHERE user.username = :username
    """)
    suspend fun queryDatabaseForFakeUser(username: String): UserEntity
}

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun fakeUsersDao(): FakeUsersDao

    companion object {
        private var database: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return database ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "fake_users_database"
            ).build().also {
                database = it
            }
        }
    }
}