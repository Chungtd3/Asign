package com.example.model

data class Chats(
     var sender: String,
     var reciever: String,
     var message: String,
     var isseen: Boolean = false
)