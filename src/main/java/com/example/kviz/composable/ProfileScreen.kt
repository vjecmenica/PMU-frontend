package com.example.kviz.composable

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kviz.R

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
