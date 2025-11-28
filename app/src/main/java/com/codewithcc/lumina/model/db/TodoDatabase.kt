package com.codewithcc.lumina.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codewithcc.lumina.model.Converters

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    companion object {
        const val NAME = "todo-db"
    }

    abstract fun getTodoDao(): TodoDao
}