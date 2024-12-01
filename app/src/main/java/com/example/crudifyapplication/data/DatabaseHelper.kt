package com.example.crudifyapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create transaction_details table
        val createTransactionTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_SELL_BY_DATE + " TEXT)"
        db.execSQL(createTransactionTable)

        // Create products table
        val createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_QUANTITY + " INTEGER)"
        db.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS)
        onCreate(db)
    }

    // Insert a new transaction into transaction_details table
    fun insertTransaction(productName: String, productQuantity: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, productName)
            put(COLUMN_PRODUCT_QUANTITY, productQuantity)
        }

        val result = db.insert(TABLE_PRODUCTS, null, contentValues)
        return result != -1L
    }


    // Insert a new product into products table
    fun insertProduct(productName: String?, productQuantity: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_PRODUCT_NAME, productName)
        contentValues.put(COLUMN_PRODUCT_QUANTITY, productQuantity)

        val result = db.insert(TABLE_PRODUCTS, null, contentValues)
        return result != -1L
    }

    // Get a product by its ID
    fun getProductById(productId: Int): Cursor? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCT_ID = ?"
        return db.rawQuery(query, arrayOf(productId.toString()))
    }

    // Check if a transaction item exists
    fun itemExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_NAME + " = ?",
            arrayOf(name)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Get quantity of a specific item in transaction_details
    fun getItemQuantity(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_NAME + " = ?",
            arrayOf(name)
        )
        var quantity = 0
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0)
        }
        cursor.close()
        return quantity
    }

    // Update quantity of an item in transaction_details
    fun updateQuantity(productId: Int, productName: String, productQuantity: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, productName)
            put(COLUMN_PRODUCT_QUANTITY, productQuantity)
        }

        val rowsUpdated = db.update(
            TABLE_PRODUCTS,
            contentValues,
            "$COLUMN_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )
        return rowsUpdated > 0
    }

    // Get all transactions
    val allTransactions: Cursor
        get() {
            val db = this.readableDatabase
            return db.query(
                TABLE_TRANSACTIONS,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }

    // Delete a transaction
    fun deleteTransaction(idToDelete: Int): Boolean {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_TRANSACTIONS, "$COLUMN_ID = ?", arrayOf(idToDelete.toString()))
        return rowsDeleted > 0
    }

    // Get all products
    val allProducts: Cursor
        get() {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)
        }

    // Insert or update a product
    fun insertOrUpdateProduct(productName: String, productQuantity: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCT_NAME = ?", arrayOf(productName))

        if (cursor.moveToFirst()) {
            val currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY))
            val contentValues = ContentValues().apply {
                put(COLUMN_PRODUCT_QUANTITY, currentQuantity + productQuantity)
            }
            val rowsUpdated = db.update(TABLE_PRODUCTS, contentValues, "$COLUMN_PRODUCT_NAME = ?", arrayOf(productName))
            cursor.close()
            return rowsUpdated > 0
        } else {
            val contentValues = ContentValues().apply {
                put(COLUMN_PRODUCT_NAME, productName)
                put(COLUMN_PRODUCT_QUANTITY, productQuantity)
            }
            val result = db.insert(TABLE_PRODUCTS, null, contentValues)
            cursor.close()
            return result != -1L
        }
    }



    // Update a product
    fun updateProduct(productId: Int, productName: String, productQuantity: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PRODUCT_NAME, productName)
            put(COLUMN_PRODUCT_QUANTITY, productQuantity)
        }

        val rowsUpdated = db.update(
            TABLE_PRODUCTS,
            contentValues,
            "$COLUMN_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )
        return rowsUpdated > 0
    }

    // Delete a product by its ID
    fun deleteProduct(productId: Int): Boolean {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_PRODUCTS, "$COLUMN_PRODUCT_ID = ?", arrayOf(productId.toString()))
        return rowsDeleted > 0 // Return true if at least one row was deleted
    }

    // Close the database
    fun closeDatabase() {
        this.writableDatabase.close()
    }

    companion object {
        private const val DATABASE_NAME = "scanner.db"
        private const val DATABASE_VERSION = 1

        // Products table
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_PRODUCT_NAME = "product_name"
        private const val COLUMN_PRODUCT_QUANTITY = "product_quantity"

        // Transaction details table
        private const val TABLE_TRANSACTIONS = "transaction_details"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_SELL_BY_DATE = "sellByDate"
    }
}
