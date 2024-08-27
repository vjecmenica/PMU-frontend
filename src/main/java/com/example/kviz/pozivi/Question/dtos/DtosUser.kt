package com.example.kviz.pozivi.Question.dtos

data class UserDto(
    val userId: Int?,
    val username: String,
    val password: String,
    val name: String,
    val surname: String
)

data class PmuResponse<T>(
    val data: T,
    val errorMessage: String?,
    val isValid: Boolean
)
