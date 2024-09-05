package com.example.kviz.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kviz.ChatDest
import com.example.kviz.R

@Composable
fun ResultScreen(
    points: Int,
    navController: NavHostController,
    onContinue: () -> Unit,
    onShare: () -> Unit,
    chatroomId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8A4FFF)) // Ljubičasta pozadina
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.znak_pitanja),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Nice Work",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = "You Achieved $points/10",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = { navController.navigate(ChatDest.createRoute(chatName = "Chat room", chatroomId = chatroomId))} ,//ovde dodati koji je chatroom
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color(0xFFFFA726), RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA726),
                contentColor = Color.White
            )
        ) {
            Text("Continue")
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color(0xFF6C63FF), RoundedCornerShape(8.dp)), // Plava boja za Share dugme
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF),
                contentColor = Color.White
            )
        ) {
            Text("Share")
        }
        Spacer(modifier = Modifier.weight(1f)) // Ispunjava preostali prostor
    }
}
