package com.example.self_introduction.viewmodel

import androidx.lifecycle.ViewModel
import com.example.self_introduction.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectsViewModel : ViewModel() {

    private val _projects = MutableStateFlow(
        listOf(
            Project(
                title = "Self Introduction App",
                description = "An Android portfolio app showcasing personal skills and projects with a modern UI.",
                githubUrl = "https://github.com/yourusername/self-introduction",
                techStack = listOf("Kotlin", "Android", "MVVM")
            ),
            Project(
                title = "Weather App",
                description = "A weather forecast application using OpenWeatherMap API with a clean and intuitive interface.",
                githubUrl = "https://github.com/yourusername/weather-app",
                techStack = listOf("Kotlin", "Retrofit", "LiveData")
            ),
            Project(
                title = "Todo Manager",
                description = "A task management app with offline support using Room database and material design.",
                githubUrl = "https://github.com/yourusername/todo-manager",
                techStack = listOf("Kotlin", "Room", "Coroutines")
            ),
            Project(
                title = "Chat Application",
                description = "Real-time messaging app built with Firebase Firestore and Firebase Authentication.",
                githubUrl = "https://github.com/yourusername/chat-app",
                techStack = listOf("Kotlin", "Firebase", "Firestore")
            )
        )
    )
    val projects: StateFlow<List<Project>> = _projects
}

