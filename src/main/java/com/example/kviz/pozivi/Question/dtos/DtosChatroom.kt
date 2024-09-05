package com.example.kviz.pozivi.Question.dtos

data class ChatroomDto(
    val chatroomId: Int?,
    val name: String,
    val owner: UserDto
)

data class ChatroomDto1(
    val chatroomId: Int?,
    val name: String,
    val owner: UserDto,
    var num: Int
)

data class CategoryDto(
    val sectionId: Int,
    val name: String,
    val numOfQuestions: Int
)

data class CategoryDtoScreen(
    val sectionDto: CategoryDto,
    val numOfQuestions: Int,
)
