package com.example.kviz.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kviz.R
import com.example.kviz.ResultDest
import com.example.kviz.pozivi.Question.dtos.QuestionDto
import com.example.kviz.pozivi.Question.interfacePoziv.QuizApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatRoomName: String = "",
    navController: NavHostController
) {
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    val messages = listOf(
        Message("Hi, how are you?", isUser = false),
        Message("I'm good, thank you! How about you?", isUser = true),
        Message("I'm great! What are you up to?", isUser = false),
        Message("Just working on a project.", isUser = true)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chatRoomName, fontSize = 20.sp, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB183), // Narandžasta boja
                    titleContentColor = Color.White // Boja teksta
                ),
                actions = {
                    IconButton(onClick = { /* Leave chatroom action */ }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Leave", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("quiz") }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Join Quiz", tint = Color.White)
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background_login), // Pozadinska slika
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        items(messages) { message ->
                            MessageItem(message)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFFFFFFF)) // Boja pozadine za unos poruke
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Type a message...") },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent
                            )
                        )
                        IconButton(onClick = {
                            if (messageText.text.isNotBlank()) {
                                // Ovde bi dodao poruku u listu ako bi koristio mutable listu
                                messageText = TextFieldValue("") // Očisti tekstualno polje
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Send", tint = Color(0xFFFFB183))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isUser) Color(0xFFE1FFC7) else Color(0xFFD3D3D3),
                    shape = CircleShape
                )
                .padding(16.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}

data class Message(val text: String, val isUser: Boolean)

@Composable
fun QuizContent(navController: NavHostController) {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quizApi = retrofit.create(QuizApiService::class.java)

    var questions by remember { mutableStateOf<List<QuestionDto>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch quiz questions when the composable is first shown
    LaunchedEffect(Unit) {
        fetchQuizQuestions(quizApi = quizApi) { result, error ->
            questions = result
            errorMessage = error
        }
    }

    // Display either the quiz questions or an error message
    when {
        questions != null -> {
            displayQuizQuestions(navController, questions!!)
        }
        errorMessage != null -> {
            // You can customize this UI as needed
            Log.e("Quiz", errorMessage ?: "Unknown error")
        }
        else -> {
            // Show a loading state or placeholder
        }
    }
}

fun fetchQuizQuestions(
    quizApi: QuizApiService,
    onResult: (List<QuestionDto>?, String?) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = quizApi.getQuizQuestions()

            withContext(Dispatchers.Main) {
                if (response.isValid) {
                    onResult(response.dto, null)
                } else {
                    onResult(null, response.errorMessage ?: "Unknown error")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult(null, "API call failed: ${e.message}")
            }
        }
    }
}

//@Composable
//fun displayQuizQuestions(questions: List<QuestionDto>) {
//    if (questions.isNotEmpty()) {
//        QuizScreen(
//            currentQuestionIndex = 0,
//            totalQuestions = questions.size,
//            question = questions[0].questions,
//            imageResource = R.drawable.background_login, // Ako koristiš resurs slike
//            options = questions[0].answersDto.map { it.answer },
//            correctAnswer = questions[0].answersDto.find { it.isCorrect }?.answer ?: "",
//            onNextQuestion = {
//                // Logika za prebacivanje na sledeće pitanje
//            }
//        )
//    }
//}
@Composable
fun displayQuizQuestions(navController: NavHostController, questions: List<QuestionDto>) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }  // Dodajemo promenljivu za praćenje broja tačnih odgovora
    val randomImages = remember { generateRandomImages() }
    if (questions.isNotEmpty()) {

        QuizScreen(
            currentQuestionIndex = currentQuestionIndex,
            totalQuestions = questions.size,
            question = questions[currentQuestionIndex].questions,
            imageResource = randomImages[currentQuestionIndex],
            //imageResource = R.drawable.background_login, // Ako koristiš resurs slike
            options = questions[currentQuestionIndex].answersDto.map { it.answer },
            correctAnswer = questions[currentQuestionIndex].answersDto.find { it.isCorrect }?.answer ?: "",
            onNextQuestion = {selectedOption ->
                if (questions[currentQuestionIndex].answersDto.find { it.isCorrect }?.answer == selectedOption) {
                    Log.d("Quiz", "Correct Answer: $correctAnswers")
                    correctAnswers++  // Ažuriramo broj tačnih odgovora
                }

                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            navController.navigate(ResultDest.createRoute(correctAnswers))
                        } catch (e: Exception) {
                            Log.e("Quiz", "Navigation error: ${e.message}")
                        }
                    }
                }
            }
        )
    }
}

fun generateRandomImages(): List<Int> {
    val allImages = (1..16).map { i ->
        val resourceName = "face$i"
        val resourceId = getResourceIdByName(resourceName)
        resourceId
    }

    return allImages.shuffled().take(10) // Izmešamo i uzmemo prvih 10
}

fun getResourceIdByName(resourceName: String): Int {
    return R.drawable::class.java.getDeclaredField(resourceName).getInt(null)
}

