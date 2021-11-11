package com.example.mobilemandatoryassignment.model

data class Comment(val id: Int, val messageId: Int, val content: String, val user: String) {
    constructor(messageId: Int, content: String, user: String) : this(-1, -1, content, user)

    override fun toString(): String {
        return  "$id, $messageId, $content, $user"
    }
}
