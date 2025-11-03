package com.example.tiptime.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips")
data class Tip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val billAmount: Double,
    val tipPercentage: Int,
    val roundUp: Boolean,
)