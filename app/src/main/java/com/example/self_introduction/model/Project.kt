package com.example.self_introduction.model

data class Project(
    val title: String,
    val description: String,
    val githubUrl: String,
    val techStack: List<String>
)

