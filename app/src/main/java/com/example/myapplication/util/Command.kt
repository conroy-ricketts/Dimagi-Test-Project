package com.example.myapplication.util

enum class Command(private val command: String) {
    HELP("help"),
    PING("ping"),
    FETCH_USER_DATA("fetchuserdata");

    override fun toString(): String {
        return command
    }
}