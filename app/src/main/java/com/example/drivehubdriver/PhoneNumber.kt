package com.example.drivehubdriver

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.drivehubdriver.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class PhoneNumber : AppCompatActivity() {

    private lateinit var sendOTPBtn : Button
    private lateinit var phoneNumberET : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private lateinit var mProgressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        init()
        sendOTPBtn.setOnClickListener {
            number = phoneNumberET.text.trim().toString()
            if (number.isNotEmpty()){
                if (number.length == 10){
                    if (isNetworkAvailable()) {
                        number = "+91$number"
                        mProgressBar.visibility = View.VISIBLE
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(number)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                            .build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                    else
                    {
                        Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show()

                    }
                }else{
                    Toast.makeText(this , "Please Enter correct Number" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this , "Please Enter Number" , Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun init(){
        mProgressBar = findViewById(R.id.Phoneprogressbar)
        mProgressBar.visibility = View.INVISIBLE
        sendOTPBtn = findViewById(R.id.sendOTPBtn)
        phoneNumberET = findViewById(R.id.phonenumberET)
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate Successfully", Toast.LENGTH_SHORT).show()
                    sendToMain()
                    val currentUser = auth.currentUser
                    val isNewUser = currentUser?.metadata?.creationTimestamp == currentUser?.metadata?.lastSignInTimestamp

                    // Pass the phone number to RegisterActivity
                    val intent = Intent(this, Register::class.java)
                    intent.putExtra("phoneNumber", number)
                    startActivity(intent)
                    finish()

                    // Inside EnterNum activity, after the phone number is obtained
//                    val phoneNumber = number // Replace with the actual phone number
//                    val profileFragment = Fragment_Profile()
//                    val bundle = Bundle()
//                    bundle.putString("phoneNumber", phoneNumber)
//                    profileFragment.arguments = bundle
//
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, profileFragment)
//                        .commit()


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                mProgressBar.visibility = View.INVISIBLE
            }
    }

    private fun sendToMain() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already authenticated, check if new or returning
            val isNewUser = currentUser.metadata?.creationTimestamp == currentUser.metadata?.lastSignInTimestamp
            if (isNewUser) {
                // New user, redirect to RegisterActivity
                startActivity(Intent(this, Register::class.java))
            } else {
                // Returning user, redirect to HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        } else {
            // Handle the case when the user is not authenticated
        }
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
            mProgressBar.visibility = View.VISIBLE
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
            val intent = Intent(this@PhoneNumber,VerifyOtp::class.java)
            intent.putExtra("OTP" , verificationId)
            intent.putExtra("resendToken" , token)
            intent.putExtra("phoneNumber" , number)
            startActivity(intent)
            mProgressBar.visibility = View.INVISIBLE
        }
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this , HomeActivity::class.java))
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnectedOrConnecting == true
    }
}