package com.example.kviz.composable

import android.util.Log


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.ParticipationDto
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.dtos.UserProfileDto
import com.example.kviz.pozivi.Question.interfacePoziv.ChatroomApiService
import com.example.kviz.pozivi.Question.interfacePoziv.UserApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProfileScreenOrigin(
    navController: NavHostController,
    dataStore: DataStore<Preferences>
) {
    // Pozadina ekrana
    Box(
        modifier = Modifier
            .fillMaxSize()
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
        var userProfileDto by remember { mutableStateOf<UserProfileDto?>(null) }
        var quizResults by remember { mutableStateOf<List<ParticipationDto>?>(null) }

        LaunchedEffect(Unit) {
            val userDtoJson = dataStore.data.map { preferences ->
                preferences[userDtoKey]
            }.firstOrNull()
            Log.d("Profile screen", "UserDto JSON iz DataStore: $userDtoJson")
            userDtoJson?.let { json ->
                userDto = gson.fromJson(json, UserDto::class.java)
            }

            Log.d("Profile screen", "UserDto: $userDto")

            userDto?.let { user ->
                userProfileDto = userApi.getUserInfoForProfile(userId = user.userid).dto
                Log.d("Profile screen", "userProfileDto: $userProfileDto")
                quizResults = chatroomApi.getResultByUserId(userId = user.userid).dto
                Log.d("Profile screen", "quizResults: $quizResults")
            }
        }

        Image(
            painter = rememberImagePainter(data = R.drawable.profil_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Venc i slika profila u sredini
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // Profilska ikonica unutar venca
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.face1),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Ime korisnika ispod profilne ikonice
            Text(
                text = (userDto?.name ?: ""),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Kartice sa statistikom

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White// Ljubičasta boja pozadine kartice
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Postavlja širinu kartice na 90% širine ekrana
                    .height(130.dp) // Visina kartice
                //.padding(16.dp) // Margin unutar bele kartice

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Log.d("Profile screen", "Points: ${userProfileDto?.points}")
                    Log.d("Profile screen", "Rate: ${userProfileDto?.rate}")

                    val rate_percenatege = (userProfileDto?.rate ?: 1) * 10
                    val played = (((userProfileDto?.points) ?: 1) / 10 / (userProfileDto?.rate ?: 1)) ?: 0
                    Log.d("Profile screen", "Played: $played")

                    CardInfoWithFraction(R.drawable.star_profile, userProfileDto?.points.toString(), "POINTS")
                    CardInfoWithFraction(R.drawable.rate, rate_percenatege.toString() + "%", "SUCCESS RATE")
                    CardInfoWithFraction(R.drawable.trophy, played.toString(), "PLAYED")
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Razmak pre tabele

            // Tabela sa istorijom kvizova
            QuizHistoryTable(quizResults ?: emptyList())

        }
    }
}

@Composable
fun CardInfoWithFraction(iconRes: Int, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified, // koristi originalne boje iz ikonica
            modifier = Modifier.size(36.dp)
        )
        Text(text = label, color = Color(0xFF8A4FFF), style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 0.30.toFloat())))
        Text(text = value, color = Color.Black)

    }
}


@Composable
fun QuizHistoryTable(quizResults: List<ParticipationDto>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
            .fillMaxWidth()
        //.align(Alignment.CenterHorizontally)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFB183)),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(80.dp))
            Text(
                text = "MATCH HISTORY",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .background(Color(0xFFFFB183))
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }


        // Prikaz tabele sa kvizovima
        val quizHistory = listOf(
            Pair("160", "ETF"),
            Pair("80", "RADOJE"),
            Pair("220", "SRB"),
            Pair("60", "GROBAR1"),
            Pair("250", "ALOOO"),
            Pair("260", "ALOOO"),
            Pair("260", "GROBAR1")
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(80.dp))
            Column (verticalArrangement = Arrangement.Center){
                Text(text = "POINTS", color = Color(0xFF8A4FFF))
            }
            Spacer(modifier = Modifier.width(40.dp))
            Column (verticalArrangement = Arrangement.Center){
                Text(text = "CHATROOM", color = Color(0xFF8A4FFF))
            }


            //Spacer(modifier = Modifier.width(8.dp))
        }

//        quizHistory.forEachIndexed { index, data ->
//            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5F5)
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(backgroundColor)
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.Start
//            ) {
//                Spacer(modifier = Modifier.width(100.dp))
//                Column (verticalArrangement = Arrangement.Center){
//                    Text(text = data.first, color = Color.Black)
//                }
//                Spacer(modifier = Modifier.width(60.dp))
//                Column (verticalArrangement = Arrangement.Center){
//                    Text(text = data.second, color = Color.Black)
//                }
//
//
//                //Spacer(modifier = Modifier.width(8.dp))
//            }
//        }

        quizResults.forEachIndexed { index, data ->
            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5F5)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(100.dp))
                Column (verticalArrangement = Arrangement.Center){
                    Text(text = data.result.toString(), color = Color.Black)
                }
                Spacer(modifier = Modifier.width(60.dp))
                Column (verticalArrangement = Arrangement.Center){
                    Text(text = data.quizDto.chatroom.name, color = Color.Black)
                }


                //Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun LeaderboardScreen(
    navController: NavHostController
) {
    // Postavi pozadinsku sliku
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(id = R.drawable.background_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Prikaz gornje slike
            Image(
                painter = painterResource(id = R.drawable.face1), // Zameni odgovarajućom slikom korisnika
                contentDescription = "User Image",
                modifier = Modifier

                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista za prikaz leaderboard-a
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(leaderboardItems) { item ->
                    LeaderboardItem(item)
                }
            }
        }

        // Donji App Bar
//        BottomAppBar(
//            containerColor = Color.White,
//            content = {
//                IconButton(onClick = { /* Handle click */ }) {
//                    Icon(Icons.Filled.Home, contentDescription = "Home")
//                }
//                Spacer(modifier = Modifier.weight(1f, true))
//                FloatingActionButton(
//                    onClick = { /* Handle FAB click */ },
//                    containerColor = Color.White
//                ) {
//                    Icon(Icons.Filled.Add, contentDescription = "Add")
//                }
//                Spacer(modifier = Modifier.weight(1f, true))
//                IconButton(onClick = { /* Handle click */ }) {
//                    Icon(Icons.Filled.Person, contentDescription = "Profile")
//                }
//            }
//        )
    }
}

@Composable
fun LeaderboardItem(item: LeaderboardItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Slika korisnika
        Image(
            painter = painterResource(id = R.drawable.olovka2), // Zameni odgovarajućom slikom
            contentDescription = "User Image",
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Detalji o korisniku
        Column {
            Text(item.name, style = MaterialTheme.typography.bodySmall)
            Text("Points: ${item.points}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Klasa za podatke o stavkama na leaderboard-u
data class LeaderboardItemData(val name: String, val points: Int)

// Uzorak podataka
val leaderboardItems = listOf(
    LeaderboardItemData("David", 1200),
    LeaderboardItemData("Ivan", 1500),
    LeaderboardItemData("Austin", 1100)
)
