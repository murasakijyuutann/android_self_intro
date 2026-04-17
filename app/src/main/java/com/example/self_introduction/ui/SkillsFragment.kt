package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.self_introduction.databinding.FragmentSkillsBinding
import com.example.self_introduction.model.Skill
import com.example.self_introduction.viewmodel.ProfileViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SkillsFragment : Fragment() {

    private var _binding: FragmentSkillsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderSkills(viewModel.skills)
    }

    private fun renderSkills(skills: List<Skill>) {
        val grouped = skills.groupBy { it.category }
        binding.skillsContainer.removeAllViews()

        grouped.forEach { (category, skillList) ->
            // Category label
            val label = TextView(requireContext()).apply {
                text = category
                textSize = 18f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 24, 0, 8)
            }
            binding.skillsContainer.addView(label)

            // ChipGroup for skills
            val chipGroup = ChipGroup(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            skillList.forEach { skill ->
                val chip = Chip(requireContext()).apply {
                    text = skill.name
                    isClickable = false
                    isCheckable = false
                }
                chipGroup.addView(chip)
            }
            binding.skillsContainer.addView(chipGroup)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

