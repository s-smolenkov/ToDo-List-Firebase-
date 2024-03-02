package com.example.todolistfirebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ToDoRepository(database: FirebaseDatabase) {
    private val db = database.reference.child("tasks")

    fun addTask(toDo: ToDo) {
        toDo.id?.let { db.child(it).setValue(toDo) }
    }

    fun updateTask(toDo: ToDo) {
        toDo.id?.let { db.child(it).setValue(toDo) }
    }

    fun removeTask(toDo: ToDo) {
        toDo.id?.let { db.child(it).removeValue() }
    }

    fun getTasks() = callbackFlow {
        val listener = db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasks = snapshot.children.mapNotNull { it.getValue<ToDo>() }
                trySend(tasks).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Database error: ${error.message}")
            }
        })
        awaitClose { db.removeEventListener(listener) }
    }
}