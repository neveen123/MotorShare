package com.motorshare.motorshare.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.motorshare.motorshare.Adapter.RideInfoAdapter.Viewholder
import com.motorshare.motorshare.MainActivity
import com.motorshare.motorshare.Model.Model
import com.motorshare.motorshare.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


/**An adapter class that connects the User Interfaces and the Data Source.
 *  returns saved list of data to display in home fragment
 *
 */
class RideInfoAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, Viewholder>(options) {

    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {

        //Gets The Data from Firebase Database using model class and sets the individual data in Holder
        holder.riderName.text = "Rider Name: " + model.riderName
        holder.customerAddress.text = "Customer Address: " + model.customerAddress
        holder.destinationAddress.text = "Destination Address: " + model.destinationAddress
        holder.totalPassengers.text = "Total Passengers: " + model.totalPassengers
        holder.rideDate.text = "Ride date: " + model.rideDate
        holder.rideTime.text = "Ride Time: " + model.rideTime
        holder.ridePrice.text = "Ride Price: " + model.ridePrice
        holder.riderPhoneNumber.text = "Rider Mobile: " + model.phoneNumber


        //Implementing the onClickListner to Book Ticket For User
        holder.bookRide.setOnClickListener { view ->
            val context = view.context
            //Accessing No of Seats available in the Vehicle and decreases the value when user books one seat
            val totalPassengersInt = (model.totalPassengers?.toInt() ?: - 1)

            //Books seats for the user if total available seats are greater than 0
            if (totalPassengersInt > 0) {

                //Decreases the Total available seats in database
                model.userId?.let {
                    FirebaseDatabase.getInstance().reference.child("Rides").child(it)
                        .child("totalPassengers").setValue(totalPassengersInt.toString())
                }
                Toast.makeText(view.context, "Successfully Registered for Ride", Toast.LENGTH_SHORT)
                    .show()

                //Restarts the intent to clear all the values after booking seat
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } else {

                //Shows this toast if total seats are booked in vehicle
                Toast.makeText(context, "We apologize all seats are completely booked.Please choose another ride", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //inflates the Xml Resource file
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_ride_details, parent, false)
        return Viewholder(view)
    }

    //ViewHolder to hold each individual data object and to show it in Recyclerview
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var riderName: TextView
        var customerAddress: TextView
        var destinationAddress: TextView
        var totalPassengers: TextView
        var rideDate: TextView
        var rideTime: TextView
        var ridePrice: TextView
        var riderPhoneNumber: TextView
        var bookRide: Button

        init {


            //Assigning the Address of Android components to show data in the appropriate component
            riderName = itemView.findViewById<View>(R.id.RiderNameTxt) as TextView
            customerAddress = itemView.findViewById<View>(R.id.CustomerAddressTxt) as TextView
            destinationAddress = itemView.findViewById<View>(R.id.DestinationAddressTxt) as TextView
            totalPassengers = itemView.findViewById<View>(R.id.TotalPassengersTxt) as TextView
            rideDate = itemView.findViewById<View>(R.id.RideDateTxt) as TextView
            rideTime = itemView.findViewById<View>(R.id.RideTimeTxt) as TextView
            ridePrice = itemView.findViewById<View>(R.id.RidePriceTxt) as TextView
            riderPhoneNumber = itemView.findViewById<View>(R.id.RiderPhoneNumber) as TextView

            //Button To book a seat for the user
            bookRide = itemView.findViewById<View>(R.id.BookRideBtn) as Button
        }
    }
}