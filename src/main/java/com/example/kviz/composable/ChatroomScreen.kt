package com.example.kviz.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.example.kviz.ChatDest
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.ChatroomDto
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
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
fun ChatRoomsScreen(
    navController: NavHostController,
    dataStore: DataStore<Preferences>
) {
    // Inicijalizacija potrebnih objekata
    val gson = Gson()
    val userDtoKey = stringPreferencesKey("user_dto")

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var showDialog by remember { mutableStateOf(false) }
    var chatRoomName by remember { mutableStateOf("") }
    val userApi = retrofit.create(ChatroomApiService::class.java)

    // State za čuvanje lista chat soba
    var chatRooms by remember { mutableStateOf<List<ChatroomDto>>(emptyList()) }

    // Pokretanje korutine za preuzimanje podataka iz DataStore i API-ja
    LaunchedEffect(Unit) {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()
        Log.d("ChatRoomsScreen", "UserDto JSON iz DataStore: $userDtoJson")
        userDtoJson?.let { json ->
            val userDto = gson.fromJson(json, UserDto::class.java)
            Log.d("ChatRoomsScreen", "UserDto iz JSON-a: $userDto")
            if (userDto != null && userDto.userid != null) {
                try {
                    val response = userApi.getAllChatroomsForUser(userDto.userid)
                    Log.d("ChatRoomsScreen", "API poziv uspešan: ${response.isValid}")
                    if (response.isValid) {
                        chatRooms = response.dto // Ako je data lista, ovo treba promeniti
                    } else {
                        Log.e("ChatRoomsScreen", "Greška u API odgovoru: ${response.errorMessage}")
                    }
                } catch (e: Exception) {
                    Log.e("ChatRoomsScreen", "Mrežna greška: $e")
                }
            } else {
                Log.e("ChatRoomsScreen", "UserDto je null ili nema userId")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChatRooms", fontSize = 24.sp, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB183), // Narandžasta boja
                    titleContentColor = Color.White // Boja teksta
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true  }, containerColor = Color(0xFFFFB183)) { // Narandžasta boja
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
                    ChatRoomItem(navController, chatRoom,  dataStore)
                }
            }
        }
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Add Chat Room") },
            text = {
                Column {
                    TextField(
                        value = chatRoomName,
                        onValueChange = { chatRoomName = it },
                        label = { Text("Chat Room Name") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Dodavanje slike (koristimo placeholder jer nema opcije za biranje slike)
                    Image(
                        painter = painterResource(id = R.drawable.background_login), // Zameni sa pravim resursom
                        contentDescription = "Chat Room Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )
                    // Dodavanje slike može biti prošireno sa pickerom za izbor slike iz galerije
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Pokrenite korutinu za API poziv
                        CoroutineScope(Dispatchers.IO).launch {
                            val userDtoJson = dataStore.data.map { preferences ->
                                preferences[userDtoKey]
                            }.firstOrNull()

                            userDtoJson?.let { json ->
                                val userDto = gson.fromJson(json, UserDto::class.java)
                                if (userDto != null && userDto.userid != null) {
                                    val newChatroom = ChatroomDto(
                                        chatroomId = null,
                                        name = chatRoomName,
                                        owner = userDto
                                    )
                                    try {
                                        val response = userApi.addChatroom(newChatroom)
                                        if (response.isValid) {
                                            withContext(Dispatchers.Main) {
                                                // Dodaj novi chatroom u listu
                                                chatRooms = chatRooms.plusElement(response.dto)
                                                showDialog = false
                                            }
                                        } else {
                                            Log.e("ChatRoomsScreen", "Greška u API odgovoru prilikom dodavanja: ${response.errorMessage}")
                                        }
                                    } catch (e: Exception) {
                                        Log.e("ChatRoomsScreen", "Mrežna greška prilikom dodavanja: $e")
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ChatRoomItem(
    navController: NavHostController,
    chatroomDto: ChatroomDto,
    dataStore: DataStore<Preferences>
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
        Text(text = chatroomDto.name, color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    val gson = Gson()
                    val chatroomDtoKey = stringPreferencesKey("chatroom_dto")
                    val chatroomDtoJson = gson.toJson(chatroomDto)

                    dataStore.edit { preferences ->
                        preferences[chatroomDtoKey] = chatroomDtoJson
                    }.apply { }
                    navController.navigate(ChatDest.createRoute(chatroomDto.name))
                }
            }
        }) {
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Enter")
        }
    }
}
