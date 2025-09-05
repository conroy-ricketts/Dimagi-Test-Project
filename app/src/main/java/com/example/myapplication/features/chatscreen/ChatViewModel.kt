package com.example.myapplication.features.chatscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val _chatHistory = MutableLiveData<List<String>>()
    val chatHistory: LiveData<List<String>> = _chatHistory

    fun sendBotCommand(command: String) {
        val response = "[bot] Test bot response"
        val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()

        newChatHistory.add("[you] $command")
        newChatHistory.add(response)

        _chatHistory.value = newChatHistory
    }
}