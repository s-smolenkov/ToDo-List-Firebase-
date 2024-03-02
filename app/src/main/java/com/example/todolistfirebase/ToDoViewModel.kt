package com.example.todolistfirebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

    private val _tasks = repository.getTasks().asLiveData(viewModelScope.coroutineContext)

    val activeTasks: LiveData<List<ToDo>> = _tasks.map { tasks ->
        tasks.filter { !it.taskStatus }
    }

    val doneTasks: LiveData<List<ToDo>> = _tasks.map { tasks ->
        tasks.filter { it.taskStatus }
    }

    fun addTask(toDo: ToDo) = viewModelScope.launch {
        repository.addTask(toDo)
    }

    fun updateTask(toDo: ToDo) = viewModelScope.launch {
        repository.updateTask(toDo)
    }

    fun removeTask(toDo: ToDo) = viewModelScope.launch {
        repository.removeTask(toDo)
    }
}


