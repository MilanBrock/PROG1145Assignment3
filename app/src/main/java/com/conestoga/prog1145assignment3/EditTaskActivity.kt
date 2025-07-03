package com.conestoga.prog1145assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*


class EditTaskActivity : AppCompatActivity() {
    private lateinit var dbHelper: TaskDBHelper
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var checkBoxCompleted: CheckBox
    private lateinit var buttonUpdate: Button
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        dbHelper = TaskDBHelper(this)
        editTitle = findViewById(R.id.editTextTaskTitle)
        editDescription = findViewById(R.id.editTextTaskDescription)
        checkBoxCompleted = findViewById(R.id.checkBoxTaskCompleted)
        buttonUpdate = findViewById(R.id.buttonUpdateTask)

        // Get the task ID from intent
        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            Toast.makeText(this, "Error loading task.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load current task data from the database
        val task = dbHelper.getAllTasks().find { it.id == taskId }
        if (task != null) {
            editTitle.setText(task.title)
            editDescription.setText(task.description)
            checkBoxCompleted.isChecked = task.isCompleted
        } else {
            Toast.makeText(this, "Task not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        buttonUpdate.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val description = editDescription.text.toString().trim()
            val isCompleted = checkBoxCompleted.isChecked

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTask = Task(id = taskId, title = title, description = description, isCompleted = isCompleted)
            dbHelper.updateTask(updatedTask)
            Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}