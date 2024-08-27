package com.example.kviz.pozivi.Question.interfacePoziv

import com.example.kviz.pozivi.Question.dtos.QuestionDeleteResponse
import com.example.kviz.pozivi.Question.dtos.QuestionDto
import com.example.kviz.pozivi.Question.dtos.QuestionSaveResponse
import com.example.kviz.pozivi.Question.dtos.QuizResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizApiService {
    @GET("questions/generateTenQuestions/1/1/1")
    suspend fun getQuizQuestions(): QuizResponse

    @DELETE("questions/deleteQuestions/{questionId}")
    suspend fun deleteQuestion(@Path("questionId") questionId: Int): QuestionDeleteResponse

    @POST("questions/addQuestion")
    suspend fun addQuestion(@Body newQuestionRequest: QuestionDto): QuestionSaveResponse
}