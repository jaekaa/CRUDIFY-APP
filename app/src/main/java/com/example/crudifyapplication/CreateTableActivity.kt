package com.example.crudifyapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudifyapplication.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText

class CreateTableActivity : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_table)

        // Initialize the database helper
        dbHelper = DatabaseHelper(this)

        // Find views by ID
        nameEditText = findViewById(R.id.productNameInput)
        quantityEditText = findViewById(R.id.stockInput)
        saveButton = findViewById(R.id.saveButton)

        // Set up the save button's click listener
        saveButton.setOnClickListener {
            // Get values from the text input fields
            val name = nameEditText.text.toString()

            val quantity: Int
            // Convert quantity to integer, handle potential format exception
            try {
                quantity = quantityEditText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this,
                    "Please enter a valid quantity",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Insert data into the database
            val isInserted = dbHelper.insertTransaction(name, quantity)
            if (isInserted) {
                // Show a success message
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

                // Start ProductListActivity and finish this activity
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                finish() // Close CreateTableActivity
            } else {
                // Show an error message if data wasn't saved
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
