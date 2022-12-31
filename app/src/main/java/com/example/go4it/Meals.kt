package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_meals.*


class Meals : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var preferences:String
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals)

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
           preferences  = bundle.getString("preference").toString()

        }

        val addRes = findViewById<Button>(R.id.nextbtn)
        // Get radio group selected status and text using button click event
        addRes.setOnClickListener{
            // Get the checked radio button id from radio group
            val id: Int = radio_group_meal.checkedRadioButtonId
            if (id!=-1){
                val intent = Intent(this,Address::class.java)
                startActivity(intent)
            }else{
                // If no radio button checked in this radio group
                Toast.makeText(applicationContext,
                    " Please select meals per week",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        var totalBill = 15
        price.text = "$" + totalBill
        if (view is RadioButton) {
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.btn4 ->
                    if (checked) {
                        totalBill*=4
                    }
                R.id.btn6 ->
                    if (checked) {
                        totalBill*=6
                    }
                R.id.btn8 ->
                    if (checked) {
                        totalBill*=8
                    }
                R.id.btn10 ->
                    if (checked) {
                        totalBill*=10
                    }
                R.id.btn12 ->
                    if (checked) {
                        totalBill*=12
                    }
                R.id.btn14 ->
                    if (checked) {
                        totalBill*=14
                    }
            }
            total.text = "$" + totalBill

            nextbtn.setOnClickListener{
                val id: Int = radio_group_meal.checkedRadioButtonId
                    val radio:RadioButton = findViewById(id)
                    val intent = Intent(this,Address::class.java)
                    intent.putExtra("meals",radio.text.toString())
                    intent.putExtra("preference",preferences)
                    intent.putExtra("amount", totalBill.toString())
                    startActivity(intent)
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