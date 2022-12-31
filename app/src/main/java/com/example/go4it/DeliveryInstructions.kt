package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_deliveryinstructions.*

class DeliveryInstructions : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var meal:String
    lateinit var preferences:String
    lateinit var total:String
    lateinit var address1:String
    lateinit var address2:String
    lateinit var city:String
    lateinit var state:String
    lateinit var zipcode:String
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliveryinstructions)

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
            address1  = bundle.getString("address1").toString()
            address2  = bundle.getString("address2").toString()
            city  = bundle.getString("city").toString()
            state  = bundle.getString("state").toString()
            zipcode  = bundle.getString("zipcode").toString()
            preferences  = bundle.getString("preference").toString()
            meal  = bundle.getString("meals").toString()
            total  = bundle.getString("amount").toString()
        }

        val otherinstructions = findViewById<EditText>(R.id.othertxt)
        val payment = findViewById<Button>(R.id.nextbtn)
        // Get radio group selected status and text using button click event
        payment.setOnClickListener {
            // Get the checked radio button id from radio group
            var id: Int = radio_group_instructions.checkedRadioButtonId
            if (id != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = findViewById(id)

                val intent = Intent(this, OrderSummary::class.java)
                intent.putExtra("instructions", radio.text.toString())
                intent.putExtra("address1", address1)
                intent.putExtra("address2", address2)
                intent.putExtra("city", city)
                intent.putExtra("state", state)
                intent.putExtra("zipcode", zipcode)
                intent.putExtra("preference", preferences)
                intent.putExtra("meals", meal)
                intent.putExtra("amount", total)
                intent.putExtra("otherinstructions", otherinstructions.text.toString())
                startActivity(intent)
            }else{
                // If no radio button checked in this radio group
                Toast.makeText(applicationContext,
                    " Please select Delivery Instructions",
                    Toast.LENGTH_SHORT).show()
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