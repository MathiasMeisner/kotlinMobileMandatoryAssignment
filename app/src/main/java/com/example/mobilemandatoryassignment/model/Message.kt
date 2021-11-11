package com.example.mobilemandatoryassignment.model

data class Message(val id: Int, val content: String, val user: String, val totalComments: Int ){
    constructor(content: String, user: String) : this(-1, content,user, -1)

    override fun toString(): String {
        return "$id  $content $user $totalComments"
    }
}
