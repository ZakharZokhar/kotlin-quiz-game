package com.zakhar.fantasyquizgame

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private lateinit var logInBinding : FragmentLogInBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logInBinding = FragmentLogInBinding.inflate(layoutInflater)

        val navController = findNavController()

        logInBinding.buttonSignIn.setOnClickListener {
            val email = logInBinding.editTextLoginEmail.text.toString()
            val password = logInBinding.editTextLogInPassword.text.toString()

            signInUser(email, password, navController)

        }
        logInBinding.textViewSignUp.setOnClickListener {

            navController.navigate(R.id.action_logInFragment_to_signUpFragment)
        }
        logInBinding.textViewForgotPassword.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_forgotPasswordFragment)
        }

        return logInBinding.root
    }

    private fun signInUser(email: String, password: String, navController: NavController) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(requireContext().applicationContext, "Welcome!!", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_logInFragment_to_homeFragment)

            } else {
                Toast.makeText(requireContext().applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = LogInFragment()
    }
}