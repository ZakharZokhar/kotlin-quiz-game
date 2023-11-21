package com.zakhar.fantasyquizgame

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zakhar.fantasyquizgame.databinding.ActivityMainBinding
import com.zakhar.fantasyquizgame.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        val navController = findNavController()

        //setSupportActionBar(homeBinding.toolbarMain)

        homeBinding.buttonStart.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_quizFragment)
        }

        return homeBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_profile -> {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_profileFragment)

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}