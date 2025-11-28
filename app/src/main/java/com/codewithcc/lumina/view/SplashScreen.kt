package com.codewithcc.lumina.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codewithcc.lumina.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onClick: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1000)
        onClick()
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(220.dp),
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "app icon"
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            text = "from Code with Cc",
            style = MaterialTheme.typography.bodyLarge
                .copy(fontWeight = FontWeight.SemiBold)
        )
    }
}