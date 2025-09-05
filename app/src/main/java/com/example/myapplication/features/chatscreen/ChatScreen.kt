package com.example.myapplication.features.chatscreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatScreen(context: Context = LocalContext.current) {
    val viewModel: ChatViewModel = viewModel()
    val sendCommandErrorMessage by viewModel.sendCommandErrorMessage.observeAsState()
    val fetchFakeUsersErrorMessage by viewModel.fetchFakeUsersErrorMessage.observeAsState()
    val storeFakeUsersErrorMessage by viewModel.storeFakeUsersErrorMessage.observeAsState()
    val queryFakeUsersErrorMessage by viewModel.queryFakeUsersErrorMessage.observeAsState()

    sendCommandErrorMessage?.let {
        Toast.makeText(context, "There was an error sending the bot command!", Toast.LENGTH_LONG).show()
        Log.e("ChatScreen", "There was an error sending a command: $it")
    }
    fetchFakeUsersErrorMessage?.let {
        Toast.makeText(context, "There was an error fetching fake user data!", Toast.LENGTH_LONG).show()
        Log.e("ChatScreen", "There was an error fetching the fake user data: $it")
    }
    storeFakeUsersErrorMessage?.let {
        Toast.makeText(context, "There was an error storing the fake user data!", Toast.LENGTH_LONG).show()
        Log.e("ChatScreen", "There was an error storing the fake user data: $it")
    }
    queryFakeUsersErrorMessage?.let {
        Toast.makeText(context, "There was an error querying the fake user data!", Toast.LENGTH_LONG).show()
        Log.e("ChatScreen", "There was an error querying the fake user data: $it")
    }

    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        InputTextBox(context, viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        ChatHistory(viewModel)
    }
}

@Composable
fun InputTextBox(context: Context, viewModel: ChatViewModel) {
    var text by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text(text = "Enter a command!") }
        )
        Spacer(modifier = Modifier.width(20.dp))
        Button(onClick = { viewModel.sendBotCommand(context, text) }) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun ChatHistory(viewModel: ChatViewModel) {
    val chatHistory by viewModel.chatHistory.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(chatHistory) {  message ->
            Text(text = message)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}