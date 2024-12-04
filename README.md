SAMPLE DATA AND INSTRUCTION HOW TO TEST

1. Create a Product (Test Create Table)
Launch the app.

Navigate to the "Create New Product" screen by clicking on the "Create New Product" button.

Enter sample data:

Product Name: "Apple"
Quantity: 10
Save the product by clicking the "Save" button.

You should see a Toast message confirming that the product has been saved successfully.

2. View Products List
Return to the Product List Activity.
You should see the product "Apple" listed in the RecyclerView with its quantity (10) displayed. This confirms that the product was saved to the database correctly.
3. Test Edit and Delete Functionality
Edit Product:

Click the edit icon next to the "Apple" product in the list.
You should be directed to an EditProductActivity (assuming you've set this up in your project).
Modify the product name to "Orange" and quantity to 5, and save it.
Return to the product list and verify that the name is updated to "Orange" and the quantity is 5.
Delete Product:

Click the delete icon next to the "Orange" product.
You should be prompted with a confirmation message.
Confirm the deletion, and the product should be removed from the list.
4. Test Barcode Scanning (Optional)
If you've integrated barcode scanning functionality, you can follow these steps:

Click on the barcode scan icon in the Product List Activity.
Scan a barcode (or manually enter a barcode in your ScannedBarcodeActivity).
You should see a message showing the scanned barcode.
The barcode data should be used to create or update a product in the database with a default quantity (you can customize the logic based on your barcode format).
Sample Data:
You can use the following sample data to test:

Product Name	Quantity
Apple	          10
Orange	         5
Banana	         8
Mango	           3

![Code-128 (2)](https://github.com/user-attachments/assets/1ed2ba1f-64a6-4ffc-aaf2-e986f28f1ea7)
![Code-128](https://github.com/user-attachments/assets/5284751f-b975-41a9-85ad-6561fc2aff58)
![Code-128 (1)](https://github.com/user-attachments/assets/1e7bcc6c-d987-45bb-ad93-2dc07e8d9036)



