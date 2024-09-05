package com.example.kviz.composable

//import androidx.compose.foundation.layout.BoxScopeInstance.align

// za snackbar

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

// align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.CategoryDtoScreen
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChoseCategoryScreen(
//    userId: Int = 1,
//    currentQuestionIndex: Int,
//    totalQuestions: Int = 10,
//    question: String,
//    imageResource: Int,
//    options: List<String>,
//    correctAnswer: String,
//    onNextQuestion: (String) -> Unit
//) {
//    var categories by remember { mutableStateOf<List<CategoryDTO>>(emptyList()) }
//    var searchQuery by remember { mutableStateOf("") }
//    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val apiService = retrofit.create(ChatroomApiService::class.java)
//
//    LaunchedEffect(Unit) {
//        categories = try {
//            apiService.getAllSectionsForUser().dto ?: emptyList()
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//
//    Log.e("Ime sekcije", categories.toString())
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF8A4FFF)) // Purple background
//    ) {
//        // Top Image with Characters and Question Mark
//        Image(
//            painter = painterResource(id = R.drawable.choose_categories), // Replace with your top image
//            contentDescription = "Top Image",
//            modifier = Modifier
//                .fillMaxWidth()
////                .padding(top = 32.dp)
////                .height(240.dp) // Height adjusted for the image with characters
//                .align(Alignment.CenterHorizontally),
////            contentScale = ContentScale.Fit
//        )
//
//        // Title Text and Button in a Row
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .padding(horizontal = 16.dp/*, vertical = 8.dp*/)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = "Choose Categories",
//                fontSize = 28.sp, // Increased font size
//                fontWeight = FontWeight.Bold,
//                color = Color.White, // White text
//                modifier = Modifier.weight(1f)
//            )
//            // Optional Button (Play Icon)
//            Image(
//                painter = painterResource(id = R.drawable.play_button), // Replace with your play icon
//                contentDescription = "Play Button",
//                modifier = Modifier
//                    .size(100.dp)
////                    .background(Color.White, RoundedCornerShape(50))
////                    .padding(8.dp)
//                    .clickable {
//                        if (selectedCategoryId != null) {
//                            // If the category ID is selected (not null), proceed to the QuizScreen
//                            QuizScreen(
//                                currentQuestionIndex = currentQuestionIndex,
//                                totalQuestions = totalQuestions,
//                                question = question,
//                                imageResource = imageResource,
//                                options = options,
//                                correctAnswer = correctAnswer,
//                                onNextQuestion = onNextQuestion,
//                                categoryId = selectedCategoryId!!
//                            )
//                        } else {
//                            LaunchedEffect(Unit) {
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar("Morate izabrati kategoriju pre nego što nastavite.")
//                                }
//                            }
//                        }
//
//                    }
//            )
//        }
//
//        // Search Bar
//        TextField(
//            value = searchQuery,
//            onValueChange = { searchQuery = it },
//            placeholder = { Text("Search", color = Color.Gray) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//                .background(Color.White, RoundedCornerShape(8.dp))
//                .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
//        )
//
//        // Filtered Categories
//        val filteredCategories = categories.filter {
//            it.name.contains(searchQuery, ignoreCase = true)
//        }
//
//        // LazyVerticalGrid for displaying 2 items per row
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            contentPadding = PaddingValues(vertical = 8.dp)
//        ) {
//            items(filteredCategories) { category ->
//                CategoryCardY(
//                    title = category.name,
//                    courses = "${category.questionCount} questions",
//                    onClick = {
//                        // Handle click event here
//                        selectedCategoryId = category.sectionId
//                        Log.d("Category Clicked", "Clicked on category: ${category.name}")
//                    }
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoseCategoryScreen(
    navController: NavHostController,
    userId: Int = 1,
    chatroomId: Int,
//    currentQuestionIndex: Int,
//    totalQuestions: Int = 10,
//    question: String,
//    imageResource: Int,
//    options: List<String>,
//    correctAnswer: String,
//    onNextQuestion: (String) -> Unit
) {
    var categories by remember { mutableStateOf<List<CategoryDtoScreen>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showQuizScreen by remember { mutableStateOf(false) }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ChatroomApiService::class.java)

    LaunchedEffect(Unit) {
        categories = try {
            apiService.getSectionsForUserScreen(userId).dto ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Trigger snackbar display
    if (snackbarHostState.currentSnackbarData != null) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(16.dp)
//                .align(Alignment.BottomCenter)
        )
    }

    if (showQuizScreen && selectedCategoryId != null) {
//        QuizScreen(
//            currentQuestionIndex = currentQuestionIndex,
//            totalQuestions = totalQuestions,
//            question = question,
//            imageResource = imageResource,
//            options = options,
//            correctAnswer = correctAnswer,
//            onNextQuestion = onNextQuestion,
//            categoryId = selectedCategoryId!!
//        )
        QuizContent(
            navController = navController,
            chatroomId = chatroomId,
            categoryId = selectedCategoryId!!,
            userId = userId
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8A4FFF)) // Purple background
        ) {
            // Top Image with Characters and Question Mark
            Image(
                painter = painterResource(id = R.drawable.choose_categories), // Replace with your top image
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            // Title Text and Button in a Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Choose Categories",
                    fontSize = 28.sp, // Increased font size
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // White text
                    modifier = Modifier.weight(1f)
                )
                // Optional Button (Play Icon)
                Image(
                    painter = painterResource(id = R.drawable.play_button), // Replace with your play icon
                    contentDescription = "Play Button",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            if (selectedCategoryId != null) {
                                showQuizScreen = true
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Morate izabrati kategoriju pre nego što nastavite.")
                                }
                            }
                        }
                )
            }

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
            )

            // Filtered Categories
            val filteredCategories = categories.filter {
                it.sectionDto.name.contains(searchQuery, ignoreCase = true)
            }

            // LazyVerticalGrid for displaying 2 items per row
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filteredCategories) { category ->
                    CategoryCardY(
                        title = category.sectionDto.name,
                        courses = "${category.numOfQuestions} questions",
                        onClick = {
                            selectedCategoryId = category.sectionDto.sectionId
                            Log.d("Category Clicked", "Clicked on category: ${category.sectionDto.name}")
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun CategoryCardY(title: String, courses: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp) // Padding between cards
            .fillMaxWidth()
            .height(110.dp) // Card height
            .clickable { onClick() }, // Add clickable modifier
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(hoveredElevation = 10.dp, defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // White cards
        )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp) // Move text towards the top of the card
        ) {
            Text(
                text = title,
                fontSize = 20.sp, // Increased font size for title
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A4FFF) // Purple text
            )
            Text(
                text = courses,
                fontSize = 16.sp, // Increased font size for question count
                color = Color(0xFF8A4FFF) // Purple text
            )
        }
    }
}
