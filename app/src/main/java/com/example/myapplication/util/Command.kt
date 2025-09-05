package com.example.myapplication.util

enum class Command(private val command: String) {
    HELP("help");

    override fun toString(): String {
        return command
    }
}