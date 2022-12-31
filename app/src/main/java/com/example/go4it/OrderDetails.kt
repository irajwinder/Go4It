package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class OrderDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var db = FirebaseFirestore.getInstance()
    private lateinit var tvUserPreferences: TextView
    private lateinit var tvMeals: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdetails)

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

        tvUserPreferences = findViewById(R.id.userPreferences)
        tvMeals = findViewById(R.id.meals)
        btnEdit = findViewById(R.id.editbtn)
        btnCancel = findViewById(R.id.cancelBtn)
    }

    override fun onStart() {
        super.onStart()

        //Fetch Data
        val bundle: Bundle? = intent.extras
        var address1  = bundle?.getString("address1").toString()
        var address2  = bundle?.getString("address2").toString()
        var city  = bundle?.getString("city").toString()
        var state  = bundle?.getString("state").toString()
        var zipcode  = bundle?.getString("zipcode").toString()
        var preferences  = bundle?.getString("preference").toString()
        var  meal  = bundle?.getString("meals").toString()
        var amount  = bundle?.getString("amount").toString()
        var deliveryinstruction  = bundle?.getString("deliveryinstruction").toString()
        var otherinstructions  = bundle?.getString("otherinstructions").toString()
        var userid  = bundle?.getString("userid").toString()
        var orderid  = bundle?.getString("orderid").toString()


        val userId = FirebaseAuth.getInstance().currentUser!!.email

        val ref = db.collection("order")
        ref.get().addOnSuccessListener {
            if(it!=null){
                for (data in it.documents) {
                    val order: Order? = data.toObject(Order::class.java)
                    if (order != null && order.userid!!.equals(userId)) {

                        tvUserPreferences.text = preferences
                        tvMeals.text = meal

                        //Pass values and navigate
                        btnEdit.setOnClickListener{
                            val bundle2 = Bundle()
                            bundle2.putString("address1", address1)
                            bundle2.putString("address2", address2)
                            bundle2.putString("city", city)
                            bundle2.putString("state", state)
                            bundle2.putString("zipcode", zipcode)
                            bundle2.putString("preference", preferences)
                            bundle2.putString("meals", meal)
                            bundle2.putString("amount", amount)
                            bundle2.putString("deliveryinstruction", deliveryinstruction)
                            bundle2.putString("otherinstructions", otherinstructions)
                            bundle2.putString("userid", userid)
                            bundle2.putString("orderid", orderid)

                            val intent = Intent(this, EditOrderDetails::class.java)
                            intent.putExtras(bundle2)
                            startActivity(intent)
                        }

                        btnCancel.setOnClickListener{
                            val orderhistory= hashMapOf(
                                "address1" to address1,
                                "address2" to address2,
                                "city" to city,
                                "state" to state,
                                "zipcode" to zipcode,
                                "userpreference" to preferences,
                                "mealsperweek" to meal,
                                "amount" to amount,
                                "deliveryinstruction" to deliveryinstruction,
                                "otherinstructions" to otherinstructions,
                                "userid" to userid,
                                "orderid" to orderid
                            )

                            //Delete from orders
                            db.collection("order").document(orderid)
                                .delete()
                                .addOnSuccessListener{
                                    Toast.makeText(this, "Your order has been cancelled", Toast.LENGTH_SHORT).show()
                                }

                            //Add into order history
                            db.collection("orderhistory")
                                .add(orderhistory)
                                .addOnSuccessListener { documentReference ->
                                    val intent = Intent(this,Preferences::class.java)
                                    startActivity(intent)
                                    finishAffinity()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show()
                                }
                        }

                    }
                }
            }
        } .addOnFailureListener {
            Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,Preferences::class.java)
        startActivity(intent)
        finishAffinity()
    }
}