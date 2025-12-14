package com.example.wateroo_app

import androidx.room.PrimaryKey

data class DrinkItem(
    val name: String,
    var amount: Int,
    val composition: Map<String, Double>
)
