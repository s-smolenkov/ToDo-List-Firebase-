package com.example.todolistfirebase

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfirebase.databinding.TaskLayoutBinding

class ToDoListAdapter(private val onTaskStatusChanged: (ToDo) -> Unit) :
    ListAdapter<ToDo, ToDoListAdapter.ToDoViewHolder>(ToDoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding, onTaskStatusChanged)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ToDoViewHolder(
        private val binding: TaskLayoutBinding,
        private val onTaskStatusChanged: (ToDo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(toDo: ToDo) {
            binding.task.text = toDo.taskName
            binding.checkBox.isChecked = toDo.taskStatus

            updateTextStyle(toDo.taskStatus)

            binding.checkBox.setOnClickListener {
                val newStatus = !binding.checkBox.isChecked
                binding.checkBox.isChecked = newStatus
                onTaskStatusChanged(toDo.copy(taskStatus = newStatus))
                updateTextStyle(newStatus)
            }
        }

        private fun updateTextStyle(isChecked: Boolean) {
            if (isChecked) {
                binding.task.paintFlags = binding.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.task.paintFlags =
                    binding.task.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    class ToDoDiffCallback : DiffUtil.ItemCallback<ToDo>() {
        override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean = oldItem == newItem
    }
}
