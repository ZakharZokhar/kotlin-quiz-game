package com.zakhar.fantasyquizgame

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zakhar.fantasyquizgame.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var resultBinding : FragmentResultBinding

    private val database = FirebaseDatabase.getInstance("https://fantasy-quiz-game-default-rtdb.europe-west1.firebasedatabase.app/")
    private val databaseReference = database.reference.child("scores")

    private val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var userCorrect = ""
    var userWrong = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultBinding = FragmentResultBinding.inflate(layoutInflater)

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let {
                    val userUID = it.uid

                    userCorrect = snapshot.child(userUID).child("correct").value.toString()
                    userWrong = snapshot.child(userUID).child("wrong").value.toString()

                    resultBinding.textViewScoreCorrect.text = userCorrect
                    resultBinding.textViewScoreWrong.text = userWrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        resultBinding.buttonPlayAgain.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack(R.id.homeFragment, true)
        }

        return resultBinding.root
    }


}