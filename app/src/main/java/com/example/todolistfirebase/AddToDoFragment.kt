package com.example.todolistfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolistfirebase.databinding.AddTaskLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class AddToDoFragment : Fragment() {

    private lateinit var binding: AddTaskLayoutBinding
    private val viewModel: ToDoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTaskLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            val taskName = binding.taskInputField.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = ToDo(
                    id = UUID.randomUUID().toString(),
                    taskStatus = false,
                    taskName = taskName
                )
                viewModel.addTask(newTask)
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}