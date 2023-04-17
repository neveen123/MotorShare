package com.motorshare.motorshare.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.motorshare.motorshare.R
import com.motorshare.motorshare.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnPoiClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        binding = ActivityMapsBinding.inflate(layoutInflater)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnPoiClickListener(this)
        mMap = googleMap

        // Add a marker in greenville  and move the camera
        val greenville = LatLng(35.6127, -77.3664)
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mMap.addMarker(MarkerOptions().position(greenville).title("Greenville, NC"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(greenville))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        mMap.isTrafficEnabled=true
    }

    //respond to a user tapping on a POI such as a business or building
    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(this, """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_SHORT
        ).show()
    }

}