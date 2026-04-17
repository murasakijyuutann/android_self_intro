package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.self_introduction.databinding.FragmentJourneyBinding
import com.example.self_introduction.databinding.ItemTimelineBinding
import com.example.self_introduction.viewmodel.ProfileViewModel

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val events = viewModel.timeline
        binding.timelineContainer.removeAllViews()

        events.forEachIndexed { index, event ->
            val itemBinding = ItemTimelineBinding.inflate(layoutInflater, binding.timelineContainer, false)
            itemBinding.tvYear.text = event.year
            itemBinding.tvTimelineTitle.text = event.title
            itemBinding.tvTimelineDesc.text = event.description
            // Hide line for last item
            if (index == events.lastIndex) {
                itemBinding.line.visibility = View.INVISIBLE
            }
            binding.timelineContainer.addView(itemBinding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

