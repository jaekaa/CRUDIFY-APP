package com.example.crudifyapplication.products

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
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
import kotlinx.coroutines.*

class ProductListActivity : AppCompatActivity() {
    private lateinit var productButton: Button
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProductListAdapter
    private lateinit var scannedBarcodeTextView: TextView

    private val REQUEST_BARCODE_SCAN = 1001
    private var activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)

        dbHelper = DatabaseHelper(this)

        // Initialize UI components
        productButton = findViewById(R.id.createNewButton)
        productRecyclerView = findViewById(R.id.productRecyclerView)
        scannedBarcodeTextView = findViewById(R.id.scannedBarcodeTextView)

        // Setup RecyclerView
        productRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load data into RecyclerView
        loadDataIntoRecyclerView()

        // Handle "Create New Product" button click
        productButton.setOnClickListener {
            val intent = Intent(this, CreateTableActivity::class.java)
            startActivity(intent)
        }

        // Handle barcode scanner button click
        val scanBarcodeButton: ImageView = findViewById(R.id.scannerIcon)
        scanBarcodeButton.setOnClickListener {
            val intent = Intent(this, ScannedBarcodeActivity::class.java)
            startActivityForResult(intent, REQUEST_BARCODE_SCAN)
        }
    }

    // Load data into RecyclerView
    private fun loadDataIntoRecyclerView() {
        activityScope.launch(Dispatchers.IO) {
            val cursor: Cursor? = dbHelper.allProducts
            withContext(Dispatchers.Main) {
                if (!isFinishing) {  // Make sure the activity is still valid
                    cursor?.let {
                        adapter = ProductListAdapter(this@ProductListActivity, it, object : ProductListAdapter.OnProductActionListener {
                            override fun onEditClick(position: Int, productId: Int) {
                                // Navigate to EditProductActivity
                                val intent = Intent(this@ProductListActivity, EditProductActivity::class.java)
                                intent.putExtra("PRODUCT_ID", productId)
                                startActivity(intent)
                            }

                            override fun onDeleteClick(position: Int, productId: Int) {
                                // Delete product and refresh list
                                val isDeleted = dbHelper.deleteProduct(productId)
                                if (isDeleted) {
                                    loadDataIntoRecyclerView()  // Refresh the RecyclerView
                                    Toast.makeText(this@ProductListActivity, "Product deleted", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@ProductListActivity, "Failed to delete product", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                        productRecyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the RecyclerView when returning to this activity
        loadDataIntoRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all ongoing coroutines to prevent leaks
        activityScope.cancel()

        // Release database resources
        dbHelper.close()

        // Release RecyclerView cursor if needed
        if (::adapter.isInitialized) {
            adapter.swapCursor(null)
        }
    }

    // Handle scanned barcode result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BARCODE_SCAN && resultCode == RESULT_OK) {
            val barcode = data?.getStringExtra("intentData")

            // Display the scanned barcode
            scannedBarcodeTextView.text = "Scanned Barcode: $barcode"

            // Optionally, save the barcode to the database or use it for further operations
            // Assuming you have a method to add the scanned barcode to the database
            activityScope.launch(Dispatchers.IO) {
                // Insert or update the product in the database based on the barcode
                val success = barcode?.let { dbHelper.insertOrUpdateProduct(it, 1) } // Assume quantity = 1 for simplicity

                withContext(Dispatchers.Main) {
                    // Check the result and show a Toast if necessary
                    if (success == true) {
                        // Reload the data in the RecyclerView
                        loadDataIntoRecyclerView()
                        Toast.makeText(this@ProductListActivity, "Product added/updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ProductListActivity, "Failed to add/update product", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
