package com.example.mobilemandatoryassignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobilemandatoryassignment.model.Comment
import com.example.mobilemandatoryassignment.model.CommentRepository

class CommentViewModel : ViewModel() {

    private val repository = CommentRepository()
    val commentLiveData: LiveData<List<Comment>> = repository.commentLiveData
    val errorCommentLiveData: LiveData<String> = repository.errorCommentLiveData
    var messageId: Int = 0

    init {
        reload(messageId)
    }

    fun reload(messageId: Int) {
        repository.getComments(messageId)
    }

    operator fun get(index: Int): Comment? {
        return commentLiveData.value?.get(index)
    }

    fun add(comment: Comment) {
        Log.d("apple", messageId.toString() + " " + comment )
        repository.addComment(messageId, comment)
    }

    fun delete(messageId: Int, commentId: Int) {
        repository.deleteComment(messageId, commentId)
    }

}