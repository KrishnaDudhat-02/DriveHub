package com.example.drivehubdriver



//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermission : AppCompatActivity() {

//    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        supportActionBar?.hide() //hide app bar

        // Check if location permission is granted
        if (isLocationPermissionGranted()) {
            // If permission is granted, start the next activity
            startNextActivity()
        } else {
            // If permission is not granted, request it
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, start the next activity
                    startNextActivity()
                } else {
                    // Permission denied, show toast message
                    Toast.makeText(this, "Location permission is compulsory.", Toast.LENGTH_SHORT).show()
                    // Optionally, you may finish the activity or handle the denial case differently
                    // finish()
                }
                return
            }
        }
    }

    private fun startNextActivity() {
        // Start the next activity here using Intent
        val intent = Intent(this, Startpage::class.java)
        startActivity(intent)
        finish() // Finish the current activity if you don't want to return to it


//        val btnAllow = findViewById<Button>(R.id.btnAllow)
//        val btnDeny = findViewById<Button>(R.id.btnDeny)
//
//        btnAllow.setOnClickListener {
//            requestLocationPermission()
//        }
//
//        btnDeny.setOnClickListener {
//            // Show toast message
//            Toast.makeText(this, "Location permission is compulsory.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission already granted, proceed to next activity
//            startNextActivity()
//        } else {
//            // Permission not granted, request it
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // Permission granted, proceed to next activity
//                    startNextActivity()
//                } else {
//                    // Permission denied, show toast message
//                    Toast.makeText(this, "Location permission is compulsory.", Toast.LENGTH_SHORT).show()
//
//                }
//                return
//            }
//        }
//    }
//
//    private fun startNextActivity() {
//        // Start the next activity here using Intent
//        val intent = Intent(this, Startpage::class.java)
//        startActivity(intent)
//        finish() // Finish the current activity if you don't want to return to it

    }
}