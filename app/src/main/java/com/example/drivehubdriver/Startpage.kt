package com.example.drivehubdriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button


class Startpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startpage)

        supportActionBar?.hide() // hide app bar

        val btnStart = findViewById<Button>(R.id.btnstart)

        // Set OnClickListener to btnStart
        btnStart.setOnClickListener {
            // Create an Intent to start ActivityB
            val intent = Intent(this, PhoneNumber::class.java)

            // You can also pass data to the new activity using Intent extras
            //intent.putExtra("key", "value")

            // Start ActivityB
            startActivity(intent)
        }
    }
}