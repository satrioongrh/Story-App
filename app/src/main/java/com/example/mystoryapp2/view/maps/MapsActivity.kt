package com.example.mystoryapp2.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp2.R
import com.example.mystoryapp2.data.viewmodel.StoryViewModel
import com.example.mystoryapp2.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mystoryapp2.view.utils.SessionPref
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var pref: SessionPref
    private var tempToken = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java]
        pref = SessionPref(this)
        tempToken = "bearer ${pref.getToken}"


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
        mMap = googleMap

        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        storyViewModel.storyList.observe(this) { storyList ->
            for (story in storyList!!) {
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            (story.lat ?: 0.0) as Double,
                            (story.lon ?: 0.0) as Double
                        )
                    ).title(story.name).snippet(story.description)
                )?.tag = story
            }
        }

        storyViewModel.loadStoryLocationData(this, tempToken)
        storyViewModel.corTemp.observe(this) {
            CameraUpdateFactory.newLatLngZoom(it, 4f)
        }
    }
}