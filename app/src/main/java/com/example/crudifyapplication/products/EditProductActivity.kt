package com.example.crudifyapplication.products

import android.os.Bundle
import android.util.Log
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
    private lateinit var dbHelper: DatabaseHelper
    private var productId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product)

        dbHelper = DatabaseHelper(this)

        // Get the product ID from the intent
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        Log.d("EditProductActivity", "Product ID received: $productId")

        // Initialize views
        editProductName = findViewById(R.id.editProductName)
        editProductQuantity = findViewById(R.id.editProductQuantity)
        saveButton = findViewById(R.id.saveButton)

        // Load existing product details
        loadProductDetails()

        // Handle Save button click
        saveButton.setOnClickListener {
            val productName = editProductName.text.toString()
            val productQuantity = editProductQuantity.text.toString().toIntOrNull()

            if (productName.isEmpty() || productQuantity == null) {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update product in database
            val isUpdated = dbHelper.updateProduct(productId, productName, productQuantity)
            if (isUpdated) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close activity and return to the product list
            } else {
                Toast.makeText(this, "Error updating product. Please check product ID and try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProductDetails() {
        // Get product from the database by its ID
        val cursor = dbHelper.getProductById(productId)

        if (cursor?.moveToFirst() == true) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"))
            val productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("product_quantity"))
            editProductName.setText(productName)
            editProductQuantity.setText(productQuantity.toString())
            Log.d("EditProductActivity", "Loaded product: $productName, Quantity: $productQuantity")
        } else {
            Log.e("EditProductActivity", "No product found for ID: $productId")
        }

        cursor?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release any resources if needed
    }
}
