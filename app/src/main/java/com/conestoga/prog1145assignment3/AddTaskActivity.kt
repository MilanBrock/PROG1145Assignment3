package com.conestoga.prog1145assignment3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*


class AddTaskActivity : AppCompatActivity() {
    private lateinit var dbHelper: TaskDBHelper
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var checkBoxCompleted: CheckBox
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        dbHelper = TaskDBHelper(this)
        editTitle = findViewById(R.id.editTextTaskTitle)
        editDescription = findViewById(R.id.editTextTaskDescription)
        checkBoxCompleted = findViewById(R.id.checkBoxTaskCompleted)
        buttonSave = findViewById(R.id.buttonSaveTask)

        buttonSave.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val description = editDescription.text.toString().trim()
            val isCompleted = checkBoxCompleted.isChecked

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(title = title, description = description, isCompleted = isCompleted)
            dbHelper.addTask(task)
            Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}