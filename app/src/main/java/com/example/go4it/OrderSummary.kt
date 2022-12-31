package com.example.go4it

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.android.synthetic.main.activity_ordersummary.*

import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderSummary : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var meal:String
    lateinit var total:String
    lateinit var preferences:String
    lateinit var address1:String
    lateinit var address2:String
    lateinit var city:String
    lateinit var state:String
    lateinit var zipcode:String
    lateinit var instructions:String
    lateinit var otherinstructions:String
    lateinit var orderid:String
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordersummary)

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
            instructions  = bundle.getString("instructions").toString()
            otherinstructions  = bundle.getString("otherinstructions").toString()
        }

        quantity.text = meal
        preference.text = preferences
        instruction.text = instructions
        totalPrice.text = "$" +total

        setupPaymentButton()
    }

    private fun setupPaymentButton() {
        val amount = total
        paypalButton.setup(
            createOrder = CreateOrder { createOrderActions ->
                createOrderActions.create(
                    Order.Builder()
                        .appContext( AppContext( userAction = UserAction.PAY_NOW ))
                        .intent(OrderIntent.CAPTURE)
                        .purchaseUnitList(
                            listOf(
                                PurchaseUnit.Builder()
                                    .amount(Amount.Builder().value(amount).currencyCode(CurrencyCode.USD)
                                            .build()
                                    )
                                    .build()
                            )
                        )
                        .build()
                )
            },
            onApprove = OnApprove { approval ->
                //Store order details to the database on successful payment
                val userId = FirebaseAuth.getInstance().currentUser!!.email

                val orderid = db.collection("order").document().id
                approval.orderActions.capture { captureOrderResult ->
                    val order= hashMapOf(
                        "address1" to address1,
                        "address2" to address2,
                        "city" to city,
                        "state" to state,
                        "zipcode" to zipcode,
                        "userpreference" to preferences,
                        "mealsperweek" to meal,
                        "amount" to amount,
                        "deliveryinstruction" to instructions,
                        "otherinstructions" to otherinstructions,
                        "userid" to userId,
                        "orderid" to orderid
                    )
                   db.collection("order").document(orderid).set(order)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Order has been Placed", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,OrderList::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to add due to " + e.message, Toast.LENGTH_SHORT).show()
                        }
                }
            },
            onCancel = OnCancel {
                Toast.makeText(this, "Successfully cancelled", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Toast.makeText(this, "Payment method Failed due to $errorInfo", Toast.LENGTH_SHORT).show()
            }
        )
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
