package com.example.self_introduction.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.self_introduction.databinding.ItemProjectBinding
import com.example.self_introduction.model.Project
import com.google.android.material.chip.Chip

class ProjectsAdapter(private var projects: List<Project>) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        with(holder.binding) {
            tvProjectTitle.text = project.title
            tvProjectDesc.text = project.description

            chipGroupTech.removeAllViews()
            project.techStack.forEach { tech ->
                val chip = Chip(chipGroupTech.context).apply {
                    text = tech
                    isClickable = false
                }
                chipGroupTech.addView(chip)
            }

            btnGithub.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(project.githubUrl))
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = projects.size

    fun updateProjects(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}

