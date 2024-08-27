package com.example.kviz.pozivi.Question.dtos

data class QuizResponse(
    val dto: List<QuestionDto>,
    val errorMessage: String?,
    val isValid: Boolean
)

data class QuestionDto(
    val questions: String,
    val section: String?,
    val answersDto: List<AnswerDto>,
    val qid: Int
)

data class AnswerDto(
    val answer: String,
    val isCorrect: Boolean,
    val answerId: Int,
    val qid: Int
)

//delete Question
data class QuestionDeleteResponse(
    val dto: DtoString,
    val errorMessage: String?,
    val isValid: Boolean
)

data class DtoString(
    val message: String?
)
//

//save question
data class QuestionSaveResponse(
    val dto: QuestionDto,
    val errorMessage: String?,
    val isValid: Boolean
)
//
