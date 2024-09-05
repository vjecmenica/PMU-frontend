package com.example.kviz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
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

    fun createRoute(userId: Int): String {
        return "chat_rooms/$userId"
    }
}

object ProfileDest : SnippetDestination {
    override val icon = Icons.Default.AccountCircle
    override val route = "profile"
}

object ChatDest : SnippetDestination {
    override val icon = Icons.Default.Send
    override val route = "chat/{chatName}/{chatroomId}"

    fun createRoute(chatName: String, chatroomId: Int): String {
        return "chat/$chatName/$chatroomId"
    }
}

object LoginDest : SnippetDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "login"
}

object SectionsDest : SnippetDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "sections"
}

object AllQuestionsDest : SnippetDestination {
    override val icon = Icons.Default.Delete
    override val route = "all_questions/{sectionId}"

    fun createRoute(sectionId: String): String {
        return "all_questions/$sectionId"
    }
}

object QuizDest : SnippetDestination {
    override val icon = Icons.Default.Info
    override val route = "quiz/{chatroomId}/{categoryId}/{userId}"
}

object ResultDest : SnippetDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "result/{result}/{chatroomId}"

    fun createRoute(result: Int, chatroomId: Int): String {
        return "result/$result/$chatroomId"
    }
}

object ChoseCategoryDest : SnippetDestination {
    override val icon = Icons.Default.List
    override val route = "chose_category/{chatroomId}/{userId}"

    fun createRoute(chatroomId: Int, userId: Int): String {
        return "chose_category/$chatroomId/$userId"
    }
}

val destinations = listOf(ChatRoomsDest, SectionsDest, ProfileDest)