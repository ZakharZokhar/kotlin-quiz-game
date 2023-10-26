package com.zakhar.fantasyquizgame

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileBinding : ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        val view = profileBinding.root

        val textView = profileBinding.backTextView

        val spannableString = SpannableString("  Back")
        val drawable: Drawable = resources.getDrawable(R.drawable.baseline_arrow_back_24, null)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
        spannableString.setSpan(imageSpan, 0, 1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        textView.text = spannableString

        val user = FirebaseAuth.getInstance().currentUser

        profileBinding.textName.text = user?.displayName ?: ""
        profileBinding.textEmail.text = user?.email ?: ""


        profileBinding.backTextView.setOnClickListener {
            finish()
        }


        profileBinding.logOutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        setContentView(view)
    }
}