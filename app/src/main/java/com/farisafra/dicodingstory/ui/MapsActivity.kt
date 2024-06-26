package com.farisafra.dicodingstory.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.repository.Result
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.viewmodel.StoriesViewModel
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.farisafra.dicodingstory.databinding.ActivityMapsBinding
import com.farisafra.dicodingstory.ui.customview.ResponseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var vmFactory: ViewModelFactory
    private val storiesViewModel: StoriesViewModel by viewModels { vmFactory }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        fetchStories()
        menuMaps()
        backActivity()
        myLocation()
        addStory()
    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
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

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        fetchStories()
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(currentLatLng)
                                .title("My Location")
                                .snippet("You are here!")
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getDeviceLocation()
            }
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

    private fun menuMaps() {
        binding.btnSetting.setOnClickListener {
            showMapOptions()
        }
    }
    private fun showMapOptions() {
        val popupMenu = PopupMenu(this, binding.btnSetting)
        popupMenu.menuInflater.inflate(R.menu.maps_options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
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
        popupMenu.show()
    }

    private fun fetchStories() {
        storiesViewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        storyMarker(result.data.listStory)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        errorResponse()
                    }
                }
            }
        }
    }

    private fun myLocation() {
        binding.btnMyLocation.setOnClickListener {
            getDeviceLocation()
        }
    }
    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun addStory() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToMain(): () -> Unit {
        return {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error, moveToMain()).show()
    }

    private fun storyMarker(listStory: List<Story>) {
        listStory.forEach{story ->
            val latLng = LatLng(story.lat, story.lon)

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.markerstory)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 128, 128, false)

            mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(story.name)
                .snippet(story.description)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)))
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                10
            )
        )
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}