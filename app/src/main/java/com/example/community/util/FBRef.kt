package com.example.community.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{
        private val database = Firebase.database

        val usersRef = database.getReference("users")
        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
    }
}