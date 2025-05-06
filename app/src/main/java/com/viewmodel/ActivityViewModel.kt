package com.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.model.Activity
import com.model.Behavior
import com.model.Grooming
import java.text.SimpleDateFormat
import java.util.*

class ActivityViewModel : ViewModel() {

    private var nextId = 0

    // Lists to hold the respective items
    private val _activityLog = mutableStateListOf<Activity>()
    val activityLog: List<Activity> = _activityLog

    private val _groomingList = mutableStateListOf<Grooming>()
    val groomingList: List<Grooming> = _groomingList

    private val _behavioralNotes = mutableStateListOf<Behavior>()
    val behavioralNotes: List<Behavior> = _behavioralNotes

    // Method to add Activity
    fun addActivity(title: String, text: String) {
        val date = getCurrentDate()
        _activityLog.add(Activity(nextId++, title, text, date))
    }

    // Method to edit Activity
    fun editActivity(id: Int, newTitle: String, newText: String) {
        val index = _activityLog.indexOfFirst { it.id == id }
        if (index != -1) {
            _activityLog[index] = _activityLog[index].copy(title = newTitle, text = newText)
        }
    }

    // Method to delete Activity
    fun deleteActivity(id: Int) {
        _activityLog.removeAll { it.id == id }
    }

    // Method to add Grooming Task
    fun addGrooming(task: String, status: String) {
        val date = getCurrentDate()
        _groomingList.add(Grooming(nextId++, task, status, date))
    }

    // Method to edit Grooming Task
    fun editGrooming(id: Int, newTask: String, newStatus: String) {
        val index = _groomingList.indexOfFirst { it.id == id }
        if (index != -1) {
            _groomingList[index] = _groomingList[index].copy(task = newTask, status = newStatus)
        }
    }

    // Method to delete Grooming Task
    fun deleteGrooming(id: Int) {
        _groomingList.removeAll { it.id == id }
    }

    // Method to add Behavioral Note
    fun addBehavior(behavior: String, reaction: String, notes: String) {
        val date = getCurrentDate()
        _behavioralNotes.add(Behavior(nextId++, behavior, reaction, notes, date))
    }

    // Method to edit Behavioral Note
    fun editBehavior(id: Int, newBehavior: String, newReaction: String, newNotes: String) {
        val index = _behavioralNotes.indexOfFirst { it.id == id }
        if (index != -1) {
            _behavioralNotes[index] = _behavioralNotes[index].copy(
                behavior = newBehavior,
                reaction = newReaction,
                notes = newNotes
            )
        }
    }

    // Method to delete Behavioral Note
    fun deleteBehavior(id: Int) {
        _behavioralNotes.removeAll { it.id == id }
    }

    // Helper function to get the current date in "MMM dd, yyyy" format
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
