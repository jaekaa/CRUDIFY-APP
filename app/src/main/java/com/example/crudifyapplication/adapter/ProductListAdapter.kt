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
        if (cursor.moveToPosition(position)) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"))
            val productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"))
            val productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("product_quantity"))

            holder.productIdTextView.text = productId.toString()
            holder.productNameTextView.text = productName
            holder.productQuantityTextView.text = productQuantity.toString()

            holder.editIcon.setOnClickListener {
                listener.onEditClick(position, productId)
            }

            holder.deleteIcon.setOnClickListener {
                listener.onDeleteClick(position, productId)
            }
        }
    }

    override fun getItemCount(): Int = cursor.count

    fun swapCursor(newCursor: Cursor?) {
        if (cursor != newCursor) {
            val oldCursor = cursor
            cursor = newCursor ?: cursor
            notifyDataSetChanged()
            oldCursor?.close()
        }
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productIdTextView: TextView = itemView.findViewById(R.id.productIdTextView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.productQuantityTextView)
        val editIcon: ImageView = itemView.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)
    }
}
