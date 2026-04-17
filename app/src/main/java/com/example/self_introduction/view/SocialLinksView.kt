package com.example.self_introduction.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.self_introduction.databinding.ViewSocialLinksBinding

class SocialLinksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewSocialLinksBinding.inflate(LayoutInflater.from(context), this, true)

    var githubUrl: String = "https://github.com/yourusername"
    var linkedinUrl: String = "https://linkedin.com/in/yourusername"
    var emailAddress: String = "your.email@example.com"

    init {
        binding.btnGithub.setOnClickListener { openUrl(githubUrl) }
        binding.btnLinkedin.setOnClickListener { openUrl(linkedinUrl) }
        binding.btnEmail.setOnClickListener { sendEmail() }
    }

    fun setLinks(github: String, linkedin: String, email: String) {
        githubUrl = github
        linkedinUrl = linkedin
        emailAddress = email
        binding.btnGithub.setOnClickListener { openUrl(githubUrl) }
        binding.btnLinkedin.setOnClickListener { openUrl(linkedinUrl) }
        binding.btnEmail.setOnClickListener { sendEmail() }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
        }
        context.startActivity(intent)
    }
}

