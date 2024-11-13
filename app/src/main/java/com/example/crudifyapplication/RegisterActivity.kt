package com.example.crudifyapplication

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameInput: TextInputEditText = findViewById(R.id.usernameInput)
        val emailInput: TextInputEditText = findViewById(R.id.emailInput)
        val passwordInput: TextInputEditText = findViewById(R.id.passwordInput)
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginLink: TextView = findViewById(R.id.loginLink)

        // Set up the "Already have an account? Login" link with colored text
        val fullText = "Already have an account? Login"
        val spannableString = SpannableString(fullText)
        val purpleColor = ContextCompat.getColor(this, R.color.dark_purple)
        val startIndex = fullText.indexOf("Login")
        spannableString.setSpan(
            ForegroundColorSpan(purpleColor),
            startIndex,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        loginLink.text = spannableString

        // Handle the register button click
        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Pass username to HomepageActivity instead of MainActivity
                val intent = Intent(this, HomepageActivity::class.java)
                intent.putExtra("USERNAME", username) // Sending username to HomepageActivity
                startActivity(intent)
                finish() // Close RegisterActivity
            }
        }

        // Handle the login link click
        loginLink.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }
}
