package com.example.wateroo_app

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var login: String,
    var password: String,
    var email: String,
    var drink_amount: Int
)