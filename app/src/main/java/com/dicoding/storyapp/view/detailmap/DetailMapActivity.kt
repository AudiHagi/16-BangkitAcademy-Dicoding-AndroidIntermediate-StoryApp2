package com.dicoding.storyapp.view.detailmap

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailMapBinding
import com.dicoding.storyapp.utils.DateFormatter
import com.dicoding.storyapp.view.detailstory.DetailStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.TimeZone

class DetailMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailMapBinding
    private var username: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var formattedDate: String? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        username = intent.getStringExtra(DetailStoryActivity.USERNAME)
        date = intent.getStringExtra(DetailStoryActivity.DATE)
        latitude = intent.getStringExtra(DetailStoryActivity.LAT)
        longitude = intent.getStringExtra(DetailStoryActivity.LON)
        formattedDate = DateFormatter.formatDate(date!!, TimeZone.getDefault().id)
        lat = latitude!!.toDouble()
        lon = longitude!!.toDouble()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        val location = LatLng(lat, lon)
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(username)
                .snippet(
                    StringBuilder("Created: ")
                        .append(formattedDate)
                        .toString()
                )
        )
        setMapStyle()
        marker?.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setupView() {
        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.detail_map)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val USERNAME = "name"
        const val DATE = "date"
        const val LAT = "lat"
        const val LON = "lon"
        private const val TAG = "DetailMapActivity"
    }
}