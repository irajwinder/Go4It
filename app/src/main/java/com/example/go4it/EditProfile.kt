package com.example.go4it

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.lang.Exception


class EditProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //Drawer
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var firstName: TextView
    private lateinit var lastName:TextView
    private lateinit var email: TextView
    private lateinit var phoneNumber:TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val navView: NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // drawer layout instance to toggle
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewInitializations()

        updateProfile.setOnClickListener {
            updateUserProfile()
        }
    }


    private fun viewInitializations() {
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        email  = findViewById(R.id.email)
        phoneNumber = findViewById(R.id.pNumber)

        //Fetch user values
        val bundle = intent.extras
        if (bundle != null){
            firstName.text = "${bundle.getString("fName")}"
            lastName.text = "${bundle.getString("lName")}"
            email.text = "${bundle.getString("email")}"
            phoneNumber.text = "${bundle.getString("phone")}"
        }
    }

    // Checking if the input in form is valid
    private fun validateInput(): Boolean {
        if (firstName.text.toString().equals("")) {
            firstName.error = "Please Enter First Name"
            return false
        }
        if (lastName.text.toString().equals("")) {
            lastName.error = "Please Enter Last Name"
            return false
        }
        if (email.text.toString().equals("")) {
            email.error = "Please Enter Email"
            return false
        }

        if (phoneNumber.text.toString().equals("")) {
            phoneNumber.error = "Please Enter Contact No"
            return false
        }

        // checking the proper email format
        if (!isEmailValid(email.text.toString())) {
            email.setError("Please Enter Valid Email")
            return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



    private fun updateUserProfile() {
        if (validateInput()) {
            // Input is valid, here send data to your server
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val email = email.text.toString()
            val phoneNo = phoneNumber.text.toString()

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val user = Firebase.auth.currentUser

            //Update details
            user!!.updateEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // API Call
                    db.collection("users").document(userId)
                        .update(
                            mapOf(
                                "firstname" to fName,
                                "lastname" to lName,
                                "email" to email,
                                "phone" to phoneNo,
                            )
                        )
                    //Navigate to the Profile
                    val intent = Intent(this, Profile::class.java)
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    val e: Exception? = task.exception
                    if (e != null) {
                        Toast.makeText(this@EditProfile, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_account -> {
                startActivity(Intent(this, Profile::class.java))
            }

            R.id.nav_order -> {
                startActivity(Intent(this, OrderList::class.java))
            }

            R.id.nav_logout -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, SignIn::class.java))
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                finish()
            }

            R.id.nav_order_history -> {
                startActivity(Intent(this, OrderHistory::class.java))
                drawerLayout.close()
            }

            R.id.nav_menu -> {
                startActivity(Intent(this, Menu::class.java))
                drawerLayout.close()
            }

        }
        return false
    }

    // function to implement the item click listener callback
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}