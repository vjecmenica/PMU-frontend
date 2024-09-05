package com.example.kviz.composable

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kviz.ChoseCategoryDest
import com.example.kviz.R
import com.example.kviz.ResultDest
import com.example.kviz.pozivi.Question.dtos.QuestionDto
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
import com.example.kviz.pozivi.Question.interfacePoziv.QuizApiService
import com.example.kviz.pozivi.Question.interfacePoziv.UserApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    chatroomId: Int,
    dataStore: DataStore<Preferences>
) {
    val gson = Gson()
    val userDtoKey = stringPreferencesKey("user_dto")
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userApi = retrofit.create(UserApiService::class.java)
    val chatroomApi = retrofit.create(ChatroomApiService::class.java)

    var userDto by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()
        Log.d("Chat screen", "UserDto JSON iz DataStore: $userDtoJson")
        userDtoJson?.let { json ->
            userDto = gson.fromJson(json, UserDto::class.java)
        }

        Log.d("Chat screen", "UserDto: $userDto")
    }

    TopAppBar(
        title = {
            Text(
                text = "ETF Chatroom Gang",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A4FFF) // Promenjena boja teksta
            )
        },
        colors =  TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFFB995FF),
            titleContentColor = Color.White
        ), // Ljubičasta boja za top app bar
        actions = {
            IconButton(onClick = { navController.navigate(ChoseCategoryDest.createRoute(chatroomId = chatroomId ?: 1, userId = userDto?.userid ?: 1)) }) {
                Image(
                    painter = painterResource(id = R.drawable.play_button),
                    contentDescription = "Play Button",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    )
}

@Composable
fun MessagesList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Hardkodovane poruke sa bubble pozadinom
        MessageBubble(
            imageRes = R.drawable.face1,
            messageText = "Super!",
            time = "Yesterday",
            isCurrentUser = false
        )
        MessageBubble(
            imageRes = R.drawable.face10,
            messageText = "Jel hoće neko da igra?",
            isCurrentUser = false
        )
        MessageBubble(
            imageRes = R.drawable.face13,
            messageText = "Evo čeka se admin.",
            isCurrentUser = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        MessageBubble(
            imageRes = R.drawable.face4,
            messageText = "Ulazimmm",
            isCurrentUser = true
        )
    }
}

