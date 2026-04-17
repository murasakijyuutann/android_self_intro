package com.example.self_introduction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.self_introduction.databinding.FragmentChinchiroBinding

class ChinchiroFragment : Fragment() {

    private var _binding: FragmentChinchiroBinding? = null
    private val binding get() = _binding!!

    private val diceFaces = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChinchiroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRoll.setOnClickListener {
            val d1 = (1..6).random()
            val d2 = (1..6).random()
            val d3 = (1..6).random()

            binding.tvDice1.text = diceFaces[d1 - 1]
            binding.tvDice2.text = diceFaces[d2 - 1]
            binding.tvDice3.text = diceFaces[d3 - 1]

            binding.tvResult.text = evaluateRoll(d1, d2, d3)
        }
    }

    private fun evaluateRoll(d1: Int, d2: Int, d3: Int): String {
        val dice = listOf(d1, d2, d3).sorted()
        return when {
            dice == listOf(4, 5, 6) -> "🎉 Shichi-Go-San! You win!"
            dice == listOf(1, 2, 3) -> "💀 Pichi! You lose!"
            d1 == d2 && d2 == d3 -> "🎲 Triple ${dice[0]}! Amazing!"
            d1 == d2 || d2 == d3 || d1 == d3 -> {
                val pair = if (d1 == d2) d1 else if (d2 == d3) d2 else d1
                val odd = listOf(d1, d2, d3).first { it != pair }
                "👀 Pair of $pair — Point: $odd"
            }
            else -> "🎲 No combination... Try again!"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

