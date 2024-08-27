package com.example.kviz.pozivi.Question.interfacePoziv

import com.example.kviz.pozivi.Question.dtos.ChatroomDto
import com.example.kviz.pozivi.Question.dtos.PmuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatroomApiService {

    @GET("membership/getAllChatroomsByUserId/{userId}")
    suspend fun getAllChatroomsForUser(@Path("userId") userId:Int): PmuResponse<ChatroomDto>
}