@Composable
fun MessageBubble(imageRes: Int, messageText: String, time: String? = null, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isCurrentUser) {
            // Avatar sa leve strane za poruke od drugih korisnika
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            if (time != null) {
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Pozadina poruke sa balonom (message bubble)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isCurrentUser) Color(0xFFFFB183) else Color.White)
                    .padding(12.dp)
            ) {
                Text(
                    text = messageText,
                    fontSize = 16.sp,
                    color = if (isCurrentUser) Color.White else Color.Black
                )
            }
        }

        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // Avatar sa desne strane za trenutnog korisnika
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
    }
}
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatroomId: Int,
    chatRoomName: String,
    dataStore: DataStore<Preferences>
) {
    BackHandler {
        // Navigiraj uvek nazad na "ChatRoom" screen
        navController.navigate("chat_rooms") {
            popUpTo("chat_rooms") { inclusive = true }
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = rememberImagePainter(data = R.drawable.background_chatroom),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gornja traka sa nazivom chat room-a i Play dugmetom
            TopBar(navController = navController, chatroomId = chatroomId, dataStore = dataStore)

            // Lista poruka
            MessagesList()

            Spacer(modifier = Modifier.weight(1f)) // Ovo gura polje za unos dole

            // Dodaj dugme za dodavanje korisnika
            FloatingActionButton(
                onClick = { showDialog = true }, // Show dialog when clicked
                modifier = Modifier.align(Alignment.End).background(Color(0xFFFFB183))
            ) {
                Text("Add New User")
            }


            // Polje za unos poruke na dnu
            MessageInputField()
        }

        if (showDialog) {
            AddUserDialog(chatroomId = chatroomId, dataStore = dataStore) {
                showDialog = false // Close the dialog when done
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserDialog(
    chatroomId: Int,
    dataStore: DataStore<Preferences>,
    onDismiss: () -> Unit
) {
    var username by remember { mutableStateOf("") }

    val gson = Gson()
    val userDtoKey = stringPreferencesKey("user_dto")
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userApi = retrofit.create(UserApiService::class.java)
    val chatroomApi = retrofit.create(ChatroomApiService::class.java)

    var userDto by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()
        Log.d("Chat screen", "UserDto JSON iz DataStore: $userDtoJson")
        userDtoJson?.let { json ->
            userDto = gson.fromJson(json, UserDto::class.java)
        }

        Log.d("Chat screen", "UserDto: $userDto")
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New User") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Add the icon above the input field
                Icon(
                    painter = painterResource(id = R.drawable.face1),
                    contentDescription = "Icon",
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text field for username input
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Enter username") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Call the API to add the user
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            chatroomApi.joinChatroom(username = username, chatroomId = chatroomId)
                            withContext(Dispatchers.Main) {
                                // Success: Dismiss the dialog
                                onDismiss()
                            }
                        } catch (e: Exception) {
                            Log.e("AddUserDialog", "Error adding user: ${e.message}")
                        }
                    }
                }
            ) {
                Text("Add User")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField() {
    // Koristimo remember i mutableStateOf da čuvamo i pratimo stanje unosa
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()

            //.align(Alignment.BottomCenter), // Poravnanje pri dnu
            .background(Color((0xFF8A4FFF))),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText, // Ovdje postavljamo dinamički tekst
            onValueChange = { newText -> messageText = newText }, // Ažuriramo stanje unosa
            placeholder = { Text(text = "Send message") },
            modifier = Modifier.padding(8.dp)
                .clip(RoundedCornerShape(16.dp)), // Zauzmi širinu dostupnog prostora
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),

            )
        Spacer(modifier = Modifier.width(8.dp))

        // Dugme za slanje poruke
        Button(onClick = {
            // Ovdje možeš dodati akciju za slanje poruke
            println("Poruka poslata: $messageText")
            messageText = "" // Resetuj polje za unos nakon slanja poruke
        }) {
            Text(text = "Send")
        }
    }
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
fun QuizContent(
    navController: NavHostController,
    chatroomId: Int,
    categoryId: Int,
    userId: Int
) {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quizApi = retrofit.create(QuizApiService::class.java)

    var questions by remember { mutableStateOf<List<QuestionDto>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch quiz questions when the composable is first shown
    LaunchedEffect(Unit) {
        Log.d("userId: ", userId.toString())
        Log.d("chatroomId: ", chatroomId.toString())
        Log.d("categoryId: ", categoryId.toString())
        fetchQuizQuestions(
            quizApi = quizApi,
            chatroomId = chatroomId,
            categoryId = categoryId,
            userId = userId
        ) { result, error ->
            questions = result
            errorMessage = error
        }
    }

    // Display either the quiz questions or an error message
    when {
        questions != null -> {
            displayQuizQuestions(navController, questions!!, categoryId, chatroomId)
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
    chatroomId: Int,
    categoryId: Int,
    userId: Int,
    onResult: (List<QuestionDto>?, String?) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        Log.d("userId: ", userId.toString())
        Log.d("chatroomId: ", chatroomId.toString())
        Log.d("categoryId: ", categoryId.toString())
        try {
            val response = quizApi.getQuizQuestions(chatroomId.toInt(), categoryId.toInt(), userId.toInt())

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

@Composable
fun displayQuizQuestions(
    navController: NavHostController,
    questions: List<QuestionDto>,
    categoryId: Int,
    chatroomId: Int,
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }  // Dodajemo promenljivu za praćenje broja tačnih odgovora
    val randomImages = remember { generateRandomImages() }
    if (questions.isNotEmpty()) {

        QuizScreen(
//        ChoseCategoryScreen(
            currentQuestionIndex = currentQuestionIndex,
            totalQuestions = questions.size,
            question = questions[currentQuestionIndex].questions,
            imageResource = randomImages[currentQuestionIndex],
            //imageResource = R.drawable.background_login, // Ako koristiš resurs slike
            options = questions[currentQuestionIndex].answersDto.map { it.answer },
            correctAnswer = questions[currentQuestionIndex].answersDto.find { it.isCorrect }?.answer ?: "",
            onNextQuestion = { selectedOption ->
                if (questions[currentQuestionIndex].answersDto.find { it.isCorrect }?.answer == selectedOption) {
                    Log.d("Quiz", "Correct Answer: $correctAnswers")
                    correctAnswers++  // Ažuriramo broj tačnih odgovora
                }

                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            navController.navigate(ResultDest.createRoute(correctAnswers, chatroomId))
                        } catch (e: Exception) {
                            Log.e("Quiz", "Navigation error: ${e.message}")
                        }
                    }
                }
            },
            categoryId = categoryId,
            chatroomId = chatroomId
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

