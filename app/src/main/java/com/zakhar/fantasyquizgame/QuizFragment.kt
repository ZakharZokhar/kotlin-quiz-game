package com.zakhar.fantasyquizgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zakhar.fantasyquizgame.databinding.FragmentQuizBinding
import kotlin.random.Random

class QuizFragment : Fragment() {

    private lateinit var quizBinding: FragmentQuizBinding

    private val database = FirebaseDatabase.getInstance("https://fantasy-quiz-game-default-rtdb.europe-west1.firebasedatabase.app/")
    private val databaseReference = database.reference.child("questions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 0

    private var userAnswer = ""
    private var userCorrect = 0
    private var userWrong = 0

    private lateinit var timer : CountDownTimer
    private val totalTime = 25000L
    private var timerContinue = false
    var leftTime = totalTime

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val scoreRef = database.reference

    val questions = HashSet<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizBinding = FragmentQuizBinding.inflate(layoutInflater)

        setQuestions()

        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()
        }

        quizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }
        quizBinding.textViewA.setOnClickListener {
            userAnswer = "a"
            markOptions(quizBinding.textViewA)
        }

        quizBinding.textViewB.setOnClickListener {
            userAnswer = "b"
            markOptions(quizBinding.textViewB)
        }

        quizBinding.textViewC.setOnClickListener {
            userAnswer = "c"
            markOptions(quizBinding.textViewC)
        }

        quizBinding.textViewD.setOnClickListener {
            userAnswer = "d"
            markOptions(quizBinding.textViewD)
        }

        return quizBinding.root
    }

    private fun gameLogic() {
        restoreOptions()

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (questionNumber < questions.size) {
                    val ref = questions.elementAt(questionNumber).toString()
                    question = snapshot.child(ref).child("q").value.toString()
                    answerA = snapshot.child(ref).child("a").value.toString()
                    answerB = snapshot.child(ref).child("b").value.toString()
                    answerC = snapshot.child(ref).child("c").value.toString()
                    answerD = snapshot.child(ref).child("d").value.toString()
                    correctAnswer = snapshot.child(ref).child("answer").value.toString()

                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD

                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()
                } else {
                    val dialogMessage = AlertDialog.Builder(requireActivity())
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!\nYou have answered all the questions. Do you want to see result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result") {dialogInterface, position ->
                        sendScore()
                        dialogInterface.dismiss()
                    }
                    dialogMessage.setNegativeButton("Play Again") {dialogInterface, position ->
                        val navController = findNavController()
                        navController.navigate(R.id.action_quizFragment_to_homeFragment)
                        dialogInterface.dismiss()
                    }
                    dialogMessage.create().show()

                }

                questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext().applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun markOptions(textView: TextView) {
        pauseTimer()

        if (correctAnswer == userAnswer) {

            textView.setBackgroundColor(Color.GREEN)
            userCorrect++
            quizBinding.textViewCorrect.text = userCorrect.toString()

        } else {
            textView.setBackgroundColor(Color.RED)
            userWrong++
            quizBinding.textViewWrong.text = userWrong.toString()
            findAnswer()
        }
        disableClickableOfOptions()
    }

    private fun findAnswer() {

        when(correctAnswer) {
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }

    }

    private fun disableClickableOfOptions() {
        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false
    }

    private fun restoreOptions() {
        quizBinding.textViewA.setBackgroundColor(Color.parseColor("#ACA7A7"))
        quizBinding.textViewB.setBackgroundColor(Color.parseColor("#ACA7A7"))
        quizBinding.textViewC.setBackgroundColor(Color.parseColor("#ACA7A7"))
        quizBinding.textViewD.setBackgroundColor(Color.parseColor("#ACA7A7"))

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true
    }

    private fun startTimer() {

        timer = object : CountDownTimer(leftTime, 1000) {
            override fun onTick(milliseconds: Long) {
                leftTime = milliseconds
                updateCountDownText()
            }

            override fun onFinish() {

                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text = "Sorry, time is up! Continue with next question."

            }

        }.start()

        timerContinue = true

    }

    private fun updateCountDownText() {
        val remainingTime : Int = (leftTime/1000).toInt()
        quizBinding.textViewTime.text = remainingTime.toString()
    }

    private  fun pauseTimer() {
        timer.cancel()
        timerContinue = false
    }

    private fun resetTimer() {
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    private fun sendScore() {
        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {

                Toast.makeText(requireContext().applicationContext, "Score saved successfully", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.navigate(R.id.action_quizFragment_to_resultFragment)

            }
        }

    }

    private fun setQuestions() {
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                do {
                    val number = Random.nextInt(1,questionCount + 1)
                    questions.add(number)
                } while (questions.size < 10)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}