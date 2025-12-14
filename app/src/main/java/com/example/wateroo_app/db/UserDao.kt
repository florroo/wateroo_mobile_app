package com.example.wateroo_app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wateroo_app.User

@Dao
interface UserDao {

    @Query("select * from User where login =:login")
    fun getUserLogin(login: String): User?

    @Query("select * from User where email =:email")
    fun getUserEmail(email: String): User?

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}