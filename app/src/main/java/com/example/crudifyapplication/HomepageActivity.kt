package com.example.crudifyapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomepageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage) // Make sure this matches your Home Page layout file name

        // Adjust insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve the username from the intent
        val username = intent.getStringExtra("USERNAME")

        // Find the TextView and set the username text
        val crudifyText: TextView = findViewById(R.id.crudifyText)
        if (!username.isNullOrEmpty()) {
            crudifyText.text = username // Display the username
        } else {
            crudifyText.text = "CRUDIFY" // Default text if username is not available
        }

        // Set up the "Create New" button or other UI elements if needed
        val createNewBtn: Button = findViewById(R.id.createNewBtn)
        createNewBtn.setOnClickListener {
            // Add action for the Create New button here
        }
    }
}
