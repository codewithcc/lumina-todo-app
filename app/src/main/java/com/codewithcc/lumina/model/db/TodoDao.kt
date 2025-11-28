package com.codewithcc.lumina.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(data: TodoEntity)

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getTodos(): Flow<List<TodoEntity>>

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("UPDATE todos SET isChecked = :checked WHERE id = :id")
    suspend fun updateCheck(id: String, checked: Boolean)

    @Query("DELETE from todos WHERE id = :id")
    suspend fun deleteTodo(id: String)

}