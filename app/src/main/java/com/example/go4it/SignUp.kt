package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity() {
    private lateinit var userFName: String
    private lateinit var userLName: String
    private lateinit var userPhone: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var signUpArray: Array<EditText>
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Hides the app name from the top
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        signUpArray = arrayOf(fName, lName, pNumber,Email, Password, ConfirmPassword)


        btnCreateAccount.setOnClickListener {
            signUp()
        }

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
    }

    //Checks if the textboxes are empty or not
    private fun notEmpty(): Boolean = fName.text.toString().trim().isNotEmpty() &&
            lName.text.toString().trim().isNotEmpty() &&
            pNumber.text.toString().trim().isNotEmpty() &&
            Email.text.toString().trim().isNotEmpty() &&
            Password.text.toString().trim().isNotEmpty() &&
            ConfirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            Password.text.toString().trim() == ConfirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            signUpArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            Toast.makeText(this, "passwords are not matching !", Toast.LENGTH_SHORT).show()
        }
        return identical
    }

    //Stores user details in the database
    private fun signUp() {
        if (identicalPassword()) {
            // identicalPassword() returns true only when inputs are not empty and passwords are identical
            userFName = fName.text.toString().trim()
            userLName = lName.text.toString().trim()
            userPhone = pNumber.text.toString().trim()
            userEmail = Email.text.toString().trim()
            userPassword = Password.text.toString().trim()
            val user= hashMapOf(
                "firstname" to userFName,
                "lastname" to userLName,
                "phone" to userPhone,
                "email" to userEmail
            )
            /*create a user*/
            val userDetails=db.collection("users")
            userDetails.whereEqualTo("email",userEmail).get()
                .addOnSuccessListener { tasks->
                    if(tasks.isEmpty) {
                        progressBar.visibility = View.VISIBLE
                        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                            .addOnCompleteListener(this){ task->
                                progressBar.visibility = View.GONE
                                if(task.isSuccessful) {
                                    val userId = auth.currentUser!!.uid
                                    userDetails.document(userId).set(user)
                                    sendEmailVerification()
                                    Toast.makeText(this, "Account created successfully !", Toast.LENGTH_SHORT).show()
                                    val intent=Intent(this,SignIn::class.java)
                                    intent.putExtra("email",userEmail)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(this,"User Already Registered, Please log in", Toast.LENGTH_LONG).show()
                        val intent= Intent(this,SignIn::class.java)
                        startActivity(intent)
                    }
                }
        } else {
            Toast.makeText(this,"Enter the Details", Toast.LENGTH_LONG).show()
        }
    }

    /* send verification email to the new user */
    private fun sendEmailVerification() {
        //get current user
        val firebaseUser = auth.currentUser
        firebaseUser!!.sendEmailVerification()
            .addOnSuccessListener {
                Toast.makeText(this, "email sent to $userEmail", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to send due to " + e.message, Toast.LENGTH_SHORT).show()
            }
    }
}