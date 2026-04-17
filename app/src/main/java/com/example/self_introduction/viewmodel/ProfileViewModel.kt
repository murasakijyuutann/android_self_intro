package com.example.self_introduction.viewmodel

import androidx.lifecycle.ViewModel
import com.example.self_introduction.model.Skill
import com.example.self_introduction.model.TimelineEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileStats(
    val projectCount: Int,
    val skillCount: Int,
    val yearsExperience: Int
)

class ProfileViewModel : ViewModel() {

    private val _name = MutableStateFlow("Your Name")
    val name: StateFlow<String> = _name

    private val _tagline = MutableStateFlow("Android Developer")
    val tagline: StateFlow<String> = _tagline

    private val _bio = MutableStateFlow(
        "Hi! I'm a passionate Android developer who loves building beautiful and functional mobile apps. " +
        "I enjoy working with Kotlin, Jetpack components, and modern Android architecture patterns.\n\n" +
        "When I'm not coding, I love exploring new technologies and contributing to open-source projects."
    )
    val bio: StateFlow<String> = _bio

    private val _quote = MutableStateFlow("\"Code is poetry written in logic.\"")
    val quote: StateFlow<String> = _quote

    private val _stats = MutableStateFlow(ProfileStats(10, 15, 3))
    val stats: StateFlow<ProfileStats> = _stats

    val skills: List<Skill> = listOf(
        Skill("Kotlin", "Languages"),
        Skill("Java", "Languages"),
        Skill("Python", "Languages"),
        Skill("Android SDK", "Frameworks"),
        Skill("Jetpack Compose", "Frameworks"),
        Skill("MVVM", "Frameworks"),
        Skill("Room", "Frameworks"),
        Skill("Retrofit", "Frameworks"),
        Skill("Git", "Tools"),
        Skill("Android Studio", "Tools"),
        Skill("Firebase", "Tools"),
        Skill("Figma", "Tools")
    )

    val timeline: List<TimelineEvent> = listOf(
        TimelineEvent("2021", "Started Learning Android", "Began my journey into Android development with Java and Kotlin basics."),
        TimelineEvent("2022", "First App Published", "Published my first app on the Google Play Store."),
        TimelineEvent("2023", "Jetpack Compose", "Embraced modern UI development with Jetpack Compose."),
        TimelineEvent("2024", "Open Source Contributions", "Started contributing to open-source Android projects."),
        TimelineEvent("2025", "Professional Projects", "Worked on professional Android applications for real clients.")
    )

    // Social links - update with your actual URLs
    val githubUrl = "https://github.com/yourusername"
    val linkedinUrl = "https://linkedin.com/in/yourusername"
    val emailAddress = "your.email@example.com"
}

