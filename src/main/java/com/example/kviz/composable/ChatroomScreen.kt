package com.example.kviz.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.kviz.ChatDest
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.ChatroomDto1
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatroomScreenOrigin(
    navController: NavHostController,
    dataStore: DataStore<Preferences>
) {
    var chatrooms by remember { mutableStateOf<MutableList<ChatroomDto1>>(mutableListOf()) }
    var numsOfMemberships by remember { mutableStateOf<List<Int>>(emptyList()) }

    var searchQuery by remember { mutableStateOf("") }

    val gson = Gson()
    val userDtoKey = stringPreferencesKey("user_dto")

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ChatroomApiService::class.java)

    LaunchedEffect(Unit) {
        val userDtoJson = dataStore.data.map { preferences ->
            preferences[userDtoKey]
        }.firstOrNull()

        var i = 1
        userDtoJson?.let { json ->
            val userDto = gson.fromJson(json, UserDto::class.java)
            if (userDto != null && userDto.userid != null) i = userDto.userid
        }

        chatrooms = try {
            apiService.getAllChatroomsForUser(i).dto?.toMutableList() ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }

        numsOfMemberships = try {
            apiService.getNumOfMembershipForChatroomId(i).dto ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        Log.d("NUMS: ", numsOfMemberships.toString())
        Log.d("Chatrooms: ", chatrooms.toString())
        // Sada možemo da menjamo chatrooms jer je MutableList
        chatrooms = chatrooms.mapIndexed { index, chatroomDto ->
            if (index < numsOfMemberships.size) {
                chatroomDto.copy(num = numsOfMemberships[index])
            } else {
                chatroomDto
            }
        }.toMutableList()
        Log.d("Chatrooms: ", chatrooms.toString())
    }

    // Koristi Image komponentu za pozadinu
    Box(
        modifier = Modifier.fillMaxSize()
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
            // Slika na vrhu
            Image(
                painter = painterResource(id = R.drawable.chatroom_top_icon),
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(8.dp))

            // "Chatrooms" tekst
            Text(
                text = "Chatrooms",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Bela slova
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Search Bar
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

            // Filtrirane kategorije/chat sobe
            val filteredChats = chatrooms.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {

                items(filteredChats) { chatroom ->
                    ChatroomCard(
                        title = chatroom.name,
                        participants = "${chatroom.num} participants",
                        onClick = {
                            navController.navigate(ChatDest.createRoute(chatName = chatroom.name))
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ChatroomCard(
    title: String,
    participants: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(hoveredElevation = 10.dp, defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Bele kartice
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Naziv chat sobe
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A4FFF) // Ljubičasta slova
            )

            // Broj učesnika
            Text(
                text = participants,
                fontSize = 14.sp,
                color = Color(0xFF8A4FFF) // Ljubičasta slova
            )
        }
    }
}
