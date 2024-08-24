//package com.example.kviz
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.kviz.ui.theme.KvizTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            KvizTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    KvizTheme {
//        Greeting("Android")
//    }
//}

package com.example.kviz

//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import coil.compose.rememberAsyncImagePainter
//import coil.compose.rememberImagePainter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kviz.composable.ChatRoomsScreen
import com.example.kviz.composable.ChatScreen
import com.example.kviz.composable.LeaderboardScreen
import com.example.kviz.composable.QuizScreen
import com.example.kviz.composable.SignInSignUpScreen
import com.example.kviz.pozivi.Question.QuestionDto
import com.example.kviz.pozivi.Question.interfacePoziv.QuizApiService
import com.example.kviz.ui.theme.KvizTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KvizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = currentBackStackEntry?.destination
                    val currentRoute = currentDestination?.route ?: LoginDest.route

                    // Initialize Retrofit and QuizApiService
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val quizApi = retrofit.create(QuizApiService::class.java)

                    // Fetch and display quiz questions
                    //QuizContent(quizApi)
                    //ChatRoomsScreen(navController)
                    //ChatScreen("Luka i kreteni")
                    //SignInSignUpScreen()
                    //LeaderboardScreen()

                    Scaffold(
                        bottomBar = {
                            if (    // prikazuje bottomBar samo ako smo na jednom od ova 3 screen-a
                                currentDestination?.route == ChatRoomsDest.route ||
                                currentDestination?.route == AddQuestionDest.route ||
                                currentDestination?.route == ProfileDest.route
                            ) {
                                BottomAppBar(containerColor = Color(0xFFFFB183)) { // Narandzasta boja
                                    destinations.forEach { navDestination ->
                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    imageVector = navDestination.icon,
                                                    contentDescription = null,
                                                )
                                            },
                                            label = { Text(text = navDestination.route) },
                                            selected = currentRoute.startsWith(navDestination.route),
                                            onClick = {
                                                val newRoute = navDestination.route
                                                navController.navigate(newRoute) {
                                                    launchSingleTop = true
                                                    restoreState = true
                                                    popUpTo(ChatRoomsDest.route) {
                                                        saveState = true
                                                        inclusive = false
                                                    }
                                                }
                                            }
                                        )
                                    }

                                }
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = LoginDest.route,
                            modifier = Modifier.padding(it),
                        ) {
                            composable(route = ChatRoomsDest.route) {
                                ChatRoomsScreen(navController)
                            }
                            composable(route = AddQuestionDest.route) {
                                // TODO
                            }
                            composable(route = ProfileDest.route) {
                                LeaderboardScreen(navController)
                            }
                            composable(route = ChatDest.route) {
                                ChatScreen("vukasin", navController)
                            }
                            composable(route = LoginDest.route) {
                                SignInSignUpScreen(navController)
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun QuizContent(quizApi: QuizApiService) {
    var questions by remember { mutableStateOf<List<QuestionDto>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch quiz questions when the composable is first shown
    LaunchedEffect(Unit) {
        fetchQuizQuestions(quizApi) { result, error ->
            questions = result
            errorMessage = error
        }
    }

    // Display either the quiz questions or an error message
    when {
        questions != null -> {
            displayQuizQuestions(questions!!)
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
    onResult: (List<QuestionDto>?, String?) -> Unit
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

@Composable
fun displayQuizQuestions(questions: List<QuestionDto>) {
    if (questions.isNotEmpty()) {
        QuizScreen(
            currentQuestionIndex = 0,
            totalQuestions = questions.size,
            question = questions[0].questions,
            imageResource = R.drawable.background_login, // Ako koristiš resurs slike
            options = questions[0].answersDto.map { it.answer },
            correctAnswer = questions[0].answersDto.find { it.isCorrect }?.answer ?: "",
            onNextQuestion = {
                // Logika za prebacivanje na sledeće pitanje
            }
        )
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KvizTheme {
        Greeting("Android")
    }
}