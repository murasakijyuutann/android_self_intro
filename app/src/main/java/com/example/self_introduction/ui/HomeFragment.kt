package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.self_introduction.R
import com.example.self_introduction.databinding.FragmentHomeBinding
import com.example.self_introduction.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private var tapCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.name.collect { binding.tvName.text = it }
            }
            launch {
                viewModel.tagline.collect { binding.tvTagline.text = it }
            }
            launch {
                viewModel.stats.collect { stats ->
                    binding.tvStatProjectsCount.text = stats.projectCount.toString()
                    binding.tvStatSkillsCount.text = stats.skillCount.toString()
                    binding.tvStatYearsCount.text = stats.yearsExperience.toString()
                }
            }
        }

        binding.socialLinks.setLinks(viewModel.githubUrl, viewModel.linkedinUrl, viewModel.emailAddress)

        binding.btnAboutMe.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_about)
        }

        binding.btnContact.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_contact)
        }

        // Easter egg: tap 5 times on the hidden emoji
        binding.tvEasterEgg.setOnClickListener {
            tapCount++
            if (tapCount >= 5) {
                tapCount = 0
                findNavController().navigate(R.id.action_home_to_chinchiro)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

