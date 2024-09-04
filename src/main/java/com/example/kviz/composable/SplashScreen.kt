package com.example.kviz.composable


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.kviz.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000) // Trajanje splash ekrana u milisekundama
        onSplashComplete()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8A4FFF))
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = R.drawable.your_logo1), // Zamenite sa vašim GIF-om
            contentDescription = "Splash GIF",
            modifier = Modifier.size(300.dp)
        )
    }
}