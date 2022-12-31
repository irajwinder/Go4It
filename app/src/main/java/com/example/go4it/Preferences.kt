package com.example.go4it

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_preferences.*
import java.util.*


class Preferences : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var notificationManager: NotificationManager? = null
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

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

        val meal = findViewById<Button>(R.id.nextbtn)
        // Get radio group selected status and text using button click event
        meal.setOnClickListener{
            // Get the checked radio button id from radio group
            var id: Int = radio_group.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:RadioButton = findViewById(id)
                    val intent = Intent(this,Meals::class.java)
                    intent.putExtra("preference",radio.text.toString())
                    startActivity(intent)
            }else{
                // If no radio button checked in this radio group
                Toast.makeText(applicationContext,
                        " Please select your preference",
                    Toast.LENGTH_SHORT).show()
            }
        }

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        setNotification();

        createNotificationChannel()

    }

    //Navigate as per the button click
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_account -> {
                startActivity(Intent(this, Profile::class.java))
            }

            R.id.nav_order -> {
                startActivity(Intent(this, OrderList::class.java))
                drawerLayout.close()
            }

            R.id.nav_order_history -> {
                startActivity(Intent(this, OrderHistory::class.java))
                drawerLayout.close()
            }

            R.id.nav_menu -> {
                startActivity(Intent(this, Menu::class.java))
                drawerLayout.close()
            }
            R.id.nav_logout -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, SignIn::class.java))
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                finish()
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


    private fun createNotificationChannel() {

        // Create a notification manager object.
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                getString(R.string.channel_id),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.channel_description)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
    }


    private fun setNotification(){

        // Set up the Notification Broadcast Intent.
        val notifyIntent = Intent(this, AlarmReceiver::class.java)

        var notifyPendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
         notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_MUTABLE
            )
        } else {
          notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //Reference Link: https://www.tutorialspoint.com/how-to-implement-alarmmanager-in-android-using-kotlin
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val timePickerDialog: TimePickerDialog
        val calendar =
            Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 14
        calendar[Calendar.MINUTE] = 5
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, notifyPendingIntent
        )
    }

    companion object {
        // Notification ID.
        private const val NOTIFICATION_ID = 0
        // Notification channel ID.
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        var instance: MainActivity? = null
            private set
    }
}
