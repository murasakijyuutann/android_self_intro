package com.example.self_introduction.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.self_introduction.databinding.FragmentContactBinding
import com.example.self_introduction.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

class ContactFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactViewModel by viewModels()

    private val recipientEmail = "your.email@example.com"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            val name = binding.etSenderName.text?.toString() ?: ""
            val email = binding.etSenderEmail.text?.toString() ?: ""
            val message = binding.etMessage.text?.toString() ?: ""

            if (name.isBlank() || email.isBlank() || message.isBlank()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subject = "Portfolio Contact from $name"
            val body = "From: $name\nEmail: $email\n\n$message"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$recipientEmail")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formState.collect { state ->
                if (state.isSent) {
                    Toast.makeText(requireContext(), "Message sent!", Toast.LENGTH_SHORT).show()
                    viewModel.resetSent()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

