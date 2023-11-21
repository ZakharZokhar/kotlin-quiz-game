package com.zakhar.fantasyquizgame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var welcomeBinding : FragmentWelcomeBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        welcomeBinding = FragmentWelcomeBinding.inflate(layoutInflater)

        val alphaAnimation = AnimationUtils.loadAnimation(requireContext().applicationContext, R.anim.splash_anim)
        welcomeBinding.textViewSplash.startAnimation(alphaAnimation)

        val navController = findNavController()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val user = auth.currentUser

            if (user != null) {
                navController.navigate(R.id.action_welcomeFragment_to_homeFragment)
            } else {
                navController.navigate(R.id.action_welcomeFragment_to_logInFragment)
            }
        }, 5000)

        return welcomeBinding.root

    }
}