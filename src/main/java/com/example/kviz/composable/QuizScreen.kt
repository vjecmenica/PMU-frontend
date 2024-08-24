package com.example.kviz.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizScreen(
    currentQuestionIndex: Int,
    totalQuestions: Int = 10,
    question: String,
    imageResource: Int,
    options: List<String>,
    correctAnswer: String,
    onNextQuestion: () -> Unit
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions

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
                    onNextQuestion()
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