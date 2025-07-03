package com.conestoga.prog1145assignment3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("dark_theme", false)) {
            setTheme(R.style.Theme_day)
        } else {
            setTheme(R.style.Theme_night)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TaskDBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTasks()

        // Add a theme switch to the UI (assuming you add a Switch with id switchTheme)
        val switchTheme = findViewById<Switch>(R.id.switchTheme)
        switchTheme.isChecked = prefs.getBoolean("dark_theme", false)
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_theme", isChecked).apply()
            recreate()
        }
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks().toMutableList()
        adapter = TaskAdapter(
            tasks,
            onItemClick = { /* show details if needed */ },
            onEditClick = { task ->
                val intent = Intent(this, EditTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                startActivity(intent)
            },
            onCompletionToggle = { task, isChecked ->
                task.isCompleted = isChecked
                dbHelper.updateTask(task)
            }
        )
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
}