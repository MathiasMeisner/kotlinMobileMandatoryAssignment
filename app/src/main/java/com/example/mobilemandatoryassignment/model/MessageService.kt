package com.example.mobilemandatoryassignment.model

import com.example.mobilemandatoryassignment.model.Message
import retrofit2.Call
import retrofit2.http.*

interface MessageService {
    @GET("messages")
    fun getAllMessages(): Call<List<Message>>

    @POST("messages")
    fun saveMessage(@Body message: Message): Call<Message>

    @DELETE("messages/{id}")
    fun deleteMessage(@Path("id") id: Int): Call<Message>
}