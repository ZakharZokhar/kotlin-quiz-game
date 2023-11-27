package com.zakhar.fantasyquizgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var forgotBinding : FragmentForgotPasswordBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotBinding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        val navController = findNavController()

        forgotBinding.buttonForgotPassword.setOnClickListener {
            val email = forgotBinding.editTextForgotPasswordEmail.text.toString()

            auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext().applicationContext, "We sent a password reset to your email address", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(requireContext().applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }

        forgotBinding.textViewRememberPassword.setOnClickListener {
            navController.popBackStack()
        }

        return forgotBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = ForgotPasswordFragment()
    }
}