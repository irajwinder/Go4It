package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signin.*

class SignIn : AppCompatActivity() {
    private lateinit var signInEmail: String
    private lateinit var signInPassword: String
    private lateinit var signInArray: Array<EditText>
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        //Hides the app name from the top
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        //Navigate to different Screen on button press
        signInArray = arrayOf(SignInEmail, SignInPassword)
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    /* check if there's a signed-in user*/
    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, Preferences::class.java))
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, Preferences::class.java))
            Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    //Authenticate user
    private fun signInUser() {
        signInEmail = SignInEmail.text.toString().trim()
        signInPassword = SignInPassword.text.toString().trim()

        if (notEmpty()) {
            progressBar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    progressBar.visibility = View.GONE
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, Preferences::class.java))
                        Toast.makeText(this, "Welcome to Go4It", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Wrong email or Password!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            signInArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                    Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}