package com.example.kviz.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            painter = rememberAsyncImagePainter(model = R.drawable.your_logo1),
            contentDescription = "Splash GIF",
            modifier = Modifier.size(300.dp)
        )
    }
}

//@Composable
//fun SplashScreen(onSplashComplete: () -> Unit) {
//    var startAnimation by remember { mutableStateOf(false) }
//
//    LaunchedEffect(key1 = true) {
//        startAnimation = true
//        delay(3000) // Duration of the splash screen in milliseconds
//        onSplashComplete()
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFEFE7F4)) // Set background color to match the one in the image
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.splash_screen), // Replace with the image ID
//            contentDescription = "Splash Image",
//            modifier = Modifier.size(300.dp)
//        )
//    }
//}