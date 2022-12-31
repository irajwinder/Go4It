package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Address : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var meal:String
    lateinit var preferences:String
    lateinit var total:String
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var addressInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

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

        val bundle = intent.extras
        if (bundle != null){
            meal  = bundle.getString("meals").toString()
            preferences  = bundle.getString("preference").toString()
            total  = bundle.getString("amount").toString()
        }

        val addressline1:EditText=findViewById(R.id.addrline1txt)
        val addressline2:EditText=findViewById(R.id.addrline2txt)
        val city:EditText=findViewById(R.id.citytxt)
        val state:EditText=findViewById(R.id.statetxt)
        val zipcode:EditText=findViewById(R.id.zipcodetxt)

        //Array of Edit Fields
        addressInputsArray = arrayOf(addressline1, city, state, zipcode)

        //Checks if the editTextBoxes are empty or not
        fun notEmpty(): Boolean = addressline1.text.toString().trim().isNotEmpty() &&
                city.text.toString().trim().isNotEmpty() &&
                state.text.toString().trim().isNotEmpty() &&
                zipcode.text.toString().trim().isNotEmpty()

        val deliveryIns = findViewById<Button>(R.id.nextbtn)
        deliveryIns.setOnClickListener{
            if (notEmpty()) {
                val intent = Intent(this, DeliveryInstructions::class.java)
                intent.putExtra("address1", addressline1.text.toString())
                intent.putExtra("address2", addressline2.text.toString())
                intent.putExtra("city", city.text.toString())
                intent.putExtra("state", state.text.toString())
                intent.putExtra("zipcode", zipcode.text.toString())
                intent.putExtra("preference", preferences)
                intent.putExtra("meals", meal)
                intent.putExtra("amount", total)
                startActivity(intent)
            }else {
                addressInputsArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"
                        Toast.makeText(this,"Enter full delivery Address", Toast.LENGTH_LONG).show()
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