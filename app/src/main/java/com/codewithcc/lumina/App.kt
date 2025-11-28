package com.codewithcc.lumina

import android.app.Application
import androidx.room.Room
import com.codewithcc.lumina.model.db.TodoDatabase

class App : Application() {
    companion object {
        lateinit var todoDatabase: TodoDatabase
    }

    override fun onCreate() {
        super.onCreate()
        todoDatabase = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.NAME
        ).build()
    }
}