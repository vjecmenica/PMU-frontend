package com.example.kviz.composable


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.example.kviz.ChatRoomsDest
import com.example.kviz.R
import com.example.kviz.pozivi.Question.dtos.UserDto
import com.example.kviz.pozivi.Question.interfacePoziv.UserApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInForm(
    message: String,
    onSignIn: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    // Sign In Form Fields
    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text(text = "Username") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Password") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp),
        visualTransformation = PasswordVisualTransformation()
    )

    // Display validation error message if needed
    if (message.isNotEmpty()) {
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
    }

    Button(
        onClick = { onSignIn(username, password) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFB183)
        )
    ) {
        Text(text = "Sign In")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm(
    onSignUp: (String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    fun isPasswordValid(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val isValidLength = password.length >= 6
        return hasLetter && hasDigit && isValidLength
    }

    TextField(
        value = firstName,
        onValueChange = { firstName = it },
        label = { Text(text = "First Name") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = lastName,
        onValueChange = { lastName = it },
        label = { Text(text = "Last Name") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = username,
        onValueChange = { username = it },
        label = { Text(text = "Username") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Password") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp),
        visualTransformation = PasswordVisualTransformation()
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text(text = "Confirm password") },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(8.dp),
        visualTransformation = PasswordVisualTransformation()
    )

    // Display validation error message if needed
    if (message.isNotEmpty()) {
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Spacer(modifier = Modifier.height(16.dp))
    }

    Button(
        onClick = {
            if (firstName == "" || lastName == "" || username == "" || password == "") {
                message = "All fields are required!"
                return@Button
            }
            if (password != passwordConfirmation) {
                message = "Password and Confirm password fields must be the same!"
            }
            if (!isPasswordValid(password)) {
                message = "Password must have mininum of 6 caracters, including one letter and one number!"
                return@Button
            }
            onSignUp(firstName, lastName, username, password)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFB183)
        )
    ) {
        Text(text = "Sign Up")
    }
}

@Composable
fun SignInSignUpScreen1(
    navController: NavHostController,
    dataStore: DataStore<Preferences>
) {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userApi = retrofit.create(UserApiService::class.java)

    // State to track which button is selected
    var isSignIn by remember { mutableStateOf(true) }

    var message by remember { mutableStateOf("") }

    // Background Image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Bottom section with white background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
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
                            containerColor = if (isSignIn) Color(0xFFB995FF)/*MaterialTheme.colorScheme.primary*/ else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25.dp))
                    ) {
                        Text(
                            text = "Sign In",
                            color = if (isSignIn) Color.White else Color.Gray,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Button(
                        onClick = { isSignIn = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isSignIn) Color(0xFFB995FF)/*MaterialTheme.colorScheme.primary*/ else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(25.dp))
                    ) {
                        Text(
                            text = "Sign Up",
                            color = if (!isSignIn) Color.White else Color.Gray,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Fields and Button Actions
                if (isSignIn) {
                    SignInForm(message) { email, password ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = userApi.authenticateUser(
                                    UserDto(
                                        userid = 0,
                                        username = email,
                                        password = password,
                                        name = "",
                                        surname = ""
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    if (response.isValid) {
                                        Log.d("SignInForm", "Response data: ${response.dto}")
                                        val gson = Gson()
                                        val userDtoKey = stringPreferencesKey("user_dto")

                                        val userDtoJson = gson.toJson(response.dto)
                                        Log.d("DataStore", "UserDto sačuvan: $userDtoJson")
                                        // Čuvanje JSON stringa u DataStore
                                        dataStore.edit { preferences ->
                                            preferences[userDtoKey] = userDtoJson
                                        }.apply {  }
                                        navController.navigate(ChatRoomsDest.route)
                                    } else {
                                        // Hange error
                                    }
                                }
                            } catch (e: Exception) {
                                // Handle network error
                            }
                        }
                    }

                    // Display validation error message if needed
//                    if (message.isNotEmpty()) {
//                        Text(
//                            text = message,
//                            color = Color.Red,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    } else {
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }

                } else {
                    SignUpForm { firstName, lastName, email, password ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = userApi.addUser(
                                    UserDto(
                                        userid = 0,
                                        username = email,
                                        password = password,
                                        name = firstName,
                                        surname = lastName
                                    )
                                )
                                withContext(Dispatchers.Main) {
                                    if (response.isValid) {
                                        //navController.navigate(ChatRoomsDest.route)
                                        isSignIn=true
                                    } else {
                                        // Handle error (e.g., show a Toast or Snackbar)
                                    }
                                }
                            } catch (e: Exception) {
                                // Handle network error
                            }
                        }
                    }
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
