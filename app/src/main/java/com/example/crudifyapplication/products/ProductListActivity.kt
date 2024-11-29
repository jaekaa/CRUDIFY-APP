package com.example.crudifyapplication.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudifyapplication.R
import com.example.crudifyapplication.adapter.ProductListAdapter
import com.example.crudifyapplication.data.DatabaseHelper
import com.example.crudifyapplication.scanner.ScannedBarcodeActivity

class ProductListActivity : AppCompatActivity() {
    private var productButton: Button? = null
    private var productRecyclerView: RecyclerView? = null
    private var adapter: ProductListAdapter? = null
    private var dbHelper: DatabaseHelper? = null
    private var scannedBarcodeTextView: TextView? = null // To display the scanned barcode

    private val REQUEST_BARCODE_SCAN = 1001 // Define the request code for barcode scan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)

        dbHelper = DatabaseHelper(this)

        // Initialize views
        productButton = findViewById(R.id.createNewButton)
        productRecyclerView = findViewById(R.id.productRecyclerView)
        scannedBarcodeTextView = findViewById(R.id.scannedBarcodeTextView) // Correct TextView for barcode

        // Set up RecyclerView with a LinearLayoutManager
        productRecyclerView?.layoutManager = LinearLayoutManager(this)

        // Load data into RecyclerView
        loadDataIntoRecyclerView()

        // Set up button to navigate to CreateTableActivity
        productButton?.setOnClickListener {
            val intent = Intent(this@ProductListActivity, CreateTableActivity::class.java)
            startActivity(intent)
        }

        // Set up button to navigate to ScannedBarcodeActivity
        val scanBarcodeButton = findViewById<ImageView>(R.id.scannerIcon)
        scanBarcodeButton.setOnClickListener {
            val intent = Intent(this@ProductListActivity, ScannedBarcodeActivity::class.java)
            startActivityForResult(intent, REQUEST_BARCODE_SCAN)
        }
    }

    // Method to load data into RecyclerView
    private fun loadDataIntoRecyclerView() {
        val cursor = dbHelper?.allTransactions
        if (cursor != null) {
            adapter = ProductListAdapter(this, cursor, object : ProductListAdapter.OnProductActionListener {
                override fun onEditClick(position: Int, productId: Int) {
                    // Handle edit
                    Toast.makeText(this@ProductListActivity, "Edit Product ID: $productId", Toast.LENGTH_SHORT).show()
                }

                override fun onDeleteClick(position: Int, productId: Int) {
                    // Handle delete
                    dbHelper?.deleteTransaction(productId)
                    loadDataIntoRecyclerView() // Refresh the list
                    Toast.makeText(this@ProductListActivity, "Product deleted", Toast.LENGTH_SHORT).show()
                }
            })
            productRecyclerView?.adapter = adapter
        }
    }

    // Override onActivityResult to get the scanned barcode data
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BARCODE_SCAN && resultCode == RESULT_OK) {
            val barcode = data?.getStringExtra("intentData")
            if (!barcode.isNullOrEmpty()) {
                scannedBarcodeTextView?.text = "Scanned Barcode: $barcode"
                // Optionally, add the barcode to your product database
            }
        }
    }
}
