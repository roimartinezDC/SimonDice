package com.example.simondice

import androidx.room.*

@Entity
data class Record (
    @PrimaryKey(autoGenerate = true)
    val id : Int,

    @ColumnInfo(name = "puntuacion")
    val puntuacion : Int

    )

@Dao
interface RecordDAO {
    @Query("SELECT puntuacion FROM Record WHERE id = 1")
    suspend fun getPuntuacion(): Int

    @Query("INSERT INTO Record (puntuacion) VALUES (0)")
    suspend fun crearPuntuacion()

    /*
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: List<Record>)
    */

    @Update
    suspend fun update(record: Record)
}