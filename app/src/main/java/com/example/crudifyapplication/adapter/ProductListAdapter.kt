package com.example.crudifyapplication.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crudifyapplication.R

class ProductListAdapter(
    private val context: Context,
    private var cursor: Cursor,
    private val listener: OnProductActionListener
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    interface OnProductActionListener {
        fun onEditClick(position: Int, productId: Int)
        fun onDeleteClick(position: Int, productId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // Move the cursor to the correct position
        if (cursor.moveToPosition(position)) {
            // Retrieve the product details from the cursor
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))

            // Set the UI elements in the ViewHolder
            holder.productIdTextView.text = id.toString()
            holder.productNameTextView.text = name
            holder.productQuantityTextView.text = quantity.toString()

            // Set up the edit and delete click listeners
            holder.editIcon.setOnClickListener {
                listener.onEditClick(position, id)
            }

            holder.deleteIcon.setOnClickListener {
                listener.onDeleteClick(position, id)
            }
        } else {
            // Handle case where cursor can't move to position
            holder.productIdTextView.text = "N/A"
            holder.productNameTextView.text = "Unknown"
            holder.productQuantityTextView.text = "0"
        }
    }

    override fun getItemCount(): Int = cursor.count

    fun swapCursor(newCursor: Cursor): Cursor {
        if (cursor != newCursor) {
            val oldCursor = cursor
            cursor = newCursor
            notifyDataSetChanged()
            return oldCursor
        }
        return cursor
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productIdTextView: TextView = itemView.findViewById(R.id.productIdTextView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.productQuantityTextView)
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)
    }
}