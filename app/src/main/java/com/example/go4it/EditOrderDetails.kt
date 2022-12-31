package com.example.go4it

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_meals.*

class EditOrderDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //Drawer
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var spUserPreference: Spinner
    private lateinit var spMeals:Spinner
    private lateinit var btnUpdateOrder: Button
    private val db = FirebaseFirestore.getInstance()

    lateinit var address1:String
    lateinit var address2:String
    lateinit var city:String
    lateinit var state:String
    lateinit var zipcode:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_order_details)

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


        var etaddressline1:EditText=findViewById(R.id.addrline1txt)
        var etaddressline2:EditText=findViewById(R.id.addrline2txt)
        var etcity:EditText=findViewById(R.id.citytxt)
        var etstate:EditText=findViewById(R.id.statetxt)
        var etzipcode:EditText=findViewById(R.id.zipcodetxt)

            etaddressline1.setText(address1)
            etaddressline2.setText(address2)
            etcity.setText(city)
            etstate.setText(state)
            etzipcode.setText(zipcode)

        spUserPreference = findViewById(R.id.editText_user_preferences)
        spMeals = findViewById(R.id.editText_meals)
        btnUpdateOrder=findViewById(R.id.button_update_oder)

            if(preferences.equals("Chef Choice")) {
                spUserPreference.setSelection(0)
            } else if(preferences.equals("Calorie Control")) {
                spUserPreference.setSelection(1)
            }else if(preferences.equals("Vegan")) {
                spUserPreference.setSelection(2)
            }else if(preferences.equals("Protein")) {
                spUserPreference.setSelection(3)
            }else if(preferences.equals("Meat Love")) {
                spUserPreference.setSelection(4)

            }

            if (meal.equals("4")) {
                spMeals.setSelection(0)
            } else if (meal.equals("6")) {
                spMeals.setSelection(1)
            } else if (meal.equals("8")) {
                spMeals.setSelection(2)
            } else if (meal.equals("10")) {
                spMeals.setSelection(3)
            } else if (meal.equals("12")) {
                spMeals.setSelection(4)
            } else if (meal.equals("14")) {
                spMeals.setSelection(5)
            }


        btnUpdateOrder.setOnClickListener {
            // Input is valid, here send data to your server
            val etUserPreference = spUserPreference.selectedItem
            val etMeals = spMeals.selectedItem

            var totalBill = 15
            if (etMeals.equals("4")) {
                totalBill*=4
            } else if (etMeals.equals("6")) {
                totalBill*=6
            } else if (etMeals.equals("8")) {
                totalBill*=8
            } else if (etMeals.equals("10")) {
                totalBill*=10
            } else if (etMeals.equals("12")) {
                totalBill*=12
            } else if (etMeals.equals("14")) {
                totalBill*=14
            }

            amount = totalBill.toString()

            //Update details
            db.collection("order").document(orderid)
                .update(
                    mapOf(
                        "userpreference" to etUserPreference,
                        "mealsperweek" to etMeals,
                        "address1" to etaddressline1.text.toString(),
                        "address2" to etaddressline2.text.toString(),
                        "city" to etcity.text.toString(),
                        "state" to etstate.text.toString(),
                        "zipcode" to etzipcode.text.toString(),
                        "amount" to amount,
                    )
                )
            Toast.makeText(this, "Order Details Updated Successfully", Toast.LENGTH_SHORT).show()
            finish()

                //Pass values and navigate
                val bundle2 = Bundle()
                bundle2.putString("address1", etaddressline1.text.toString())
                bundle2.putString("address2", etaddressline2.text.toString())
                bundle2.putString("city", etcity.text.toString())
                bundle2.putString("state", etstate.text.toString())
                bundle2.putString("zipcode", etzipcode.text.toString())
                bundle2.putString("preference", etUserPreference.toString())
                bundle2.putString("meals", etMeals.toString())
                bundle2.putString("amount", amount)
                bundle2.putString("deliveryinstruction", deliveryinstruction)
                bundle2.putString("otherinstructions", otherinstructions)
                bundle2.putString("userid", userid)
                bundle2.putString("orderid", orderid)

                val intent = Intent(this, OrderDetails::class.java)
                intent.putExtras(bundle2)
                startActivity(intent)
        }
    }

    @SuppressLint("SuspiciousIndentation")
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