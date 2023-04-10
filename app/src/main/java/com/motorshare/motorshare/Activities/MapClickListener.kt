package com.motorshare.motorshare.Activities

import android.location.Location
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapClickListener(locationList: MutableList<Location>) : OnMapClickListener {
    private val locationList: MutableList<Location>
    override fun onMapClick(latLng: LatLng) {
        // Create a new Location object with the latLng
        val location = Location(latLng.latitude, latLng.longitude)
        // Add the location to the list
        locationList.add(location)
        // Add a marker to the map at the clicked location
        val markerOptions = MarkerOptions().position(latLng)
        GoogleMap(location).addMarker(markerOptions)
    }

    init {
        this.locationList = locationList
    }

    fun onMapReady(googleMap: GoogleMap) {
        // Set the map click listener
        val mapClickListener = MapClickListener(locationList)
        googleMap.setOnMapClickListener(mapClickListener)
    }

}