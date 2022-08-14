package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "TAG QUIZ_VIEW_MODEL"

class QuizViewModel :ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_russia, true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true)
    )
     var currentIndex = 0

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText : Int get() = questionBank[currentIndex].textResId
    var isCheater = false
    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}