package de.tecrox.quizapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import de.panjutorials.quizapp.Constants
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlinx.android.synthetic.main.activity_quiz_questions.view.*

class QuizQuestionsActivity : AppCompatActivity(), OnClickListener {

    private var mUserName: String? = null
    private var mCurrentPosition = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOption: Int = 0
    private var mCorrectAnswers: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        setQuestion()

        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    private fun setQuestion() {

        mQuestionList = Constants.getQuestions()

        defaultOptionsView()

        if (mCurrentPosition == mQuestionList!!.size) {
            btnSubmit.text = "BEENDEN"
        } else {
            btnSubmit.text = "BESTÄTIGEN"
        }

        val question = mQuestionList!![mCurrentPosition - 1]

        progressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition/" + progressBar.max

        tvQuestion.text = question!!.question
        ivFlag.setImageResource(question.image)
        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this, R.drawable.default_option_border
            )
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tvOptionOne -> {
                selectedOptionsView(tvOptionOne, 1)
            }
            R.id.tvOptionTwo -> {
                selectedOptionsView(tvOptionTwo, 2)
            }
            R.id.tvOptionThree -> {
                selectedOptionsView(tvOptionThree, 3)
            }
            R.id.tvOptionFour -> {
                selectedOptionsView(tvOptionFour, 4)
            }
            R.id.btnSubmit -> {
                if (mSelectedOption == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        } else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOption) {
                        answerView(mSelectedOption, R.drawable.wrong_option_border)
                    } else {
                        mCorrectAnswers++
                    }
                    answerView(question!!.correctAnswer, R.drawable.correct_option_border)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        btnSubmit.text = "BEENDEN"
                    } else {
                        btnSubmit.text = "NÄCHSTEN FRAGE"
                    }
                    mSelectedOption = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {

            1 -> {
                tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }

            2 -> {
                tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }

            3 -> {
                tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }

            4 -> {
                tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun selectedOptionsView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOption = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this, R.drawable.selected_option_border
        )
    }
}