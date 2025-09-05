package com.example.myapplication.features

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen() {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        InputTextBox()
        Spacer(Modifier.height(20.dp))
        ChatHistory()
    }
}

@Composable
fun InputTextBox() {
    var text by remember { mutableStateOf("") }

    Surface {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text(text = "Enter a command!") }
        )
    }
}

@Composable
fun ChatHistory() {
    val testData = listOf("test 1", "test 2", "test 3")

    LazyColumn {
        items(testData) {  message ->
            Text(text = message)
            Spacer(Modifier.height(10.dp))
        }
    }
}