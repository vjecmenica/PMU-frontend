package com.example.kviz.composable

//import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.CategoryDto
import com.example.kviz.pozivi.Question.dtos.CategoryDtoScreen
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionScreenOrigin(navController: NavHostController, dataStore: DataStore<Preferences>) {
    var categories by remember { mutableStateOf<List<CategoryDtoScreen>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // State for showing the dialog
    var newSectionName by remember { mutableStateOf("") } // State for the new section name
    val gson = Gson()
    val userDtoKey = stringPreferencesKey("user_dto")

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ChatroomApiService::class.java)

    // Function to refresh categories
    suspend fun refreshCategories() {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()

        val userId = userDtoJson?.let { json ->
            gson.fromJson(json, UserDto::class.java)?.userid ?: 1
        } ?: 1

        categories = try {
            apiService.getSectionsForUserScreen(userId).dto ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Load initial categories list
    LaunchedEffect(Unit) {
        refreshCategories()
    }

    // Function to create a new section
    suspend fun createSection() {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()

        val userId = userDtoJson?.let { json ->
            gson.fromJson(json, UserDto::class.java)?.userid ?: 1
        } ?: 1

        val newCategory = CategoryDto(
            sectionId = 0, // Assuming the backend handles ID generation
            name = newSectionName,
            numOfQuestions = 0
        )

        try {
            // Call API to create new section
            apiService.createSection(newCategory, userId)
            // Refresh the categories list
            refreshCategories()
        } catch (e: Exception) {
            // Handle error
        }
    }

    // Function to delete a section
    suspend fun deleteSection(sectionId: Int) {
        try {
            apiService.deleteSection(sectionId)
            // Refresh the categories list
            refreshCategories()
        } catch (e: Exception) {
            // Handle error
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFF8A4FFF)) // Purple background
    ) {
        // Postavljanje slike kao pozadine
        Image(
            painter = rememberImagePainter(data = R.drawable.background_chatroom),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.sections_icon1),
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row with "My Categories" text and Add Button
            var shouldCreateSection by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "My Categories",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f) // Take available space
                        .padding(start = 12.dp)
                )

                // Add button to open the dialog
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(end = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB183) // Set the background color of the button
                    )
                ) {
                    Text("Add Section")
                }
            }

            // Show the AlertDialog when adding a new section
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add New Section") },
                    text = {
                        Column {
                            TextField(
                                value = newSectionName,
                                onValueChange = { newSectionName = it },
                                label = { Text("Section Name") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                // Trigger the section creation in a coroutine
                                CoroutineScope(Dispatchers.IO).launch {
                                    createSection()
                                }
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Show the search bar and filtered categories
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
            )

            val filteredCategories = categories.filter {
                it.sectionDto.name.contains(searchQuery, ignoreCase = true)
            }

            // Display the list of categories
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredCategories) { category ->
                    CategoryCardX(
                        title = category.sectionDto.name,
                        courses = "${category.numOfQuestions} questions",
                        onClick = {
                            navController.navigate("all_questions/${category.sectionDto.sectionId}")
                        },
                        onDeleteClick = {
                            // Trigger the delete operation in a coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                deleteSection(category.sectionDto.sectionId)
                            }
                        }
                    )
                }
            }
        }

    }
}


@Composable
fun CategoryCardX(title: String, courses: String, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(hoveredElevation = 10.dp, defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8A4FFF)
                )
                Text(
                    text = courses,
                    fontSize = 16.sp,
                    color = Color(0xFF8A4FFF)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
                    .clickable { onDeleteClick() } // Trigger onDeleteClick when the icon is clicked
            )
        }
    }
}
