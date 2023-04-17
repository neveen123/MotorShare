package com.motorshare.motorshare

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.motorshare.motorshare.Fragments.MapsFragment
import android.Manifest

/**
 * activity created for Bottom Navigation
 * we will check whether user logged in or not and redirect to Starting activity if not logged in
 */
class MainActivity : AppCompatActivity() {
   var bottomNavigationView: BottomNavigationView? = null
   private lateinit var oneTapClient: SignInClient
   private lateinit var signInRequest: BeginSignInRequest
   private val LOCATION_PERMISSION_REQUEST_CODE = 1
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
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Request location permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
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
                R.id.mapsMenu -> fragment = MapsFragment()
            }
            //Replaces the fragment in the FrameLayout
            supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer, fragment!!)
                .commit()

           /** val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(MapsActivity(activity as MapsActivity?))**/
            true
        }

}


