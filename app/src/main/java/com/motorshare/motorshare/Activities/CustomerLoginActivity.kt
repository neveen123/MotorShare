package com.motorshare.motorshare.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.motorshare.motorshare.R
import com.motorshare.motorshare.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import android.Manifest

/**
 * activity created to make the user login using google account and
 *  add the user details to firebase database
 */
class CustomerLoginActivity() : AppCompatActivity() {
    private var SignInClient: GoogleSignInClient? = null
    private var firebaseAuth: FirebaseAuth? = null
    var progressDialog: ProgressDialog? = null
    private var loginButton: Button? = null
    var phoneNumberEditTxt: EditText? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Assigned Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        loginButton = findViewById<View>(R.id.loginBtn) as Button
        phoneNumberEditTxt = findViewById<View>(R.id.phoneNumberEditTxt) as EditText

        //ProgressDialog is set To indicate Background Action is taking place as
        //user is trying to sign in with their gmail
        progressDialog = ProgressDialog(this)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setMessage("We are getting everything ready...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)


        //Firebase SignOptions to Register User using Gmail
        //Specifies that an ID token for authenticated users is requested.
        // Requesting an ID token requires that the server client ID be specified
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken( "772995514777-8hkugsmvb7ng697743tplqijgud8ak9p.apps.googleusercontent.com")
            .requestEmail()
            .build()
        SignInClient = GoogleSignIn.getClient(applicationContext, signInOptions)

        //implementation of OnClickListener  to Perform signIn operation on Button click
        loginButton!!.setOnClickListener(View.OnClickListener {
            //Gets phone number from the EditText
            val phoneNumber = filterNumber(phoneNumberEditTxt!!.text.toString())

            if (phoneNumber.length < 10) {

                //Shows the Below Toast if the number editText is empty
                Toast.makeText(
                    applicationContext,
                    "Please enter your number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                //Shows all the available mail ids in device
                val intent = SignInClient!!.signInIntent
                startActivityForResult(intent, 100)
            }
        })
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
    }

    public fun filterNumber(phoneNumber: String): String{
        var phoneNumberEdit = phoneNumber
        phoneNumberEdit = phoneNumberEdit.filterNot { it <= ' ' }
        phoneNumberEdit = phoneNumberEdit.filterNot { it <= '-' }
        phoneNumberEdit = phoneNumberEdit.filterNot { it <= '(' }
        phoneNumberEdit = phoneNumberEdit.filterNot { it <= ')' }
        phoneNumberEdit = phoneNumberEdit.filterNot { it <= '+' }
        return phoneNumberEdit
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val googleSignInAccountTask = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            //Checks if SignIn is Successful or not
            if (googleSignInAccountTask.isSuccessful) {
                progressDialog!!.show()
                try {
                    val googleSignInAccount = googleSignInAccountTask.getResult(
                        ApiException::class.java
                    )
                    if (googleSignInAccount != null) {
                        val authCredential = GoogleAuthProvider
                            .getCredential(googleSignInAccount.idToken, null)

                        //Makes the user signin with the Registered Gmail
                        firebaseAuth!!.signInWithCredential(authCredential)
                            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                                override fun onComplete(task: Task<AuthResult?>) {
                                    if (task.isSuccessful) {

                                        //Firebase database path to store user details
                                        val database = FirebaseDatabase.getInstance()
                                        val myRef = database.reference.child("users")
                                        val userDetails = HashMap<String, String>()

                                        //Access the user details from the registered Gmail
                                        val id = googleSignInAccount.id.toString()
                                        val name = googleSignInAccount.displayName.toString()
                                        val mail = googleSignInAccount.email.toString()
                                        val pic = googleSignInAccount.photoUrl.toString()
                                        val phoneNumber =
                                            phoneNumberEditTxt!!.text.toString().trim { it <= ' ' }


                                        //Stores the above details in a HashMap
                                        userDetails["userId"] = id
                                        userDetails["userName"] = name
                                        userDetails["mail"] = mail
                                        userDetails["profilePic"] = pic
                                        userDetails["phoneNumber"] = phoneNumber


                                        //Add the Details HashMap to the Firebase database
                                        myRef.child(id).setValue(userDetails)
                                            .addOnCompleteListener(object :
                                                OnCompleteListener<Void?> {
                                                override fun onComplete(task: Task<Void?>) {
                                                    if (task.isSuccessful) {

                                                        //Navigates to MainActivity if userDetails inserted to firebase Database
                                                        progressDialog!!.cancel()
                                                        val intent = Intent(
                                                            applicationContext,
                                                            MainActivity::class.java
                                                        )
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                        startActivity(intent)
                                                    }
                                                }
                                            })
                                    }
                                }
                            })
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}