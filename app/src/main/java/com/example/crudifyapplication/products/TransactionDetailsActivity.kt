package com.example.crudifyapplication.products

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.crudifyapplication.R
import com.example.crudifyapplication.data.DatabaseHelper
import com.example.crudifyapplication.products.ProductListActivity

class TransactionDetailsActivity : AppCompatActivity() {
    private var tableLayout: TableLayout? = null
    private var dbHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaction_details)

        tableLayout = findViewById(R.id.tableLayout) // Reference to the TableLayout
        dbHelper = DatabaseHelper(this)

        loadDataIntoTable() // Load and populate the table with data

        // Set up "Go to Product List" button functionality
        val goToProductListButton = findViewById<View>(R.id.goToProductListButton)
        goToProductListButton.setOnClickListener {
            val intent = Intent(this@TransactionDetailsActivity, ProductListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadDataIntoTable() {
        // Correct the method call to use allTransactions (which is defined in DatabaseHelper)
        val cursor: Cursor? = dbHelper?.allTransactions

        cursor?.let {
            if (it.count == 0) {
                return@let // No data found, exit early
            }

            // Iterate through each record in the database
            while (it.moveToNext()) {
                val tableRow = TableRow(this)
                tableRow.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )

                // Create TextViews for each column (ID, Name, Quantity)
                val idText = TextView(this)
                idText.text = it.getInt(0).toString() // ID
                idText.setPadding(8, 5, 5, 5)
                idText.gravity = Gravity.START // Align to left
                tableRow.addView(idText)

                val nameText = TextView(this)
                nameText.text = it.getString(1) // Name
                nameText.setPadding(5, 5, 5, 5)
                nameText.gravity = Gravity.CENTER // Center align
                tableRow.addView(nameText)

                val quantityText = TextView(this)
                quantityText.text = it.getInt(2).toString() // Quantity
                quantityText.setPadding(5, 5, 8, 5)
                quantityText.gravity = Gravity.END // Align to right
                tableRow.addView(quantityText)

                // Optional: Add delete icon functionality (if implemented)
//                val deleteIcon = ImageView(this)
//                deleteIcon.setImageResource(R.drawable.baseline_delete_24) // Use an actual delete icon resource
//                deleteIcon.setPadding(5, 5, 5, 5)
//                deleteIcon.setOnClickListener {
//                    val idToDelete = it.getInt(0) // Get ID for deletion
//                    dbHelper?.deleteTransaction(idToDelete) // Implement this method in DatabaseHelper
//                    tableLayout?.removeView(tableRow) // Remove the row from the table
//                }
//                tableRow.addView(deleteIcon)

                // Add the TableRow to the TableLayout
                tableLayout?.addView(tableRow)
            }
            it.close() // Close the cursor to avoid memory leaks
        }
    }
}
