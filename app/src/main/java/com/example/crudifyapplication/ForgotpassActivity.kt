package com.example.crudifyapplication

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class ForgotpassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)

        val emailInput: TextInputEditText = findViewById(R.id.emailInput)
        val newPasswordInput: TextInputEditText = findViewById(R.id.newPasswordInput)
        val confirmPasswordInput: TextInputEditText = findViewById(R.id.confirmPasswordInput)
        val resetButton: Button = findViewById(R.id.resetButton)
        val loginLink: TextView = findViewById(R.id.loginLink)

        val loginText = "Remember your password? Login"
        val loginSpannable = SpannableString(loginText)
        val purpleColor = ContextCompat.getColor(this, R.color.dark_purple)
        val loginIndex = loginText.indexOf("Login")
        loginSpannable.setSpan(
            ForegroundColorSpan(purpleColor),
            loginIndex,
            loginText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        loginLink.text = loginSpannable

        resetButton.setOnClickListener {
            val email = emailInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (email.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomepageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }
}