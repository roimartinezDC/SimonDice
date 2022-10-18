package com.example.simondice

import androidx.room.*

@Entity
data class Record (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val puntuacion : Int
    )

@Dao
interface RecordDAO {
    @Query("SELECT * from Record")
    suspend fun getAll(): List<Record>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: List<Record>)

    @Update
    suspend fun update(record: Record)
}