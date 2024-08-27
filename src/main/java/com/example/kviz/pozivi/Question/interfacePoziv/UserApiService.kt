package com.example.kviz.pozivi.Question.interfacePoziv

import com.example.kviz.pozivi.Question.dtos.DtoString
import com.example.kviz.pozivi.Question.dtos.PmuResponse
import com.example.kviz.pozivi.Question.dtos.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    @GET("users/authenticateUser")
    suspend fun authenticateUser(@Body userDto: UserDto): PmuResponse<UserDto>

    @GET("users/getUser/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): PmuResponse<UserDto>

    @POST("users/addUser")
    suspend fun addUser(@Body userDto: UserDto): PmuResponse<UserDto>

    @DELETE("users/deleteUser/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Int): PmuResponse<DtoString>
}