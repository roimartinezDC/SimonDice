package com.example.simondice

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Record::class],
    version = 1
)
abstract class RecordDB : RoomDatabase() {
    abstract fun recordDao(): RecordDAO
}