package com.motorshare.motorshare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.motorshare.motorshare.Activities.StartingActivity
import com.motorshare.motorshare.Fragments.CreateRideFragment
import com.motorshare.motorshare.Fragments.HomeFragment
import com.motorshare.motorshare.Fragments.CustomerProfileFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

/**
 * activity created for Bottom Navigation
 * we will check whether user logged in or not and redirect to Starting activity if not logged in
 */
class MainActivity : AppCompatActivity() {
   var bottomNavigationView: BottomNavigationView? = null
   private lateinit var oneTapClient: SignInClient
   private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       //assigning the bottom navigation to navigate between the fragments
        bottomNavigationView =
            findViewById<View>(R.id.BottomNavigationView) as BottomNavigationView
        val menuNav = bottomNavigationView!!.menu

        //setting the homeFragment as default Fragment
        supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer, HomeFragment())
            .commit()
        //BottomNavigationMethod to perform bottomNavigation Action
        bottomNavigationView!!.setOnNavigationItemSelectedListener(bottomnavMethod)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // our server's client ID
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only shows accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }

    override fun onStart() {
        super.onStart()

        //checks if the user logged in or not
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            //Redirect to the starting Activity if the user is not logged
            val intent = Intent(this@MainActivity, StartingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Performs the Bottom Navigation aciton
    private val bottomnavMethod =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> //Setting the fragment to null
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.homeMenu -> fragment = HomeFragment()
                R.id.createRideMenu -> fragment = CreateRideFragment()
                R.id.profileMenu -> fragment = CustomerProfileFragment()
            }
            //Replaces the fragment in the FrameLayout
            supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer, fragment!!)
                .commit()
            true
        }
}