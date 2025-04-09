package com.example.drivehubdriver


//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class CallPermission : AppCompatActivity() {

//    private lateinit var btnAllow: Button
//    private lateinit var btnDeny: Button
//
//    companion object {
//        const val CONTACT_PERMISSION_REQUEST_CODE = 100
//    }

    companion object {
        const val CONTACT_PERMISSION_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_permission)

        supportActionBar?.hide() //hide app bar

        requestContactPermission()

//        btnAllow = findViewById(R.id.btnAllow)
//        btnDeny = findViewById(R.id.btnDeny)
//
//        btnAllow.setOnClickListener {
//            requestContactPermission()
//        }
//
//        btnDeny.setOnClickListener {
//            // Handle deny action
//            Toast.makeText(this, "Call Permission is Compulsory", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun requestContactPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_CONTACTS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.READ_CONTACTS),
//                CONTACT_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            // Permission already granted
//            navigateToNextActivity()
//        }
//    }
//
//    private fun navigateToNextActivity() {
//        // Start the next activity here
//        Toast.makeText(this, "Permission Granted. Navigating to Next Activity...", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this, LocationPermission::class.java)
//        startActivity(intent)
//        finish() // Optional: finish the current activity if needed
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == CONTACT_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                navigateToNextActivity()
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Call Permission is Compulsory", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    private fun requestContactPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACT_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted
            navigateToNextActivity()
        }
    }

    private fun navigateToNextActivity() {
        // Start the next activity here, for example, your main app activity
//        Toast.makeText(this, "Permission Granted. Navigating to Next Activity...", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LocationPermission::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity if needed
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACT_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                navigateToNextActivity()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}