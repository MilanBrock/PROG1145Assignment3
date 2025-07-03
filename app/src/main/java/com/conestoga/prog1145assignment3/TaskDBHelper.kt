package com.conestoga.prog1145assignment3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TITLE TEXT,
                $DESCRIPTION TEXT,
                $IS_COMPLETED INTEGER
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(task: Task) {
        val values = ContentValues().apply {
            put(TITLE, task.title)
            put(DESCRIPTION, task.description)
            put(IS_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun updateTask(task: Task) {
        val values = ContentValues().apply {
            put(TITLE, task.title)
            put(DESCRIPTION, task.description)
            put(IS_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        writableDatabase.update(TABLE_NAME, values, "$ID=?", arrayOf(task.id.toString()))
    }

    fun deleteTaskById(id: Int) {
        writableDatabase.delete(TABLE_NAME, "$ID=?", arrayOf(id.toString()))
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                    val description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                    val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(IS_COMPLETED)) == 1
                    tasks.add(Task(id, title, description, isCompleted))
                } while (cursor.moveToNext())
            }
        }
        return tasks
    }

    companion object {
        private const val DATABASE_NAME = "TaskDB"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "tasks"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IS_COMPLETED = "isCompleted"
    }
}