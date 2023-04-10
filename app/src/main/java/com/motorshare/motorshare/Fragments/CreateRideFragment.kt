package com.motorshare.motorshare.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.motorshare.motorshare.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * In this Fragment We will take Ride details as input
 * and add Ride Details details to firebase
 */
class CreateRideFragment : Fragment() {
    var riderNameEditTxt: EditText? = null
    var customerAddressEditTxt: EditText? = null
    var destinationAddressEditTxt: EditText? = null
    var totalPassengersEditTxt: EditText? = null
    var rideDateEditTxt: EditText? = null
    var rideTimeEditTxt: EditText? = null
    var ridePriceEditTxt: EditText? = null
    var phoneNumberEditTxt: EditText? = null
    var createRide: Button? = null


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
        val view = inflater.inflate(R.layout.fragment_create_ride, container, false)

        //Assigning the address of android component to perform appropriate action
        riderNameEditTxt = view.findViewById<View>(R.id.RiderNameEdit) as EditText
        customerAddressEditTxt = view.findViewById<View>(R.id.CustomerAddressEdit) as EditText
        destinationAddressEditTxt = view.findViewById<View>(R.id.DestinationAddressEdit) as EditText
        totalPassengersEditTxt = view.findViewById<View>(R.id.TotalPassengersEdit) as EditText
        rideDateEditTxt = view.findViewById<View>(R.id.RideDateEdit) as EditText
        rideTimeEditTxt = view.findViewById<View>(R.id.RideTimeEdit) as EditText
        ridePriceEditTxt = view.findViewById<View>(R.id.RidePriceEdit) as EditText
        phoneNumberEditTxt = view.findViewById<View>(R.id.PhoneNumberEdit) as EditText

        //CreateRide Butotn to update the ride details in Fireabse database
        createRide = view.findViewById<View>(R.id.CreateRideBtn) as Button


        //implementing the onclickListener to upload the Ride Details to fireabse on buttonClick
        createRide!!.setOnClickListener{
            //Gets the Test form the editTexts and assigning to Strings
            val riderName =
                riderNameEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val customerAddress = customerAddressEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(
                Locale.getDefault()
            )
            val destinationAddress =
                destinationAddressEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(
                    Locale.getDefault()
                )
            val totalPassengers = totalPassengersEditTxt!!.text.toString()
            val rideDate =
                rideDateEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val rideTime =
                rideTimeEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val ridePrice =
                ridePriceEditTxt!!.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val phoneNumber = phoneNumberEditTxt!!.text.toString().trim { it <= ' ' }

            //Shows the below toast message if eny EditText field is empty
            if (riderName.isEmpty() || customerAddress.isEmpty() || destinationAddress.isEmpty() || totalPassengers.isEmpty() || rideDate.isEmpty() || rideTime.isEmpty() || ridePrice.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(view.context, "Please, Fill all the details", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //Calls the below method to update the data in firebase
                AddDataToFirebase(
                    riderName,
                    customerAddress,
                    destinationAddress,
                    totalPassengers,
                    rideDate,
                    rideTime,
                    ridePrice,
                    phoneNumber
                )
            }
        }
        return view
    }

    private fun AddDataToFirebase(
        riderName: String,
        customerAddress: String,
        destinationAddress: String,
        totalPassengers: String,
        rideDate: String,
        rideTime: String,
        ridePrice: String,
        phoneNumber: String
    ) {

        //Accessing the google user id to create a unique ride  for unique users
        val userId = GoogleSignIn.getLastSignedInAccount(requireActivity())!!.id

        //Fireabase database path to store the Ride details
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Rides")

        //Creating the HashMap to store the Ride Details
        val userDetails = HashMap<String, String?>()


        //Assigining the customer Address and Destination Address to make Ride search Easy
        val customerAddrAndDestinationAddr = customerAddress + destinationAddress

        //Storeing the ride Details in the HashMap
        userDetails["riderName"] = riderName
        userDetails["customerAddress"] = customerAddress
        userDetails["destinationAddress"] = destinationAddress
        userDetails["customerAddrAndDestinationAddr"] = customerAddrAndDestinationAddr
        userDetails["rideDate"] = rideDate
        userDetails["rideTime"] = rideTime
        userDetails["ridePrice"] = ridePrice
        userDetails["totalPassengers"] = totalPassengers
        userDetails["userId"] = userId
        userDetails["phoneNumber"] = phoneNumber

        //Setting the Ride details in the firebase Database
        myRef.child(userId!!).setValue(userDetails).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                //Shows the bellow message when the Ride Details Successfully update to Database
                Toast.makeText(context, "Ride Created Successfully", Toast.LENGTH_SHORT).show()

                //Setting the editText Fields to empty after updating the data in fireabse
                riderNameEditTxt!!.setText("")
                customerAddressEditTxt!!.setText("")
                destinationAddressEditTxt!!.setText("")
                totalPassengersEditTxt!!.setText("")
                rideDateEditTxt!!.setText("")
                rideTimeEditTxt!!.setText("")
                ridePriceEditTxt!!.setText("")
                phoneNumberEditTxt!!.setText("")
            }
            else{
                Toast.makeText(context, "Ride was not created successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): CreateRideFragment {
            val fragment = CreateRideFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}