package com.digmoy.testapllicationdigmoy.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digmoy.testapllicationdigmoy.R
import com.digmoy.testapllicationdigmoy.SplashActivity.Companion.latitude
import com.digmoy.testapllicationdigmoy.SplashActivity.Companion.longitude
import com.digmoy.testapllicationdigmoy.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {

    private var mMap: GoogleMap? = null
    private lateinit var binding : FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater,container,false)
        val supportMapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            mMap = googleMap

            // Add a marker in Sydney and move the camera

            // Add a marker in Sydney and move the camera

            val sydney = LatLng(latitude!!.toDouble(), longitude!!.toDouble())
            mMap!!.addMarker(MarkerOptions().position(sydney).title("I am here"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
        return binding.root
    }


}