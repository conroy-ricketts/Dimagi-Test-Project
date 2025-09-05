package com.example.myapplication.util

enum class Command(private val command: String) {
    HELP("help"),
    PING("ping"),
    FETCH_USER_DATA("fetchuserdata"),
    STORE_USER_DATA("storeuserdata"),
    QUERY_USER_DATA("queryuserdata");

    override fun toString(): String {
        return command
    }
}