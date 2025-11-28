package com.codewithcc.lumina.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithcc.lumina.App
import com.codewithcc.lumina.model.db.TodoEntity
import com.codewithcc.lumina.model.TodoPriority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TodoScreenViewModel : ViewModel() {
    private val todoDao = App.Companion.todoDatabase.getTodoDao()
    private val _todoList = MutableStateFlow<List<TodoEntity>>(emptyList())
    private val _selectedTodo = MutableStateFlow(TodoEntity())
    private val _showSheet = MutableStateFlow(false)
    private val _loading = MutableStateFlow(false)
    private val _showDialog = MutableStateFlow(false)
    private val _initialLoading = MutableStateFlow(false)

    val todoList = _todoList.asStateFlow()
    val selectedTodo = _selectedTodo.asStateFlow()
    val showSheet = _showSheet.asStateFlow()
    val loading = _loading.asStateFlow()
    val showDialog = _showDialog.asStateFlow()
    val initialLoading = _initialLoading.asStateFlow()

    fun updateSelectedTodo(value: TodoEntity) = _selectedTodo.update { value }
    fun toggleSheet() = _showSheet.update { !it }
    fun toggleDialog() = _showDialog.update { !it }

    init {
        observeTodos()
    }

    private fun observeTodos() {
        viewModelScope.launch {
            _initialLoading.update { true }
            delay(1000)
            todoDao.getTodos().collectLatest { newTodoList ->
                _todoList.update { newTodoList }
                _initialLoading.update { false }
            }
        }
    }

    fun addTodo(title: String, description: String, priority: TodoPriority) {
        viewModelScope.launch {
            _loading.update { true }
            val data = TodoEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                priority = priority,
                createdAt = System.currentTimeMillis()
            )
            todoDao.addTodo(data)
            _selectedTodo.update { TodoEntity() }
            _loading.update { false }
            _showSheet.update { false }
        }
    }

    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch {
            _loading.update { true }
            todoDao.updateTodo(todo)
            _selectedTodo.update { TodoEntity() }
            _loading.update { false }
            _showSheet.update { false }
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            _loading.update { false }
            todoDao.deleteTodo(id)
            _selectedTodo.update { TodoEntity() }
            _loading.update { true }
            _showDialog.update { false }
        }
    }

    fun updateCheck(id: String, checked: Boolean) {
        viewModelScope.launch {
            todoDao.updateCheck(id, checked)
        }
    }
}