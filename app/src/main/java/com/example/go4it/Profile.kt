package com.example.go4it

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView


class Profile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    //Drawer
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var fName: TextView
    private lateinit var lName: TextView
    private lateinit var Email: TextView
    private lateinit var Phone: TextView

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        fName = findViewById(R.id.firstName)
        lName = findViewById(R.id.lastName)
        Email = findViewById(R.id.email)
        Phone = findViewById(R.id.pNumber)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

       val ref = db.collection("users").document(userId)
       ref.get().addOnSuccessListener {
            if(it!=null){
                val fName = it.data?.get("firstname")?.toString()
                val lName = it.data?.get("lastname")?.toString()
                val email = it.data?.get("email")?.toString()
                val phone = it.data?.get("phone")?.toString()

                firstName.text = fName
                lastName.text = lName
                Email.text = email
                Phone.text = phone

                //Pass values and navigate
                editProfile.setOnClickListener{
                    val bundle = Bundle()
                    bundle.putString("fName", firstName.text.toString())
                    bundle.putString("lName", lastName.text.toString())
                    bundle.putString("email", Email.text.toString())
                    bundle.putString("phone", Phone.text.toString())

                    val intent = Intent(this, EditProfile::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
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
}