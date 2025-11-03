package com.example.tiptime.data

import kotlinx.coroutines.flow.Flow

class TipRepository(private val tipDao: TipDao) {

    fun getAllTips(): Flow<List<Tip>> = tipDao.getAllTips()

    fun getTipById(id: Int): Flow<Tip?> = tipDao.getTipById(id)

    suspend fun insertTip(tip: Tip) = tipDao.insertTip(tip)

    suspend fun updateTip(tip: Tip) = tipDao.updateTip(tip)

    suspend fun deleteTip(tip: Tip) = tipDao.deleteTip(tip)
}