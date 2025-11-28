package com.codewithcc.lumina.model

import androidx.room.TypeConverter

enum class TodoPriority {
    Low, Medium, High
}

class Converters {
    @TypeConverter
    fun fromPriority(priority: TodoPriority): String = priority.name

    @TypeConverter
    fun toPriority(priority: String): TodoPriority = TodoPriority.valueOf(priority)
}