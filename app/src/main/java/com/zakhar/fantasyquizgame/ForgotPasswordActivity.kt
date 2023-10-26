package com.zakhar.fantasyquizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var forgotBinding : ActivityForgotPasswordBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.buttonForgotPassword.setOnClickListener {
            val email = forgotBinding.editTextForgotPasswordEmail.text.toString()

            auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "We sent a password reset to your email address", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }

        forgotBinding.textViewRememberPassword.setOnClickListener {
            finish()
        }
    }
}