package com.example.crudifyapplication

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductListActivity : AppCompatActivity(), ProductListAdapter.OnProductActionListener {
    private lateinit var productButton: Button
    private lateinit var productRecyclerView: RecyclerView
    private var adapter: ProductListAdapter? = null
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)

        // Initialize dbHelper
        dbHelper = DatabaseHelper(this)

        // Set up views
        productButton = findViewById(R.id.createNewButton)
        productRecyclerView = findViewById(R.id.productRecyclerView)

        // Set layout manager for RecyclerView
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load data into RecyclerView
        loadDataIntoRecyclerView()

        // Set up button to navigate to CreateTableActivity
        productButton.setOnClickListener {
            val intent = Intent(this@ProductListActivity, CreateTableActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadDataIntoRecyclerView() {
        // Fetch data using the helper
        val cursor: Cursor = dbHelper.allTransactions // Correct property call

        // Check if cursor is not empty and then set up the adapter
        if (cursor.count > 0) {
            adapter = ProductListAdapter(this, cursor, this)
            productRecyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDataIntoRecyclerView() // Reload data when returning to the activity
    }

    override fun onEditClick(position: Int, productId: Int) {
        // Handle edit action here, e.g., by opening an edit screen
        Toast.makeText(this, "Edit Product ID: $productId", Toast.LENGTH_SHORT).show()
        // Start an edit activity if needed, passing the productId
    }

    override fun onDeleteClick(position: Int, productId: Int) {
        // Remove the product from the database and notify the adapter
        val deleted = dbHelper.deleteTransaction(productId) // Now returns a Boolean
        if (deleted) {
            loadDataIntoRecyclerView() // Refresh list
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show()
        }
    }
}