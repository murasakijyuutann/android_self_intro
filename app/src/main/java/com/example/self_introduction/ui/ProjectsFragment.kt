package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.self_introduction.adapter.ProjectsAdapter
import com.example.self_introduction.databinding.FragmentProjectsBinding
import com.example.self_introduction.viewmodel.ProjectsViewModel
import kotlinx.coroutines.launch

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProjectsViewModel by viewModels()
    private lateinit var adapter: ProjectsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProjectsAdapter(emptyList())
        binding.recyclerProjects.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProjects.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.projects.collect { projects ->
                adapter.updateProjects(projects)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

