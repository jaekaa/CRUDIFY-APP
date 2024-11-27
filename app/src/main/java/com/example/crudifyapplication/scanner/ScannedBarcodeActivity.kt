package com.example.crudifyapplication.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.crudifyapplication.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class ScannedBarcodeActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var txtBarcodeValue: TextView
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private var intentData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_barcode)

        // Initialize views
        initViews()

        // Initialize the barcode detector and camera source
        initialiseDetectorsAndSources()
    }

    private fun initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue)
        surfaceView = findViewById(R.id.surfaceView)
    }

    private fun initialiseDetectorsAndSources() {
        // Initialize the barcode detector
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        // Initialize the camera source
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        // Set up the SurfaceView callback for handling camera surface
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    // Check camera permissions and start the camera source
                    if (ActivityCompat.checkSelfPermission(
                            this@ScannedBarcodeActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource?.start(surfaceView.holder)
                    } else {
                        // Request camera permission if not granted
                        ActivityCompat.requestPermissions(
                            this@ScannedBarcodeActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error starting camera", Toast.LENGTH_SHORT).show()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Handle surface changes here if needed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop() // Stop the camera when surface is destroyed
            }
        })

        // Set barcode detection processor
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scan stopped", Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    // When barcode is detected, update UI with the barcode value
                    txtBarcodeValue.post {
                        intentData = barcodes.valueAt(0).displayValue
                        txtBarcodeValue.text = intentData

                        // Pass the scanned data back to the calling activity
                        val resultIntent = Intent()
                        resultIntent.putExtra("intentData", intentData)
                        setResult(RESULT_OK, resultIntent)
                        finish() // Close the activity and return result
                    }
                }
            }
        })
    }

    // Handle the camera permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission granted, restart the camera
                try {
                    cameraSource?.start(surfaceView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error starting camera", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show a message if permission is denied
                Toast.makeText(applicationContext, "Camera permission is required to scan barcode", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Release the camera source when the activity is paused
        cameraSource?.release()
    }

    override fun onResume() {
        super.onResume()
        // Reinitialize the detectors and sources when activity resumes
        initialiseDetectorsAndSources()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
    }
}
