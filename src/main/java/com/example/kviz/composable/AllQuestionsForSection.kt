package com.example.vezbazak1.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailsScreen(
    sectionId: String,
    navController: NavHostController
) {
    // Pretpostavimo da ovde imamo listu pitanja i odgovora koja pripadaju ovoj sekciji
    val questions = remember { mutableStateListOf(
        Question("Question 1", "Answer 1", "Answer 2", "Answer 3", "Answer 4", 1),
        Question("Question 2", "Answer A", "Answer B", "Answer C", "Answer D", 2)
    ) }

    var showEditDialog by remember { mutableStateOf(false) }
    var questionToEdit by remember { mutableStateOf<Question?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Questions for Section $sectionId", fontSize = 24.sp, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB183),
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8A4FFF))
                    .padding(paddingValues)
            ) {
                items(questions) { question ->
                    QuestionItem(
                        question = question,
                        onEditClick = {
                            questionToEdit = question
                            showEditDialog = true
                        },
                        onDeleteClick = {
                            questions.remove(question)
                        }
                    )
                }
            }

            if (showEditDialog && questionToEdit != null) {
                EditQuestionDialog(
                    question = questionToEdit!!,
                    onDismiss = { showEditDialog = false },
                    onSave = { editedQuestion ->
                        // Update the question in the list
                        val index = questions.indexOfFirst { it == questionToEdit }
                        if (index != -1) {
                            questions[index] = editedQuestion
                        }
                        showEditDialog = false
                    }
                )
            }
        }
    )
}

@Composable
fun QuestionItem(question: Question, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .background(Color.White)
        .padding(8.dp)) {
        Text(text = question.text, fontSize = 18.sp, color = Color.Black)
        Text(text = "1. ${question.answer1}", fontSize = 16.sp, color = Color.Gray)
        Text(text = "2. ${question.answer2}", fontSize = 16.sp, color = Color.Gray)
        Text(text = "3. ${question.answer3}", fontSize = 16.sp, color = Color.Gray)
        Text(text = "4. ${question.answer4}", fontSize = 16.sp, color = Color.Gray)
        Text(text = "Correct Answer: ${question.correctAnswer}", fontSize = 16.sp, color = Color.Blue)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onEditClick) {
                Text(text = "Edit", color = Color.Blue)
            }
            TextButton(onClick = onDeleteClick) {
                Text(text = "Delete", color = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionDialog(question: Question, onDismiss: () -> Unit, onSave: (Question) -> Unit) {
    var text by remember { mutableStateOf(question.text) }
    var answer1 by remember { mutableStateOf(question.answer1) }
    var answer2 by remember { mutableStateOf(question.answer2) }
    var answer3 by remember { mutableStateOf(question.answer3) }
    var answer4 by remember { mutableStateOf(question.answer4) }
    var correctAnswer by remember { mutableStateOf(question.correctAnswer) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Question") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
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
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(Question(text, answer1, answer2, answer3, answer4, correctAnswer))
            }) {
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

data class Question(
    val text: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val correctAnswer: Int
)
