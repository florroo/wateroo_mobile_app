package com.example.wateroo_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wateroo_app.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    companion object {
        private var INSTANCE: UserDatabase? = null
        const val NAME = "users"

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


    abstract fun getUserDao(): UserDao
}