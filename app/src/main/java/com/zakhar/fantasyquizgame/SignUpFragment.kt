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
import com.google.firebase.auth.UserProfileChangeRequest
import com.zakhar.fantasyquizgame.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var signUpBinding: FragmentSignUpBinding

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signUpBinding = FragmentSignUpBinding.inflate(layoutInflater)
        val navController = findNavController()

        signUpBinding.textViewSignIn.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_logInFragment)
        }

        signUpBinding.buttonSignUp.setOnClickListener {
            val email = signUpBinding.editTextSignUpEmail.text.toString()
            val password = signUpBinding.editTextSignUpPassword.text.toString()
            val name = signUpBinding.signUpName.text.toString()
            signUpWithFirebase(email, password, name, navController)

        }

        return signUpBinding.root
    }

    private fun signUpWithFirebase(email : String, password: String, name: String, navController: NavController,) {

        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.buttonSignUp.isClickable = false
        val context = requireContext()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Toast.makeText(context.applicationContext, "Your account has been successfully created", Toast.LENGTH_SHORT).show()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        user?.updateProfile(
                            UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build())

                        navController.navigate(R.id.action_signUpFragment_to_homeFragment)

                    } else {
                        Toast.makeText(context.applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                }

            } else {
                Toast.makeText(context.applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()

            }
            signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
            signUpBinding.buttonSignUp.isClickable = true
        }

    }
}