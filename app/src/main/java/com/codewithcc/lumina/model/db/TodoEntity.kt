package com.codewithcc.lumina.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithcc.lumina.model.TodoPriority

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val description: String = "",
    val isChecked: Boolean = false,
    val priority: TodoPriority = TodoPriority.Low,
    val createdAt: Long = System.currentTimeMillis()
)