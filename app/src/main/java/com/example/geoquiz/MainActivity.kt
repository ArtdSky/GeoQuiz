package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get

private const val TAG = "TAG MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private  var res : Int = 0
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var questionTextView : TextView
    private lateinit var cheatButton: Button
    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProviders.of(this)[QuizViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        trueButton = findViewById(R.id.btn_true)
        falseButton = findViewById(R.id.btn_false)
        nextButton = findViewById(R.id.btn_next)
        backButton = findViewById(R.id.btn_back)
        questionTextView = findViewById(R.id.tv_question)
        cheatButton = findViewById(R.id.cheat_button)
        updateQuestion()

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            falseButton.isEnabled = false

        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            trueButton.isEnabled = false
        }


        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            quizViewModel.isCheater = false
            updateQuestion()
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }
        backButton.setOnClickListener { view:View->
//            if(currentIndex < 1){
//                currentIndex = questionBank.size -1
//            } else{
//                currentIndex--
//            }
//
//            updateQuestion()
        }

        cheatButton.setOnClickListener { view : View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if( requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG ON PAUSE", "on pause called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
       savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText

        questionTextView.setText(questionTextResId)

    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId  = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}