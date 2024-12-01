package com.example.crudifyapplication.products

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudifyapplication.R
import com.example.crudifyapplication.data.DatabaseHelper

class EditProductActivity : AppCompatActivity() {

    private lateinit var editProductName: EditText
    private lateinit var editProductQuantity: EditText
    private lateinit var saveButton: Button
    private var productId: Int = 0
    private var productName: String? = null
    private var productQuantity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product) // Your edit product layout

        // Retrieve data from Intent
        val productId = intent.getIntExtra("productId", 0)
        val productName = intent.getStringExtra("productName")
        val productQuantity = intent.getIntExtra("productQuantity", 0)

        // Initialize UI components
        editProductName = findViewById(R.id.editProductName)
        editProductQuantity = findViewById(R.id.editProductQuantity)
        saveButton = findViewById(R.id.saveButton)

        // Set the existing product data
        editProductName.setText(productName)
        editProductQuantity.setText(productQuantity.toString())

        // Handle save button click
        saveButton.setOnClickListener {
            val updatedName = editProductName.text.toString()
            val updatedQuantityString = editProductQuantity.text.toString()

            // Check for empty fields
            if (updatedName.isEmpty() || updatedQuantityString.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedQuantity = updatedQuantityString.toIntOrNull() ?: 0
            if (updatedQuantity == 0) {
                Toast.makeText(this, "Quantity must be a positive number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the product in the database (using DatabaseHelper)
            val dbHelper = DatabaseHelper(this)
            val success = dbHelper.updateProduct(productId, updatedName, updatedQuantity)

            // Show success or failure message and finish the activity
            if (success) {
                Toast.makeText(this, "Product updated successfully: $updatedName, Quantity: $updatedQuantity", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(this, "Error updating product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
