package com.example.kviz.pozivi.Question.dtos

data class QuizDto(
    val quizId: Int,
    val chatroom: ChatroomDto
)

data class ParticipationDto(
    val participationId: Int,
    val result: Float,
    val userDto: UserDto,
    val quizDto: QuizDto
)