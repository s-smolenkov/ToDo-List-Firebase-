package com.example.todolistfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfirebase.databinding.ListFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel: ToDoViewModel by viewModels()
    private lateinit var binding: ListFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabButton.setOnClickListener {
            val activity = requireActivity() as onAddClickListener
            activity.onFabClick()
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val activeAdapter = ToDoListAdapter {
            viewModel.updateTask(it.copy(taskStatus = !it.taskStatus))
        }

        val doneAdapter = ToDoListAdapter {
            viewModel.updateTask(it.copy(taskStatus = !it.taskStatus))
        }

        binding.todoList.layoutManager = LinearLayoutManager(requireContext())
        binding.todoList.adapter = activeAdapter
        binding.doneList.layoutManager = LinearLayoutManager(requireContext())
        binding.doneList.adapter = doneAdapter

        viewModel.activeTasks.observe(viewLifecycleOwner) { tasks ->
            activeAdapter.submitList(tasks)
            binding.todoTextview.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.doneTasks.observe(viewLifecycleOwner) { tasks ->
            doneAdapter.submitList(tasks)
            binding.doneTextview.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        }

        setupSwipeToDelete(binding.todoList, activeAdapter)
        setupSwipeToDelete(binding.doneList, doneAdapter)
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView, adapter: ToDoListAdapter) {
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val task = adapter.currentList[position]
                    viewModel.removeTask(task)
                }
            }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView)
    }
}
