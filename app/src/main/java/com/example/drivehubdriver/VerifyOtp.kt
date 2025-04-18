package com.example.drivehubdriver

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.drivehubdriver.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


import android.widget.Button
import com.google.firebase.database.*


class VerifyOtp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verifyBtn: Button //2
    private lateinit var resendOTPBtn: Button
    private lateinit var inputOTP1: EditText
    private lateinit var inputOTP2: EditText
    private lateinit var inputOTP3: EditText
    private lateinit var inputOTP4: EditText
    private lateinit var inputOTP5: EditText
    private lateinit var inputOTP6: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var phoneNumber: String //2

    private val PERMISSION_REQUEST_CODE = 123 // Replace with any unique code
    private val REQUIRED_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION

    private lateinit var database: DatabaseReference //2

//    private lateinit var database: DatabaseReference
//    private lateinit var verifyBtn: Button
//    private lateinit var phoneNumber: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        database = FirebaseDatabase.getInstance().reference.child("phoneNumbers")

        //verifyBtn = findViewById(R.id.verifyBtn)



        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phoneNumber")!!

        init()
        progressBar.visibility = View.INVISIBLE
        addTextChangeListener()
        resendOTPTvVisibility()

        resendOTPBtn.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }

        verifyBtn.setOnClickListener {
            //collect otp from all the edit texts
            val typedOTP =
                (inputOTP1.text.toString() + inputOTP2.text.toString() + inputOTP3.text.toString()
                        + inputOTP4.text.toString() + inputOTP5.text.toString() + inputOTP6.text.toString())

            if (typedOTP.isNotEmpty()) {
                if (typedOTP.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    progressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show()
            }


        }

        //phonenumber retrieve firebase to app
        verifyBtn.setOnClickListener { retrievePhoneNumbers() }
    }

    private fun retrievePhoneNumbers() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    phoneNumber = snapshot.value as String
                    Log.d("PhoneNumber", phoneNumber)
                    passPhoneNumberToRegisterActivity(phoneNumber)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FirebaseError", "Failed to read value.", databaseError.toException())
            }
        })
    }

    private fun passPhoneNumberToRegisterActivity(phoneNumber: String) {
        val intent = Intent(this, Register::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
    }

    private fun resendOTPTvVisibility() {
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")
        resendOTPBtn.visibility = View.INVISIBLE
        resendOTPBtn.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendOTPBtn.visibility = View.VISIBLE
            resendOTPBtn.isEnabled = true
        }, 60000)
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
            progressBar.visibility = View.VISIBLE
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token
        }
    }

//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//
//                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
//                    sendToMain()
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                    // Update UI
//                }
//                progressBar.visibility = View.VISIBLE
//            }
//    }


//======================================================================================================//

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    checkUserAndNavigate()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                progressBar.visibility = View.VISIBLE
            }
    }



    private fun checkUserAndNavigate() {
        // Check if the user is already registered or a new user
        // Replace this with your actual logic to determine if the user is new or not
        val isNewUser = true // Replace with your actual logic

        if (isNewUser) {
            // If the user is new, navigate to registration screen
            sendToRegistration()
        } else {
            // If the user is already registered, check and request location permission
            checkAndRequestPermission()
        }
    }

    private fun sendToRegistration() {
        startActivity(Intent(this, Register::class.java))
        finish()
    }
    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed to HomeActivity or any other action
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                // Permission not granted, request it
                requestPermissions(arrayOf(REQUIRED_PERMISSION), PERMISSION_REQUEST_CODE)
            }
        } else {
            // For devices running below Marshmallow, no need to request runtime permissions
            startActivity(Intent(this, HomeActivity::class.java))
            showMessageAndFinish("Location permission is required for the app to function.")
            finish()
        }
    }

    // Existing code...

    private fun showMessageAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        // Finish the activity
        finishAffinity()
    }


    //=========================================================================================================//
    private fun sendToMain() {
        startActivity(Intent(this, Register::class.java))
    }

    private fun addTextChangeListener() {
        inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
        inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
        inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
        inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
        inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
        inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP6))
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.OTPprogressbar)
        verifyBtn = findViewById(R.id.verifyOTPBtn)
        resendOTPBtn = findViewById(R.id.resendOTPBtn)
        inputOTP1 = findViewById(R.id.otpEditText1)
        inputOTP2 = findViewById(R.id.otpEditText2)
        inputOTP3 = findViewById(R.id.otpEditText3)
        inputOTP4 = findViewById(R.id.otpEditText4)
        inputOTP5 = findViewById(R.id.otpEditText5)
        inputOTP6 = findViewById(R.id.otpEditText6)
    }


    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            val text = p0.toString()
            when (view.id) {
                R.id.otpEditText1 -> if (text.length == 1) inputOTP2.requestFocus()
                R.id.otpEditText2 -> if (text.length == 1) inputOTP3.requestFocus() else if (text.isEmpty()) inputOTP1.requestFocus()
                R.id.otpEditText3 -> if (text.length == 1) inputOTP4.requestFocus() else if (text.isEmpty()) inputOTP2.requestFocus()
                R.id.otpEditText4 -> if (text.length == 1) inputOTP5.requestFocus() else if (text.isEmpty()) inputOTP3.requestFocus()
                R.id.otpEditText5 -> if (text.length == 1) inputOTP6.requestFocus() else if (text.isEmpty()) inputOTP4.requestFocus()
                R.id.otpEditText6 -> if (text.isEmpty()) inputOTP5.requestFocus()

            }
        }
    }
}