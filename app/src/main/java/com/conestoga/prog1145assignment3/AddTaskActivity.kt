package com.conestoga.prog1145assignment3

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*

/*
This activity is launched when the floating action button is clicked
It will allow the user to create a new task for the task list
 */
class AddTaskActivity : AppCompatActivity() {
    // The DBHelper is used to add the task to the database if successful
    private lateinit var dbHelper: TaskDBHelper

    // The input of the user
    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var checkBoxCompleted: CheckBox

    // The button used to request saving in the database
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        // Pull the selected theme from the SharedPreferences and set it
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("dark_theme", false)) {
            setTheme(R.style.Theme_day)
        } else {
            setTheme(R.style.Theme_night)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Set the local variables

        dbHelper = TaskDBHelper(this)
        editTitle = findViewById(R.id.editTextTaskTitle)
        editDescription = findViewById(R.id.editTextTaskDescription)
        checkBoxCompleted = findViewById(R.id.checkBoxTaskCompleted)
        buttonSave = findViewById(R.id.buttonSaveTask)

        // When the button is clicked, validate the input, if it passes, save the task
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