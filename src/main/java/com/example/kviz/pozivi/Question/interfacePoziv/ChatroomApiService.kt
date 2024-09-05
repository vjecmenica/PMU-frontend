package com.example.kviz.pozivi.Question.interfacePoziv

import com.example.kviz.pozivi.Question.dtos.CategoryDto
import com.example.kviz.pozivi.Question.dtos.CategoryDtoScreen
import com.example.kviz.pozivi.Question.dtos.ChatroomDto
import com.example.kviz.pozivi.Question.dtos.PmuResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatroomApiService {

    @GET("membership/getAllChatroomsByUserId/{userId}")
    suspend fun getAllChatroomsForUser(@Path("userId") userId:Int): PmuResponse<List<ChatroomDto>>

    @POST("chatrooms/addChatroom")
    suspend fun addChatroom(@Body chatroomDto: ChatroomDto): PmuResponse<ChatroomDto>

    @GET("sections/allSections")
    suspend fun getAllSectionsForUser(): PmuResponse<List<CategoryDto>>

    @GET("sections/getUserSection/{userId}")
    suspend fun getSectionsForUser(@Path("userId") userId:Int ): PmuResponse<List<CategoryDto>>

    @GET("sections/getUserSectionScreen/{userId}")
    suspend fun getSectionsForUserScreen(@Path("userId") userId:Int ): PmuResponse<List<CategoryDtoScreen>>

    @POST("sections/newSection/{userId}")
    suspend fun createSection(@Body sectionDto: CategoryDto, @Path("userId") userId: Int): PmuResponse<CategoryDto>

    @DELETE("sections/deleteSection/{sectionId}")
    suspend fun deleteSection(@Path("sectionId") sectionId: Int): PmuResponse<CategoryDto>
}

//@GetMapping(value = "/getUserSectionScreen/{userId}")
//@Operation(summary = "Getting section (screen) for user.")
//public PmuResponse<List<SectionScreenDto>> getUserSectionsScreen(@PathVariable("userId") Integer userId){
//    return sectionService.getSectionForUserScreen(userId);
//}