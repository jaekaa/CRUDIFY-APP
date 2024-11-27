package com.example.crudifyapplication

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudifyapplication.data.DatabaseHelper
import com.example.crudifyapplication.products.TransactionDetailsActivity
import com.example.crudifyapplication.scanner.ScannedBarcodeActivity

class HomepageActivity : AppCompatActivity() {

    // Initialize UI components
    private lateinit var btnScanBarcode: Button
    private lateinit var btnShowData: Button
    private lateinit var saveButton: Button
    private lateinit var resultText: TextView
    private lateinit var dbHelper: DatabaseHelper

    private val scannedBarcodes = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage) // Ensure this matches your homepage layout file

        // Adjust insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        btnScanBarcode = findViewById(R.id.btnScanBarcode)
        resultText = findViewById(R.id.result_text)
        btnShowData = findViewById(R.id.btnShowData)
        saveButton = findViewById(R.id.goToProductListButton)

        dbHelper = DatabaseHelper(this) // Initialize the database helper

        // Retrieve the username from the intent
        val username = intent.getStringExtra("USERNAME")
        val crudifyText: TextView = findViewById(R.id.crudifyText)
        crudifyText.text = username ?: "CRUDIFY" // Display the username or default text

        // Set OnClickListeners with lambdas
        btnScanBarcode.setOnClickListener {
            val intent = Intent(this, ScannedBarcodeActivity::class.java)
            scanBarcodeLauncher.launch(intent)
        }

        btnShowData.setOnClickListener {
            displayAllData()
            val intent = Intent(this, TransactionDetailsActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            val scannedProductName = "Sample Product" // Replace with actual scanned product name
            val scannedQuantity = 1 // Replace with actual scanned quantity

            // Call method to update product quantity
            val isUpdated = dbHelper.insertOrUpdateProduct(scannedProductName, scannedQuantity)
            // Notify the user about the success or failure of the update
            Toast.makeText(this, if (isUpdated) "Product list updated!" else "Update failed. Please try again!", Toast.LENGTH_SHORT).show()
        }
    }

    // Barcode scanner result handling
    private val scanBarcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val intentData = data?.getStringExtra("intentData")

            if (!intentData.isNullOrEmpty()) {
                if (dbHelper.itemExists(intentData)) {
                    // Get the current quantity and increment it
                    val currentQuantity = dbHelper.getItemQuantity(intentData)
                    dbHelper.updateQuantity(intentData, currentQuantity + 1)
                } else {
                    // Insert new item with quantity 1
                    dbHelper.insertTransaction(intentData, 1)
                }

                scannedBarcodes.add(intentData)
                updateResultText()
            }
        }
    }

    // Update result text to show scanned barcodes
    private fun updateResultText() {
        resultText.text = scannedBarcodes.joinToString("\n")
    }

    // Display all data from the database
    private fun displayAllData() {
        val cursor: Cursor = dbHelper.allTransactions // No parentheses here
        if (cursor.count == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            return
        }

        val sb = StringBuilder()
        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getInt(0)).append(", ")
            sb.append("Name: ").append(cursor.getString(1)).append(", ")
            sb.append("Quantity: ").append(cursor.getInt(2)).append("\n")
        }
        resultText.text = sb.toString()
    }
}
