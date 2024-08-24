package com.example.kviz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.graphics.vector.ImageVector

interface SnippetDestination {
    val icon: ImageVector
    val route: String
}

object ChatRoomsDest : SnippetDestination {
    override val icon = Icons.Default.MailOutline
    override val route = "chat_rooms"
}

object AddQuestionDest : SnippetDestination {
    override val icon = Icons.Default.Face
    override val route = "add_question"
}

object ProfileDest : SnippetDestination {
    override val icon = Icons.Default.AccountCircle
    override val route = "profile"
}

object ChatDest : SnippetDestination {
    override val icon = Icons.Default.Send
    override val route = "chat"
}

object LoginDest : SnippetDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "login"
}

val destinations = listOf(ChatRoomsDest, AddQuestionDest, ProfileDest)