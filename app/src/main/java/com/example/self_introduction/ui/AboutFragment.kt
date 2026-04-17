package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.self_introduction.databinding.FragmentAboutBinding
import com.example.self_introduction.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            launch { viewModel.name.collect { binding.tvAboutName.text = "About $it" } }
            launch { viewModel.bio.collect { binding.tvBio.text = it } }
            launch { viewModel.quote.collect { binding.tvQuote.text = it } }
        }

        binding.socialLinks.setLinks(viewModel.githubUrl, viewModel.linkedinUrl, viewModel.emailAddress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

