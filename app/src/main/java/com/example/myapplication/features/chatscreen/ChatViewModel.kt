package com.example.myapplication.features.chatscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.ChatRepository
import com.example.myapplication.network.datamodels.UserResponse
import com.example.myapplication.util.Command
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {
    private val _chatHistory = MutableLiveData<List<String>>()
    val chatHistory: LiveData<List<String>> = _chatHistory

    private val _sendCommandErrorMessage = MutableLiveData<String>()
    val sendCommandErrorMessage: LiveData<String> = _sendCommandErrorMessage

    private val _fetchFakeUsersErrorMessage = MutableLiveData<String>()
    val fetchFakeUsersErrorMessage: LiveData<String> = _fetchFakeUsersErrorMessage

    private val _storeFakeUsersErrorMessage = MutableLiveData<String>()
    val storeFakeUsersErrorMessage: LiveData<String> = _storeFakeUsersErrorMessage

    private val _queryFakeUsersErrorMessage = MutableLiveData<String>()
    val queryFakeUsersErrorMessage: LiveData<String> = _queryFakeUsersErrorMessage

    private var fakeUsersData: List<UserResponse>? = null

    fun sendBotCommand(context: Context, command: String) {
        try {
            // Copy the current chat history.
            val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()

            // Check if the bot command is valid.
            val verifiedCommand = checkCommand(command.lowercase())

            // Transform the bot command into a response.
            val transformedCommand = transformCommand(context, verifiedCommand)

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
            Command.STORE_USER_DATA.toString() -> Command.STORE_USER_DATA
            Command.QUERY_USER_DATA.toString() -> Command.QUERY_USER_DATA
            else -> {
                throw IllegalStateException("$command is not a valid command!")
            }
        }
    }

    // Do something with the bot command, and return the bot's response.
    private fun transformCommand(context: Context, command: Command): String {
        return when (command) {
            Command.HELP -> getHelpCommandResponse()
            Command.PING -> getPingCommandResponse()
            Command.FETCH_USER_DATA -> getFetchUserDataCommandResponse()
            Command.STORE_USER_DATA -> getStoreUserDataCommandResponse(context)
            Command.QUERY_USER_DATA -> getQueryUserDataCommandResponse(context)
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
                Command.STORE_USER_DATA -> "\n\t${command} - store any fake user data in a database"
                Command.QUERY_USER_DATA -> "\n\t${command} - query the database for a fake user"
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
            try {
                val apiResponse = repository.fetchFakeUsers()

                // Store the api response in a global variable for other commands!
                fakeUsersData = apiResponse

                // Create a string to hold a displayable list of fake users for the bot's response.
                var response = "Here are the fetched users:"
                for (user in apiResponse) {
                    response += "\n\t${user.name}"
                }

                // Add the list of fake users to the bot's chat log.
                val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()
                val botChatLog = "[bot] $response"
                newChatHistory.add(botChatLog)
                _chatHistory.value = newChatHistory
            } catch (e: Exception) {
                _fetchFakeUsersErrorMessage.value = e.message
            }
        }

        return "Fetching fake user data. Please wait!"
    }

    // Handle the store user data command, and return a response.
    private fun getStoreUserDataCommandResponse(context: Context): String {
        // Use Kotlin coroutines to store any fake user data.
        viewModelScope.launch {
            try {
                if (fakeUsersData == null) {
                    throw IllegalStateException("Cannot store user data before fetching user data!")
                }

                fakeUsersData?.let {
                    repository.storeFakeUserData(context, it)
                }

                // Add a message to the bot's chat log to show that the computation is complete.
                val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()
                val response = "Stored fake user data successfully!"
                val botChatLog = "[bot] $response"
                newChatHistory.add(botChatLog)
                _chatHistory.value = newChatHistory
            } catch (e: Exception) {
                _storeFakeUsersErrorMessage.value = e.message
            }
        }

        return "Storing fake user data. Please wait!"
    }

    // Handle the query user data command, and return a response.
    private fun getQueryUserDataCommandResponse(context: Context): String {
        // Use Kotlin coroutines to query the database for a fake user.
        viewModelScope.launch {
            try {
                // TODO: Let the user input the username. For now, we are querying for the username "Bret".
                val queryUserName = "Bret"
                val queriedUser = repository.queryDatabaseForFakeUser(context, queryUserName)

                // Add a message to the bot's chat log to show that the computation is complete.
                val newChatHistory = chatHistory.value?.toMutableList() ?: mutableListOf()
                val response = "Queried the database successfully! Here is the queried user:" +
                        "\n\tName: ${queriedUser.name}" +
                        "\n\tUsername: ${queriedUser.username}" +
                        "\n\tEmail: ${queriedUser.email}" +
                        "\n\tCity: ${queriedUser.city}" +
                        "\n\tCompany: ${queriedUser.company}"
                val botChatLog = "[bot] $response"
                newChatHistory.add(botChatLog)
                _chatHistory.value = newChatHistory
            } catch (e: Exception) {
                _queryFakeUsersErrorMessage.value = e.message
            }
        }

        return "Querying the database for a user. Please wait!"
    }
}