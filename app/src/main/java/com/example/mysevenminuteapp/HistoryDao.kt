package com.example.mysevenminuteapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllHistories(): Flow<List<HistoryEntity>>

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)
}