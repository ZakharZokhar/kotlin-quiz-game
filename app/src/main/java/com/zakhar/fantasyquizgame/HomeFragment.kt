package com.zakhar.fantasyquizgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zakhar.fantasyquizgame.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        val navController = findNavController()

        homeBinding.buttonStart.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_quizFragment)
        }

        homeBinding.profileImage.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }



        return homeBinding.root
    }
}