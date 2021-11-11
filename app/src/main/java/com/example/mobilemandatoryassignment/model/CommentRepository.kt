package com.example.mobilemandatoryassignment.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.mobilemandatoryassignment.view.SingleMessage

class CommentRepository {
    private val url = "https://anbo-restmessages.azurewebsites.net/api/messages/"

    private val commentService: CommentService
    val commentLiveData: MutableLiveData<List<Comment>> = MutableLiveData<List<Comment>>()
    val errorCommentLiveData: MutableLiveData<String> = MutableLiveData()
    val updateCommentLiveData: MutableLiveData<String> = MutableLiveData()
    val singlemessageBinding: SingleMessage = SingleMessage()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        commentService = build.create(CommentService::class.java)
        getComments(singlemessageBinding.messageId)
    }

    fun getComments(messageId: Int) {
        commentService.getAllComments(messageId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    commentLiveData.postValue(response.body())
                    errorCommentLiveData.postValue("")
                } else {
                    val comment = response.code().toString() + " " + response.message()
                    errorCommentLiveData.postValue(comment)
                    Log.d("APPLE", comment)
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                errorCommentLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun addComment(messageId: Int, comment: Comment) {
        commentService.saveComment(messageId, comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "ADDED: " + response.body())
                    updateCommentLiveData.postValue("Added: " + response.body())
                } else {
                    val comment = response.code().toString() + " " + response.message()
                    errorCommentLiveData.postValue(comment)
                    Log.d("APPLE", comment)
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                errorCommentLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun deleteComment(messageId: Int, commentId: Int) {
        commentService.deleteComment(messageId, commentId).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateCommentLiveData.postValue("Deleted: " + response.body())
                } else {
                    val comment = response.code().toString() + " " + response.message()
                    errorCommentLiveData.postValue(comment)
                    Log.d("APPLE", comment)
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                errorCommentLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }
}