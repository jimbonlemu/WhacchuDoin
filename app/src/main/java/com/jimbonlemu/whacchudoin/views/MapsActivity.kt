package com.jimbonlemu.whacchudoin.views

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityMapsBinding
import com.jimbonlemu.whacchudoin.view_models.MapsViewModel
import org.koin.android.ext.android.inject

class MapsActivity : CoreActivity<ActivityMapsBinding>(), OnMapReadyCallback {

    private val mapViewModel: MapsViewModel by inject()
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapViewModel.getAllStoriesWithLocation()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityMapsBinding =
        ActivityMapsBinding.inflate(layoutInflater)

    override fun onMapReady(maps: GoogleMap) {
        maps.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
        setMapStyle(maps)

        mapViewModel.storyResult.observe(this@MapsActivity) { result ->
            when (result) {
                is ResponseState.Loading -> getToast(getString(R.string.loading_map))

                is ResponseState.Success -> {
                    result.data.listStory.forEach { story ->
                        val latLng = LatLng(story.lat, story.lon)
                        maps.addMarker(MarkerOptions().position(latLng).title(story.name))
                        boundsBuilder.include(latLng)
                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    maps.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }

                is ResponseState.Error -> getToast(result.errorMessage)

                else -> {}
            }

        }
    }

    private fun setMapStyle(maps: GoogleMap) {
        try {
            val success =
                maps.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this@MapsActivity,
                        R.raw.map_styles
                    )
                )
            if (!success) {
                getToast(getString(R.string.failed_to_apply_map_style))
            }
        } catch (exception: Resources.NotFoundException) {
            getToast(getString(R.string.cannot_find_map_style, exception))
        }
    }

    private fun getToast(msg: String) {
        Toast.makeText(this@MapsActivity, msg, Toast.LENGTH_SHORT).show()
    }
}