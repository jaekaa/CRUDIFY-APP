package com.example.scanner

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.crudifyapplication.DatabaseHelper
import com.example.crudifyapplication.R
import com.example.crudifyapplication.ScannedBarcodeActivity
import com.example.crudifyapplication.TransactionDetailsActivity

class MainActivity : AppCompatActivity() {
    var btnScanBarcode: Button? = null
    var btnShowData: Button? = null
    var saveButton: Button? = null
    var resultText: TextView? = null
    var scannedBarcodes: ArrayList<String?>? = null
    var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure you're using the correct layout file

        // Initialize UI components
        btnScanBarcode = findViewById<Button>(R.id.btnScanBarcode)
        resultText = findViewById<TextView>(R.id.result_text)
        btnShowData = findViewById<Button>(R.id.btnShowData)
        saveButton = findViewById<Button>(R.id.goToProductListButton) // The save button

        scannedBarcodes = ArrayList()
        dbHelper = DatabaseHelper(this) // Initialize your database helper

        // Set an OnClickListener on the scan barcode button
        btnScanBarcode.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@MainActivity,
                ScannedBarcodeActivity::class.java
            )
            scanBarcodeLauncher.launch(intent)
        })

        // Set an OnClickListener on the show data button
        btnShowData.setOnClickListener(View.OnClickListener {
            displayAllData()
            val intent = Intent(
                this@MainActivity,
                TransactionDetailsActivity::class.java
            )
            startActivity(intent)
        })

        // Set an OnClickListener on the save button
        saveButton.setOnClickListener(View.OnClickListener { // Assuming you have logic to get the scanned product details
            val scannedProductName = "Sample Product" // Replace with actual scanned product name
            val scannedQuantity = 1 // Replace with actual scanned quantity

            // Call method to update product quantity
            val isUpdated: Boolean =
                dbHelper.insertOrUpdateProduct(scannedProductName, scannedQuantity)
            // Notify the user about the success or failure of the update
            if (isUpdated) {
                Toast.makeText(this@MainActivity, "Product list updated!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Update failed. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Barcode scanner result handling
    private val scanBarcodeLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null && data.hasExtra("intentData")) {
                val intentData = data.getStringExtra("intentData")

                if (dbHelper.itemExists(intentData)) {
                    // Get the current quantity and increment it
                    val currentQuantity: Int = dbHelper.getItemQuantity(intentData)
                    dbHelper.updateQuantity(intentData, currentQuantity + 1)
                } else {
                    // Insert new item with quantity 1
                    dbHelper.insertTransaction(intentData, 1)
                }


                scannedBarcodes!!.add(intentData)
                updateResultText()
            }
        }
    }

    // Update result text to show scanned barcodes
    private fun updateResultText() {
        val sb = StringBuilder()
        for (barcode in scannedBarcodes!!) {
            sb.append(barcode).append("\n")
        }
        resultText!!.text = sb.toString()
    }

    // Display all data from the database
    private fun displayAllData() {
        val cursor: Cursor = dbHelper.getAllTransactions()
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
        resultText!!.text = sb.toString()
    }
}