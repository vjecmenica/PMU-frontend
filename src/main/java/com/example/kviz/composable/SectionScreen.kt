package com.example.vezbazak1.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kviz.AllQuestionsDest
import com.example.kviz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionsScreen(
    navController: NavHostController
) {
    val sections = listOf("Section 1", "Section 2", "Section 3")
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Section", fontSize = 24.sp, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB183),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFFFFB183)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8A4FFF))
                    .padding(paddingValues)
            ) {
                items(sections) { section ->
//                    ChatRoomItem(navController, chatRoom)
                    SectionItem(navController, section)
                }
            }

            if (showDialog) {
                AddQuestionDialog(onDismiss = { showDialog = false }, onSave = {
                    // Ovde dodaj akciju za čuvanje pitanja
                    showDialog = false
                })
            }
        }
    )
}

@Composable
fun SectionItem(
    navController: NavHostController,
    sectionName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween // ili Arrangement.End
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_login), // Zameni sa pravim resursom
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = sectionName, color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            navController.navigate(AllQuestionsDest.createRoute(sectionName))
        }) {
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Enter")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionDialog(onDismiss: () -> Unit, onSave: () -> Unit) {
    var question by remember { mutableStateOf("") }
    var answer1 by remember { mutableStateOf("") }
    var answer2 by remember { mutableStateOf("") }
    var answer3 by remember { mutableStateOf("") }
    var answer4 by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf(0) }
    var sectionId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add Question")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer1,
                    onValueChange = { answer1 = it },
                    label = { Text("Answer 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer2,
                    onValueChange = { answer2 = it },
                    label = { Text("Answer 2") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer3,
                    onValueChange = { answer3 = it },
                    label = { Text("Answer 3") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer4,
                    onValueChange = { answer4 = it },
                    label = { Text("Answer 4") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Correct Answer", fontSize = 16.sp)

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = correctAnswer == 1,
                                onClick = { correctAnswer = 1 }
                            )
                            Text(text = "1")
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = correctAnswer == 2,
                                onClick = { correctAnswer = 2 }
                            )
                            Text(text = "2")
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = correctAnswer == 3,
                                onClick = { correctAnswer = 3 }
                            )
                            Text(text = "3")
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = correctAnswer == 4,
                                onClick = { correctAnswer = 4 }
                            )
                            Text(text = "4")
                        }
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = sectionId,
                    onValueChange = { sectionId = it },
                    label = { Text("Section ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
