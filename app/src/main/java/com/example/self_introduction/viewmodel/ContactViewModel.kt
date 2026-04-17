package com.example.self_introduction.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ContactFormState(
    val senderName: String = "",
    val senderEmail: String = "",
    val message: String = "",
    val isSent: Boolean = false
)

class ContactViewModel : ViewModel() {

    private val _formState = MutableStateFlow(ContactFormState())
    val formState: StateFlow<ContactFormState> = _formState

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(senderName = name)
    }

    fun updateEmail(email: String) {
        _formState.value = _formState.value.copy(senderEmail = email)
    }

    fun updateMessage(message: String) {
        _formState.value = _formState.value.copy(message = message)
    }

    fun onSendClicked() {
        _formState.value = _formState.value.copy(isSent = true)
    }

    fun resetSent() {
        _formState.value = _formState.value.copy(isSent = false)
    }
}

