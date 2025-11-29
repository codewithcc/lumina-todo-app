package com.codewithcc.lumina.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.codewithcc.lumina.R
import com.codewithcc.lumina.model.db.TodoEntity
import com.codewithcc.lumina.model.TodoPriority

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TodoInputScreen(
    initialData: TodoEntity,
    loading: Boolean,
    onClick: (String, String, TodoPriority) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(initialData.title) }
    var description by rememberSaveable { mutableStateOf(initialData.description) }
    var priority: TodoPriority? by rememberSaveable { mutableStateOf(initialData.priority) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(if (initialData.id.isEmpty()) R.string.add else R.string.update_todo),
            style = MaterialTheme.typography.headlineMedium
                .copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = title,
            enabled = !loading,
            onValueChange = {
                if (it.length <= 12) title = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_title)
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = description,
            enabled = !loading,
            onValueChange = {
                if (it.length <= 120) description = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_desc)
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            )
        )
        Spacer(Modifier.height(12.dp))
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                value = priority?.name ?: stringResource(R.string.select_priority),
                enabled = !loading,
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                TodoPriority.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name
                            )
                        },
                        onClick = {
                            priority = it
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !loading,
            onClick = {
                if (!loading && title.isNotEmpty() && description.isNotEmpty() && priority != null) {
                    onClick(title, description, priority ?: TodoPriority.Low)
                }
            }
        ) {
            if (loading) CircularWavyProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = Color.Transparent
            ) else Text(text = stringResource(if (initialData.id.isEmpty()) R.string.add else R.string.update))
        }
    }
}