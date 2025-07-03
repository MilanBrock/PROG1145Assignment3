package com.conestoga.prog1145assignment3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
This activity shows the list of the current saved tasks to the user
It allows different activities like adding, editing and deleting.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        // Get the Shared Preferences
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("dark_theme", false)) {
            setTheme(R.style.Theme_day)
        } else {
            setTheme(R.style.Theme_night)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To retrieve the tasks from the database
        dbHelper = TaskDBHelper(this)
        // Set up the recyclerview
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Theme toggle
        val switchTheme = findViewById<Switch>(R.id.switchTheme)
        switchTheme.isChecked = prefs.getBoolean("dark_theme", false)
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_theme", isChecked).apply()
            recreate()
        }

        // Set up the floating action button
        val fab = findViewById<FloatingActionButton>(R.id.fabAddTask)
        fab.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        loadTasks()
    }

    private fun loadTasks() {
        // Use the DB helper to gather all the tasks
        val tasks = dbHelper.getAllTasks().toMutableList()
        // Add the click listeners for editing the task
        adapter = TaskAdapter(
            tasks,
            onItemClick = { },
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
        enableSwipeToDelete(tasks)
    }

    // When the card is swiped, the DB helper is triggered to delete the task
    private fun enableSwipeToDelete(taskList: MutableList<Task>) {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.taskList[position]
                dbHelper.deleteTaskById(task.id)
                adapter.taskList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }
}