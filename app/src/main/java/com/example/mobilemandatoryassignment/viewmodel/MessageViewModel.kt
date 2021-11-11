package com.example.mobilemandatoryassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobilemandatoryassignment.model.Message
import com.example.mobilemandatoryassignment.model.MessageRepository

class MessageViewModel : ViewModel() {
    private val repository = MessageRepository()
    val messagesLiveData: LiveData<List<Message>> = repository.messagesLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getMessages()
    }

    operator fun get(index: Int): Message? {
        return messagesLiveData.value?.get(index)
    }

    fun add(message: Message) {
        repository.add(message)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

}