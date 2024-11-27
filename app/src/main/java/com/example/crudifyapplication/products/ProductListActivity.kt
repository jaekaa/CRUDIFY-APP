package com.example.crudifyapplication.products

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudifyapplication.R
import com.example.crudifyapplication.data.DatabaseHelper
import com.example.crudifyapplication.adapter.ProductListAdapter
import com.example.crudifyapplication.HomepageActivity

class ProductListActivity : AppCompatActivity(), ProductListAdapter.OnProductActionListener {
    private lateinit var homeIcon: ImageView
    private lateinit var createNewButton: Button
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list)

        dbHelper = DatabaseHelper(this)

        homeIcon = findViewById(R.id.homeIcon)
        createNewButton = findViewById(R.id.createNewButton)
        productRecyclerView = findViewById(R.id.productRecyclerView)

        productRecyclerView.layoutManager = LinearLayoutManager(this)

        loadDataIntoRecyclerView()

        homeIcon.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        }

        createNewButton.setOnClickListener {
            val intent = Intent(this, CreateTableActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadDataIntoRecyclerView() {
        val cursor: Cursor = dbHelper.allTransactions

        if (cursor.count > 0) {
            adapter = ProductListAdapter(this, cursor, this)
            productRecyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDataIntoRecyclerView()
    }

    override fun onEditClick(position: Int, productId: Int) {
        Toast.makeText(this, "Edit Product ID: $productId", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(position: Int, productId: Int) {
        val deleted: Boolean = dbHelper.deleteTransaction(productId)
        if (deleted) {
            loadDataIntoRecyclerView()
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show()
        }
    }
}