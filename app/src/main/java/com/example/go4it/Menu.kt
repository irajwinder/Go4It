package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class Menu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    private val db = FirebaseFirestore.getInstance()
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

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