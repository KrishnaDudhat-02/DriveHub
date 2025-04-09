package com.example.drivehubdriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Register : AppCompatActivity() {

    private lateinit var phonenumberET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        phonenumberET = findViewById(R.id.phonenumberET)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {
            val intent =Intent(this,CitySelection::class.java)
        }

        val phoneNumber = intent.getStringExtra("phoneNumber")
        phonenumberET.setText(phoneNumber)


    }
}