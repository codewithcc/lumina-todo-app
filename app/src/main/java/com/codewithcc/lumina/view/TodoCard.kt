package com.codewithcc.lumina.view

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithcc.lumina.R
import com.codewithcc.lumina.model.db.TodoEntity
import com.codewithcc.lumina.view.theme.DeleteDark
import com.codewithcc.lumina.view.theme.DeleteLight
import com.codewithcc.lumina.view.theme.EditDark
import com.codewithcc.lumina.view.theme.EditLight
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TodoCard(
    todo: TodoEntity,
    onCheckChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val isDark = isSystemInDarkTheme()
    var createdAt by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(todo) {
        val instant = Instant.ofEpochMilli(todo.createdAt)
        createdAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("hh:mm a - dd/MM/yy"))
    }

    Box(
        modifier = Modifier
            .background(
                color = if (todo.isChecked) if (isDark) Color.DarkGray else Color.LightGray
                else  MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = {
                        expanded = !expanded
                    }
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Checkbox(
                checked = todo.isChecked,
                onCheckedChange = {
                    hapticFeedback.performHapticFeedback(
                        if (todo.isChecked) HapticFeedbackType.ToggleOff
                        else HapticFeedbackType.ToggleOn
                    )
                    onCheckChange(it)
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = todo.title,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (todo.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        fontSize = 18.sp
                    )
                    Text(
                        modifier = Modifier
                            .basicMarquee(iterations = Int.MAX_VALUE),
                        text = createdAt,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                Text(
                    text = todo.description,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify,
                    maxLines = if (expanded) Int.MAX_VALUE else 1
                )
            }
            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "edit",
                    tint = if (isDark) EditDark else EditLight
                )
            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "delete",
                    tint = if (isDark) DeleteDark else DeleteLight
                )
            }
        }
    }
}