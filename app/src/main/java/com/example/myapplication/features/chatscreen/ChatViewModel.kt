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
            val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()
            val verifiedCommand = checkCommand(command.lowercase())
            val transformedCommand = transformCommand(verifiedCommand)
            val userChatLog = "[you] $command"
            val botChatLog = "[bot] $transformedCommand"

            newChatHistory.add(userChatLog)
            newChatHistory.add(botChatLog)

            _chatHistory.value = newChatHistory
        } catch (e: Exception) {
            _sendCommandErrorMessage.value = e.message
        }
    }

    // Check if the bot command is valid, and if so, return the enum.
    private fun checkCommand(command: String): Command {
        return when (command) {
            Command.HELP.toString() -> Command.HELP
            else -> {
                throw IllegalStateException("$command is not a valid command!")
            }
        }
    }

    // Do something with the bot command, and return the bot's response.
    private fun transformCommand(command: Command): String {
        return when (command) {
            Command.HELP -> getHelpCommandResponse()
        }
    }

    // Handle the help command, and return a response.
    private fun getHelpCommandResponse(): String {
        var response = "Here is a list of the available commands:"

        for (command in Command.entries) {
            when (command) {
                Command.HELP -> response += "\n\t${command} - display all available commands"
            }
        }

        return response
    }
}