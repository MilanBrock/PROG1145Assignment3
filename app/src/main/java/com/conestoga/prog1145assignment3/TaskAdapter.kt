package com.conestoga.prog1145assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
The task adapter is used when populating elements on the screen with data about tasks
For example populating the task list on the main screen
 */
class TaskAdapter(
    val taskList: MutableList<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onEditClick: (Task) -> Unit,
    private val onCompletionToggle: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        val textDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxCompleted)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEditTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_list, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.textTitle.text = task.title
        holder.textDescription.text = task.description
        holder.checkBox.isChecked = task.isCompleted

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isCompleted
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCompletionToggle(task, isChecked)
        }

        holder.itemView.setOnClickListener { onItemClick(task) }
        holder.editButton.setOnClickListener { onEditClick(task) }
    }

    override fun getItemCount(): Int = taskList.size
}