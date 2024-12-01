package com.example.crudifyapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudifyapplication.products.CreateTableActivity
import com.example.crudifyapplication.products.EditProductActivity
import com.example.crudifyapplication.scanner.ScannedBarcodeActivity

class HomepageActivity : AppCompatActivity() {

    private lateinit var homeIcon: ImageView
    private lateinit var textView: TextView
    private lateinit var createNewButton: Button
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList: MutableList<Product> = mutableListOf() // Empty product list
    private lateinit var scannerIcon: ImageView // Declare scannerIcon variable

    // Declare the ActivityResultLauncher
    private lateinit var barcodeResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_list) // Replace with the correct XML file name if different

        // Initialize UI components
        homeIcon = findViewById(R.id.homeIcon)
        textView = findViewById(R.id.textView)
        createNewButton = findViewById(R.id.createNewButton)
        productRecyclerView = findViewById(R.id.productRecyclerView)
        scannerIcon = findViewById(R.id.scannerIcon) // Initialize scannerIcon here

        // Set up RecyclerView with an empty list
        productRecyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList, this) // Pass context here
        productRecyclerView.adapter = productAdapter

        // Set up click listeners
        homeIcon.setOnClickListener {
            // Handle home icon click if necessary
        }

        createNewButton.setOnClickListener {
            // Navigate to CreateTableActivity
            val intent = Intent(this, CreateTableActivity::class.java)
            startActivity(intent)
        }

        // Initialize the ActivityResultLauncher
        barcodeResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val barcodeData = result.data?.getStringExtra("intentData")
                // Handle the barcode data (e.g., display it or use it)
                val scannedBarcodeTextView: TextView = findViewById(R.id.scannedBarcodeTextView)
                scannedBarcodeTextView.text = "Scanned Barcode: $barcodeData"
            }
        }

        // Set scanner icon click listener
        scannerIcon.setOnClickListener {
            val intent = Intent(this, ScannedBarcodeActivity::class.java)
            barcodeResultLauncher.launch(intent) // Use the launcher to start the activity
        }
    }

    // Function to add a new product and update the RecyclerView
    private fun addNewProduct(product: Product) {
        productList.add(product)
        productAdapter.notifyItemInserted(productList.size - 1) // Notify adapter of new item
    }
}

// Data class for Product
data class Product(val id: Int, val name: String, val quantity: Int)

// Adapter for RecyclerView
class ProductAdapter(private val productList: List<Product>, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productId: TextView = view.findViewById(R.id.productIdTextView)
        val productName: TextView = view.findViewById(R.id.productNameTextView)
        val productQuantity: TextView = view.findViewById(R.id.productQuantityTextView)
        val editIcon: ImageView = view.findViewById(R.id.editIcon) // Edit icon reference
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = android.view.LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productId.text = product.id.toString()
        holder.productName.text = product.name
        holder.productQuantity.text = product.quantity.toString()

        // Set the click listener for the edit icon
        holder.editIcon.setOnClickListener {
            val intent = Intent(context, EditProductActivity::class.java)
            intent.putExtra("productId", product.id)
            intent.putExtra("productName", product.name)
            intent.putExtra("productQuantity", product.quantity)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
