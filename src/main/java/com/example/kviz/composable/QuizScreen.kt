package com.example.kviz.composable

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kviz.R
import kotlinx.coroutines.launch

fun playSound(context: Context, soundResourceId: Int, onSoundCompleted: () -> Unit) {
    val mediaPlayer = MediaPlayer.create(context, soundResourceId)
    mediaPlayer.setOnCompletionListener {
        Log.d("Quiz", "Zvuk je završio reprodukciju.")
        it.release() // Oslobađanje resursa nakon reprodukcije

        // Dodajemo odlaganje nakon što zvuk završi
        kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(700) // Odlaganje za 700ms
            onSoundCompleted() // Nastavljamo sa prelaskom na sledeće pitanje
        }
    }
    Log.d("Quiz", "Početak reprodukcije zvuka.")
    mediaPlayer.start() // Početak reprodukcije
}

@Composable
fun QuizScreen(
    currentQuestionIndex: Int,
    totalQuestions: Int = 10,
    question: String,
    imageResource: Int,
    options: List<String>,
    correctAnswer: String,
    onNextQuestion: (String) -> Unit,
    categoryId: Int,
    chatroomId: Int
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var answerSubmitted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions

    // Resetovanje stanja kada se prikaže novo pitanje
    LaunchedEffect(currentQuestionIndex) {
        selectedOption = null
        isAnswerCorrect = null
        answerSubmitted = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8A4FFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp)) // Zaobljenje ivice na sve strane
                .padding(bottom = 0.dp), // Uklonjen padding ispod bara
            color = Color(0xFFFFA726),
            trackColor = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )

                Text(
                    text = question,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            val buttonColor by animateColorAsState(
                if (selectedOption == option) {
                    if (isAnswerCorrect == true && option == correctAnswer) {
                        Color.Green
                    } else if (isAnswerCorrect == false && option == selectedOption) {
                        Color.Red
                    } else {
                        Color(0xFFFFA726)
                    }
                } else {
                    Color.White
                }, label = ""
            )

            Button(
                onClick = {
                    selectedOption = option
                    isAnswerCorrect = (option == correctAnswer)
                    answerSubmitted = true

                    playSound(context, if (isAnswerCorrect == true) R.raw.sound_correct else R.raw.sound_wrong) {
                        // Ovo će se izvršiti nakon što zvuk završi i nakon odlaganja
                        onNextQuestion(option)
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = if (selectedOption == option) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = option, fontSize = 18.sp)
            }
        }
    }
}
