package com.example.tiptime.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Query("SELECT * FROM tips ORDER BY id ASC")
    fun getAllTips(): Flow<List<Tip>>

    @Query("SELECT * FROM tips WHERE id = :id")
    fun getTipById(id: Int): Flow<Tip?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTip(tip: Tip)

    @Update
    suspend fun updateTip(tip: Tip)

    @Delete
    suspend fun deleteTip(tip: Tip)
}