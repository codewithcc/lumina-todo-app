package com.codewithcc.lumina.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithcc.lumina.R
import com.codewithcc.lumina.viewModel.TodoScreenViewModel
import com.codewithcc.lumina.model.db.TodoEntity
import com.codewithcc.lumina.model.TodoPriority
import com.codewithcc.lumina.view.theme.Green
import com.codewithcc.lumina.view.theme.Red
import com.codewithcc.lumina.view.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodoScreen() {
    val viewModel: TodoScreenViewModel = viewModel()
    val todoList by viewModel.todoList.collectAsState()
    val showSheet by viewModel.showSheet.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val selectedTodo by viewModel.selectedTodo.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val initialLoading by viewModel.initialLoading.collectAsState()

    var lastTodoPriority by rememberSaveable { mutableStateOf<TodoPriority?>(null) }

    if (showDialog) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {
                viewModel.toggleDialog()
                viewModel.updateSelectedTodo(TodoEntity())
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete_icon),
                    tint = Color.Red
                )
            },
            title = {
                Text(text = stringResource(R.string.delete_todo))
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_todo_text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (!loading) {
                            viewModel.toggleDialog()
                            viewModel.updateSelectedTodo(TodoEntity())
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (!loading) viewModel.deleteTodo(selectedTodo.id)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete)
                    )
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.toggleSheet()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { values ->
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.toggleSheet()
                    viewModel.updateSelectedTodo(TodoEntity())
                }
            ) {
                TodoInputScreen(selectedTodo, loading) { title, description, priority ->
                    if (selectedTodo.id.isEmpty()) {
                        viewModel.addTodo(title, description, priority)
                    } else {
                        viewModel.updateTodo(selectedTodo.copy(
                            title = title.ifEmpty { selectedTodo.title },
                            description = description.ifEmpty { selectedTodo.description },
                            priority = priority
                        ))
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            contentAlignment = Alignment.Center
        ) {
            if (initialLoading) {
                CircularWavyProgressIndicator()
            } else {
                if (todoList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_todos)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            todoList.sortedWith(
                                compareByDescending<TodoEntity> { it.priority.ordinal }
                                    .thenByDescending { it.createdAt }
                            )
                        ) { todo ->
                            if (lastTodoPriority != todo.priority) {
                                lastTodoPriority = todo.priority
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp, top = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(
                                                color = when (lastTodoPriority) {
                                                    TodoPriority.Medium -> Yellow
                                                    TodoPriority.High -> Green
                                                    else -> Red
                                                },
                                                shape = CircleShape
                                            )
                                    )
                                    Text(
                                        text = "$lastTodoPriority Priority",
                                        fontSize = 12.sp
                                    )
                                    HorizontalDivider()
                                }
                                Spacer(Modifier.height(12.dp))
                            }
                            TodoCard(
                                todo = todo,
                                onCheckChange = {
                                    viewModel.updateCheck(todo.id, !todo.isChecked)
                                },
                                onEdit = {
                                    viewModel.updateSelectedTodo(todo)
                                    viewModel.toggleSheet()
                                },
                                onDelete = {
                                    viewModel.updateSelectedTodo(todo)
                                    viewModel.toggleDialog()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}