package com.motorshare.motorshare.Activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.motorshare.motorshare.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase

/**
 *activity created to take user inputs and update the details in firebase Database
 */
class EditCustomerDetailsActivity : AppCompatActivity() {
    private var updatePhoneNumber: EditText? = null
    private var submitDataBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_details)

        //Assigning the address of android components to perform Action
        updatePhoneNumber = findViewById<View>(R.id.updatePhoneNumberEditTxt) as EditText
        submitDataBtn = findViewById<View>(R.id.submitDetailsBtn) as Button
        //User id to get User Details from database
        val userId = GoogleSignIn.getLastSignedInAccount(applicationContext)!!
            .id

        //implementation of onClickListener to store data in firebase on click button
        submitDataBtn!!.setOnClickListener {
            //Get Text from the EditText
            val phoneNumber = updatePhoneNumber!!.text.toString()
            if (phoneNumber.isEmpty()) {

                //Shows this toast if number EditText is Empty
                Toast.makeText(applicationContext, "Please,Enter Your  Number", Toast.LENGTH_SHORT)
                    .show()
            } else {

                //Firebase Path to Update data in database
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(userId!!)
                    .child("phoneNumber")
                    .setValue(phoneNumber).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Phone Number Updated Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            //Sets the EditText as Empty after Updating the data in Database
                            updatePhoneNumber!!.setText("")
                        }
                    }
            }
        }
    }
}