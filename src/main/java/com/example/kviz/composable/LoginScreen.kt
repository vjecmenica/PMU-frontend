package com.example.kviz.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kviz.ChatRoomsDest
import com.example.kviz.R
import com.example.kviz.pozivi.Question.interfacePoziv.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun SignInSignUpScreen(
    navController: NavHostController
) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi = retrofit.create(UserApiService::class.java)

    // State to track which button is selected
    var isSignIn by remember { mutableStateOf(true) }

    // Background Image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_login), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Bottom section with white background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp) // Adjust the height as needed
                .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Sign In/Sign Up Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { isSignIn = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSignIn) MaterialTheme.colorScheme.primary else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25.dp))
                    ) {
                        Text(
                            text = "Sign In",
                            color = if (isSignIn) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { isSignIn = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isSignIn) MaterialTheme.colorScheme.primary else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25.dp))
                    ) {
                        Text(
                            text = "Sign Up",
                            color = if (!isSignIn) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Fields
                if (isSignIn) {
                    SignInForm()
                } else {
                    SignUpForm()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    navController.navigate(ChatRoomsDest.route)
                }) {
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Enter")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Forgot Password
                Text(
                    text = "Forgot password?",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* Handle forgot password */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInForm() {
    // Sign In Form Fields
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Email") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Password") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm() {
    // Sign Up Form Fields
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "First Name") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Last Name") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Email") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Password") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

//@Preview(showBackground = true)
//@Composable
//fun SignInSignUpScreenPreview() {
//    SignInSignUpScreen()
//}
