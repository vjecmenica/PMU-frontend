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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kviz.ChatDest
import com.example.kviz.ChatRoomsDest
import com.example.kviz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomsScreen(
    navController: NavHostController
) {
    val chatRooms = listOf("Chat Room 1", "Chat Room 2", "Chat Room 3") // Hardcode-ovane vrednosti

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: ChatRoomsDest.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChatRooms", fontSize = 24.sp, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB183), // Narandzasta boja
                    titleContentColor = Color.White // Boja teksta
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Akcija */ }, containerColor = Color(0xFFFFB183)) { // Narandžasta boja
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF8A4FFF)) // Ljubičasta pozadina
                    .padding(paddingValues) // Dodaj padding iz Scaffold-a
            ) {
                items(chatRooms) { chatRoom ->
                    ChatRoomItem(navController, chatRoom)
                }
            }
        }

    )
}

@Composable
fun ChatRoomItem(
    navController: NavHostController,
    chatRoom: String
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
        Text(text = chatRoom, color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            navController.navigate(ChatDest.route)
        }) {
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Enter")
        }
    }
}