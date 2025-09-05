package com.example.myapplication.features.chatscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.util.Command

class ChatViewModel : ViewModel() {
    private val _chatHistory = MutableLiveData<List<String>>()
    val chatHistory: LiveData<List<String>> = _chatHistory

    private val _sendCommandErrorMessage = MutableLiveData<String>()
    val sendCommandErrorMessage: LiveData<String> = _sendCommandErrorMessage

    fun sendBotCommand(command: String) {
        try {
            val response = "[bot] Test bot response"
            val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()

            checkCommand(command.lowercase())

            newChatHistory.add("[you] $command")
            newChatHistory.add(response)

            _chatHistory.value = newChatHistory
        } catch (e: Exception) {
            _sendCommandErrorMessage.value = e.message
        }
    }

    private fun checkCommand(command: String): Command {
        return when (command) {
            Command.HELP.toString() -> Command.HELP
            else -> {
                throw IllegalStateException("$command is not a valid command!")
            }
        }
    }
}