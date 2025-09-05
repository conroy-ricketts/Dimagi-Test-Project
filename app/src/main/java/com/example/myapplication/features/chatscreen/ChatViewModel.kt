package com.example.myapplication.features.chatscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.ChatRepository
import com.example.myapplication.util.Command
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {
    private val _chatHistory = MutableLiveData<List<String>>()
    val chatHistory: LiveData<List<String>> = _chatHistory

    private val _sendCommandErrorMessage = MutableLiveData<String>()
    val sendCommandErrorMessage: LiveData<String> = _sendCommandErrorMessage

    fun sendBotCommand(command: String) {
        try {
            // Copy the current chat history.
            val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()

            // Check if the bot command is valid.
            val verifiedCommand = checkCommand(command.lowercase())

            // Transform the bot command into a response.
            val transformedCommand = transformCommand(verifiedCommand)

            // Update the chat history log
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
            Command.PING.toString() -> Command.PING
            Command.FETCH_USER_DATA.toString() -> Command.FETCH_USER_DATA
            else -> {
                throw IllegalStateException("$command is not a valid command!")
            }
        }
    }

    // Do something with the bot command, and return the bot's response.
    private fun transformCommand(command: Command): String {
        return when (command) {
            Command.HELP -> getHelpCommandResponse()
            Command.PING -> getPingCommandResponse()
            Command.FETCH_USER_DATA -> getFetchUserDataCommandResponse()
        }
    }

    // Handle the help command, and return a response.
    private fun getHelpCommandResponse(): String {
        var response = "Here is a list of the available commands:"

        for (command in Command.entries) {
            response += when (command) {
                Command.HELP -> "\n\t${command} - display help; args: [command]"
                Command.PING -> "\n\t${command} - test if the bot is active"
                Command.FETCH_USER_DATA -> "\n\t${command} - fetch some fake user data"
            }
        }

        return response
    }

    // Handle the ping command, and return a response.
    private fun getPingCommandResponse(): String {
        return "pong"
    }

    // Handle the fetch user data command, and return a response.
    private fun getFetchUserDataCommandResponse(): String {
        // Use Kotlin coroutines to fetch some fake user data.
        viewModelScope.launch {
            // Errors are handled in the sendBotCommand() function!
            val apiResponse = repository.fetchFakeUsers()

            // Create a string to hold the displayable list of fake users for the bot's response.
            var response = "Here are the fetched users:"
            for (user in apiResponse) {
                response += "\n\t${user.name}"
            }

            // Add the list of fake users to the bot's chat log.
            val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()
            val botChatLog = "[bot] $response"
            newChatHistory.add(botChatLog)
            _chatHistory.value = newChatHistory
        }

        return "Fetching fake user data. Please wait!"
    }
}