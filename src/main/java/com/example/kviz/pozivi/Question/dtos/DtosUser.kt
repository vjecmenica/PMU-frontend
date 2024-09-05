package com.example.kviz.pozivi.Question.dtos

data class UserDto(
    val userid: Int,
    val username: String,
    val password: String,
    val name: String,
    val surname: String
)

data class PmuResponse<T>(
    val dto: T,
    val errorMessage: String?,
    val isValid: Boolean
)

data class UserProfileDto(
    val userDto: UserDto,
    val chatroomCount: Int,
    val sectionCount: Int,
    val rate: Int,
    val points: Int
)
