package com.motorshare.motorshare.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.motorshare.motorshare.R

/**
 *  activity created to display the intro of app in which the customer sees  upon launching
 */
class StartingActivity : AppCompatActivity() {
    var getStartedBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)

        //Assigning the Button to perform  Appropriate Action
        getStartedBtn = findViewById<View>(R.id.GetStartedBtn) as Button

        //implementing the onClickListener to Change the intent
        getStartedBtn!!.setOnClickListener {
            val intent = Intent(applicationContext, CustomerLoginActivity::class.java)
            startActivity(intent)
        }
    }
}