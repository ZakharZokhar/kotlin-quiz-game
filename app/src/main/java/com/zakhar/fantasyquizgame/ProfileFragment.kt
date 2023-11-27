package com.zakhar.fantasyquizgame

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.zakhar.fantasyquizgame.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater)
        val navController = findNavController()

        val spannableString = SpannableString("  Back")
        val drawable: Drawable = resources.getDrawable(R.drawable.baseline_arrow_back_24, null)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM)
        spannableString.setSpan(imageSpan, 0, 1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        profileBinding.backTextView.text = spannableString

        val user = FirebaseAuth.getInstance().currentUser

        profileBinding.textName.text = user?.displayName ?: ""
        profileBinding.textEmail.text = user?.email ?: ""


        profileBinding.backTextView.setOnClickListener {
            navController.popBackStack()
        }


        profileBinding.logOutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    navController.navigate(R.id.action_profileFragment_to_logInFragment)
                }
            }
        }

        return profileBinding.root
    }
}