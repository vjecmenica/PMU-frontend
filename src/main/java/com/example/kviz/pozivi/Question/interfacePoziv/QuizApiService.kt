package com.example.kviz.pozivi.Question.interfacePoziv

import com.example.kviz.pozivi.Question.QuizResponse
import retrofit2.http.GET

interface QuizApiService {
    @GET("questions/generateTenQuestions/1/1/1")
    suspend fun getQuizQuestions(): QuizResponse
}