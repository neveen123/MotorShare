package com.motorshare.motorshare.Fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.MessageQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MapsFragment : Fragment(), OnPoiClickListener {

    private val callback = OnMapReadyCallback { googleMap:GoogleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        // Add a marker in greenville  and move the camera
        googleMap.setOnPoiClickListener(this)
        val greenville = LatLng(35.6127, -77.3664)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.addMarker(MarkerOptions().position(greenville).title("Greenville, NC"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(greenville))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        googleMap.isTrafficEnabled = true
    }
    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(requireActivity(), "Clicked!: ${poi.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}
