package com.project.fat

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.ar.sceneform.rendering.ViewRenderable
import com.project.fat.databinding.ActivityArBinding
import com.project.fat.databinding.MonsterInfoBinding
import com.project.fat.location.LocationProvider
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ViewNode


class ArActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityArBinding

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var modelNode : ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelNode = ArModelNode().apply {
            placementMode = PlacementMode.BEST_AVAILABLE
            hitPosition = Position(0.0f, 0.0f, - 10.0f)
            scale = Scale(0.2f, 0.2f, 0.2f)

            lifecycleScope.launchWhenCreated {
                val modelInstance = modelNode.loadModelGlbAsync(
                    glbFileLocation = "https://sceneview.github.io/assets/models/MaterialSuite.glb"
                ) {
                    binding.sceneView.planeRenderer.isVisible = true
                }
            }
        }

        binding.sceneView.addChild(modelNode)

        val arTextViewBinding = MonsterInfoBinding.inflate(layoutInflater)
        arTextViewBinding.info.text ="!@!@!@!@ ArActivity!!!!!!"

        ViewRenderable.builder()
            .setView(this, arTextViewBinding.root)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.TOP)
            .build()
            .thenAccept { renderable: ViewRenderable ->
                val viewNode = ViewNode()
                viewNode.parent = modelNode
                viewNode.setRenderable(renderable)
                viewNode.position = Position(x = 0.0f, y = 5.0f, z = 0.0f)
            }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_ar) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        LocationProvider.stopLocationUpdates()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        lifecycleScope.launchWhenCreated {
            fusedLocationClient = LocationProvider.fusedLocationProviderClient
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        Log.d("onLocationResult in ArActivity", "${location.latitude}, ${location.longitude}")
                        setLocation(location)
                    }
                }
            }

            LocationProvider.init(this@ArActivity, locationCallback)
        }


        mMap.isMyLocationEnabled = true

    }

    private fun setLocation(location : Location){
        Log.d("map", "setLocation")
        val myLocation = LatLng(location.latitude, location.longitude)

        val camerOption = CameraPosition.builder()
            .target(myLocation)
            .zoom(17.5f)
            .build()
        val camera = CameraUpdateFactory.newCameraPosition(camerOption)

        mMap.moveCamera(camera)
    }

    private fun setCameraPosition(location : Location){
    }
}