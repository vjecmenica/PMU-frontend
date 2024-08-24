package com.example.kviz.composable

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatRoomName: String,
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
                    IconButton(onClick = { /* Join quiz action */ }) {
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
