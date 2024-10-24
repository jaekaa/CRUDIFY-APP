package com.example.crudifyapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val emailInput: TextInputEditText = findViewById(R.id.emailInput)
        val passwordInput: TextInputEditText = findViewById(R.id.passwordInput)
        val signInButton: Button = findViewById(R.id.signInButton)
        val forgotPasswordLink: TextView = findViewById(R.id.forgotPasswordLink)

        val fullText = "Forgot your password? Reset Password"
        val spannableString = SpannableString(fullText)
        val purpleColor = ContextCompat.getColor(this, R.color.dark_purple)
        val startIndex = fullText.indexOf("Reset Password")
        spannableString.setSpan(
            ForegroundColorSpan(purpleColor),
            startIndex,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        forgotPasswordLink.text = spannableString

        signInButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val intent = Intent(this, HomepageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        forgotPasswordLink.setOnClickListener {
            val intent = Intent(this, ForgotpassActivity::class.java)
            startActivity(intent)
        }
    }
}