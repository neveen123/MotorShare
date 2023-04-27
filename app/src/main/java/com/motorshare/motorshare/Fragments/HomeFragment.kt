package com.motorshare.motorshare.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.motorshare.motorshare.Adapter.RideInfoAdapter
import com.motorshare.motorshare.Model.Model
import com.motorshare.motorshare.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * In this fragment we will show the ride details in RecyclerView
 *so that the user can search for previous rides
 */
class HomeFragment : Fragment() {
    private var searchRideButton: Button? = null
    private var customerAddressEditText: EditText? = null
    private var destinationAddressEditText: EditText? = null
    private var rideInfoAdapter: RideInfoAdapter? = null
    private var recyclerView: RecyclerView? = null

    // TODO: Rename and change types of parameters
    /** var mParam1: String? = null
    var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (arguments != null) {
    mParam1 = requireArguments().getString(ARG_PARAM1)
    mParam2 = requireArguments().getString(ARG_PARAM2)
    }
    }**/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Assigning the Address of the android components to Perform appropriate action
        searchRideButton = view.findViewById<View>(R.id.SearchRideBtn) as Button
        customerAddressEditText = view.findViewById<View>(R.id.SearchCustomerEdit) as EditText
        destinationAddressEditText = view.findViewById<View>(R.id.SearchDestinationEdit) as EditText

        //Recycler view to shows the Rides
        recyclerView = view.findViewById<View>(R.id.RecyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = rideInfoAdapter


        //Implementation of onClickListener to find the Rides
        searchRideButton!!.setOnClickListener { //Getting the String from the EditText
            val customerAddress =
                customerAddressEditText!!.text.toString().trim { it <= ' ' }.lowercase(
                    Locale.getDefault()
                )
            val destinationAddress =
                destinationAddressEditText!!.text.toString().trim { it <= ' ' }.lowercase(
                    Locale.getDefault()
                )

            //Adding the customer address and Destination Address to make search easy
            val customerAndDestinatinAddr = customerAddress + destinationAddress

            //FirebaseRecyclerOptions to Populate the data form firebase
            val options = FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(
                    FirebaseDatabase.getInstance().reference.child("Rides")
                        .orderByChild("customerAddrAndDestinationAddr")
                        .equalTo(customerAndDestinatinAddr), Model::class.java
                )
                .build()
            rideInfoAdapter = RideInfoAdapter(options)
            //Starting the Adapter To listen for Data
            rideInfoAdapter!!.startListening()

            recyclerView = view.findViewById<View>(R.id.RecyclerView) as RecyclerView
            recyclerView!!.layoutManager = LinearLayoutManager(context)
            recyclerView!!.adapter = rideInfoAdapter
            //Setting the Adapter to RecyclerView
            //  recyclerView!!.adapter = rideInfoAdapter
        }
        return view
    }

    /** companion object {
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"
    fun newInstance(param1: String?, param2: String?): HomeFragment {
    val fragment = HomeFragment()
    val args = Bundle()
    args.putString(ARG_PARAM1, param1)
    args.putString(ARG_PARAM2, param2)
    fragment.arguments = args
    return fragment
    }**/
}

