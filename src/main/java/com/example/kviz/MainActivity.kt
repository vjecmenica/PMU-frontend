package com.example.kviz

//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import coil.compose.rememberAsyncImagePainter
//import coil.compose.rememberImagePainter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kviz.composable.ChatRoomsScreen
import com.example.kviz.composable.ChatScreen
import com.example.kviz.composable.LeaderboardScreen
import com.example.kviz.composable.QuizContent
import com.example.kviz.composable.ResultScreen
import com.example.kviz.composable.SignInSignUpScreen1
import com.example.kviz.composable.SplashScreen
import com.example.kviz.ui.theme.KvizTheme
import com.example.vezbazak1.composable.SectionDetailsScreen
import com.example.vezbazak1.composable.SectionsScreen

class MainActivity : ComponentActivity() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KvizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = currentBackStackEntry?.destination
                    val currentRoute = currentDestination?.route ?: LoginDest.route

                    // Initialize Retrofit and QuizApiService

                    // Fetch and display quiz questions
                    //QuizContent(quizApi)
                    //ChatRoomsScreen(navController)
                    //ChatScreen("Luka i kreteni")
                    //SignInSignUpScreen()
                    //LeaderboardScreen()
                    var showSplashScreen by remember { mutableStateOf(true) }

                    if (showSplashScreen) {
                        SplashScreen(onSplashComplete = {
                            showSplashScreen = false
                        })
                    }
                    else{
                        Scaffold(
                            bottomBar = {
                                if (    // prikazuje bottomBar samo ako smo na jednom od ova 3 screen-a
                                    currentDestination?.route == ChatRoomsDest.route ||
                                    currentDestination?.route == SectionsDest.route ||
                                    currentDestination?.route == ProfileDest.route
                                ) {
                                    BottomAppBar(containerColor = Color(0xFFFFB183)) { // Narandzasta boja
                                        destinations.forEach { navDestination ->
                                            NavigationBarItem(
                                                icon = {
                                                    Icon(
                                                        imageVector = navDestination.icon,
                                                        contentDescription = null,
                                                    )
                                                },
                                                label = { Text(text = navDestination.route) },
                                                selected = currentRoute.startsWith(navDestination.route),
                                                onClick = {
                                                    val newRoute = navDestination.route
                                                    navController.navigate(newRoute) {
                                                        launchSingleTop = true
                                                        restoreState = true
                                                        popUpTo(ChatRoomsDest.route) {
                                                            saveState = true
                                                            inclusive = false
                                                        }
                                                    }
                                                }
                                            )
                                        }

                                    }
                                }
                            }
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = LoginDest.route,
                                modifier = Modifier.padding(it),
                            ) {
                                composable(route = ChatRoomsDest.route) {
                                    ChatRoomsScreen(navController, dataStore)
                                }

                                composable(route = SectionsDest.route) {
                                    SectionsScreen(navController)
                                }
                                composable(route = ProfileDest.route) {
                                    LeaderboardScreen(navController)
                                }
                                composable(
                                    route = ChatDest.route,
                                    arguments = listOf(navArgument("chatName") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val chatName = backStackEntry.arguments?.getString("chatName")
                                    ChatScreen(navController = navController, chatRoomName = chatName ?: "")
                                }
                                composable(route = LoginDest.route) {
                                    SignInSignUpScreen1(navController, dataStore)
                                }
                                composable(
                                    route = AllQuestionsDest.route,
                                    arguments = listOf(navArgument("sectionId") { type = NavType.StringType })
                                ) { backStackEntry ->
                                    val sectionId = backStackEntry.arguments?.getString("sectionId")
                                    SectionDetailsScreen(navController = navController, sectionId = sectionId ?: "")
                                }
                                composable(route = QuizDest.route ) {
                                    QuizContent(navController = navController)
                                }
                                composable(
                                    route = ResultDest.route,
                                    arguments = listOf(navArgument("result") { type = NavType.IntType })
                                ) { backStackEntry ->
                                    val result = backStackEntry.arguments?.getInt("result")
                                    ResultScreen(points = result ?: 0,navController, {}, {})
                                }
                            }
                        }

                    }
                }
            }

        }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KvizTheme {
        Greeting("Android")
    }
}