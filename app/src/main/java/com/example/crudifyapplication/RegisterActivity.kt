package com.example.crudifyapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        registerButton.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            finish() // This prevents the user from coming back to the register screen with the back button
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            // We don't call finish() here so the user can come back to register if needed
        }
    }
}