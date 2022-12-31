//Reference Link: https://www.youtube.com/watch?v=cBwaJYocb9I

package com.example.go4it

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderHistory : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener, Adapter.OnItemClickListener  {
    private lateinit var actionBar: ActionBar
    lateinit var drawerLayout: DrawerLayout
    private var db = Firebase.firestore

    private var orderList=ArrayList<Order>()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val navView: NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // drawer layout instance to toggle
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //Configure Action Bar
        actionBar = supportActionBar!!
        actionBar.title = "Order History"

        //Reference Link : https://www.youtube.com/watch?v=cBwaJYocb9I

        var tvnoorderhistory:TextView=findViewById(R.id.textView_no_order_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.rv_order_history)
        recyclerView.layoutManager=LinearLayoutManager(this)

        var useremail=firebaseAuth.currentUser!!.email

        orderList = arrayListOf<Order>()
        db=FirebaseFirestore.getInstance()

        db.collection("orderhistory").get()
            .addOnSuccessListener {
                if (it!=null) {
                    for (data in it.documents) {
                        val order: Order? = data.toObject(Order::class.java)
                        if (order != null && order.userid!!.equals(useremail)) {
                            orderList.add(order)
                        }
                        if(orderList.size>0){
                            tvnoorderhistory.visibility=View.GONE
                        }else{
                            tvnoorderhistory.visibility=View.VISIBLE
                        }
                    }
                    recyclerView.adapter = Adapter(orderList, this)
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onItemClick(position: Int) {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_account -> {
                startActivity(Intent(this, Profile::class.java))
            }

            R.id.nav_order -> {
                startActivity(Intent(this, OrderList::class.java))
                drawerLayout.close()
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
