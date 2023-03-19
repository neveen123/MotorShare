package com.motorshare.motorshare.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.motorshare.motorshare.Activities.EditCustomerDetailsActivity
import com.motorshare.motorshare.Activities.StartingActivity
import com.motorshare.motorshare.Model.Model
import com.motorshare.motorshare.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * We will show the user details in this fragment and signOut Option
 */
class CustomerProfileFragment : Fragment() {
    var circleImageView: CircleImageView? = null
    var userName: TextView? = null
    var phoneNumber: TextView? = null
    var signOutBtn: Button? = null
    var editDetailsBtn: Button? = null
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //Accessioning the userId From google SignIn to access user Details
        val userId = GoogleSignIn.getLastSignedInAccount(requireActivity())!!.id

        //Assigning the Addresses of the android components to show the data and perform appropriate action
        circleImageView = view.findViewById<View>(R.id.ProfilePicImg) as CircleImageView
        userName = view.findViewById<View>(R.id.UserNameTxt) as TextView
        phoneNumber = view.findViewById<View>(R.id.phoneNumberTxt) as TextView

        //Signout Button to make the user signout from the current account
        signOutBtn = view.findViewById<View>(R.id.SignOutBtn) as Button

        //edit details button to make the user change his Details
        editDetailsBtn = view.findViewById<View>(R.id.EditDetailsBtn) as Button

        //implementing the OnclickListener to change the Activity
        editDetailsBtn!!.setOnClickListener { //When we click on the editDetails Button.we will redirect to EditDetails Activity
            val intent = Intent(context, EditCustomerDetailsActivity::class.java)
            startActivity(intent)
        }

        //Firebase Database Path to access the user details and for showing user details
        FirebaseDatabase.getInstance().reference.child("users")
            .child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    //using the model class to get the data from the database
                    val model = snapshot.getValue(
                        Model::class.java
                    )

                    //using  picasso repository to  load the image into image view from a url
                    Picasso.get().load(model!!.profilePic).into(circleImageView)
                    userName!!.text = model.userName
                    phoneNumber!!.text = model.phoneNumber
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        //implementing the signOut button
        signOutBtn!!.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

            //Accesses the current users
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //makes the user Signout From the App
                    FirebaseAuth.getInstance().signOut()

                    //Changes the intent after user Signout
                    val intent = Intent(context, StartingActivity::class.java)

                    //Clears all the previous intents
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }
        return view
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): CustomerProfileFragment {
            val fragment = CustomerProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}