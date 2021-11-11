package com.example.mobilemandatoryassignment.model

import retrofit2.Call
import retrofit2.http.*

interface CommentService {
    @GET("{messageId}/comments")
    fun getAllComments(@Path("messageId") messageId: Int) : Call<List<Comment>>

    @POST("{messageId}/comments")
    fun saveComment(@Path("messageId") messageId: Int, @Body comment: Comment): Call<Comment>

    @DELETE("{messageId}/comments/{commentId}")
    fun deleteComment(@Path("messageId") messageId: Int, @Path("commentId") commentId: Int): Call<Comment>
}