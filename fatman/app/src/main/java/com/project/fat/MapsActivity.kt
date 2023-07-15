package com.project.fat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.fat.data.permission.MAXNUM_MARKER
import com.project.fat.databinding.ActivityMapsBinding
import com.project.fat.databinding.CustomDialogBinding
import com.project.fat.location.LocationProvider
import kotlin.random.Random

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mMap : GoogleMap
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback

    private var numOfMarker = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationProvider.stopLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        lifecycleScope.launchWhenCreated {
            mMap = googleMap
            val dialogBinding = CustomDialogBinding.inflate(layoutInflater)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        Log.d("onLocationResult in MapsActivity", "${location.latitude}, ${location.longitude}")
                        setLocation(location)
                    }
                }
            }

            LocationProvider.init(this@MapsActivity, fusedLocationClient, locationCallback)

            //마커 클릭 이벤트
            mMap.setOnMarkerClickListener{
                val marker = it
                //해당되는 뷰와 다른 뷰가 부모로 설정된 것을 삭제
                val parentView = dialogBinding.root.parent as? ViewGroup
                parentView?.removeView(dialogBinding.root)
                //다이얼로그 동적 생성
                Log.d("MarkerClick", "${it.position}")
                val dialog = AlertDialog.Builder(this@MapsActivity)
                    .setView(dialogBinding.root)
                    .create()

                dialog.setOnShowListener {
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                //다이얼로그 예상거리&몬스터이미지 입력 부분
                dialogBinding.expectedKilometer.text = "2km"
                Glide.with(dialogBinding.root)
                    .load("https://cdn-icons-png.flaticon.com/512/104/104663.png")
                    .into(dialogBinding.monsterImage)

                dialogBinding.giveup.setOnClickListener{
                    Log.d("DialogSetOnClickListener", "give up")
                    dialog.dismiss()
                }

                dialogBinding.move.setOnClickListener {
                    Log.d("DialogSetOnClickListener", "move")
                    val intent = Intent(this@MapsActivity, RunningTimeActivity::class.java)

                    marker.remove()
                    numOfMarker--

                    dialog.dismiss()

                    startActivity(intent)
                }

                dialog.show()

                true
            }
            Log.d("map", "map setting")
        }
    }

    private fun setLocation(location: Location) {
        Log.d("map", "setLocation")
        val myLocation = LatLng(location.latitude, location.longitude)

        val cameraOption = CameraPosition.builder()
            .target(myLocation)
            .zoom(17.0f)
            .build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)

        val icon = setMarkerBitmap()

        val markerCnt = numOfMarker

        mMap.moveCamera(camera)

        for(i in markerCnt..MAXNUM_MARKER) {
            setMonsterMarker(icon, location)
        }
    }

    private fun setMonsterMarker(icon : Bitmap, location : Location) {
        Log.d("map", "setMonsterMarker")
        val rdLatLng = randomLatLng()
        val markerLocation = LatLng(location.latitude+rdLatLng[0], location.longitude+rdLatLng[1])

        val markerOption = MarkerOptions()
            .position(markerLocation)
            .icon(BitmapDescriptorFactory.fromBitmap(icon))

        mMap.addMarker(markerOption)

        numOfMarker++
    }

    private fun randomLatLng() : Array<Double> {
        Log.d("map", "make random LatLng")
        val randomLat = Random.nextDouble(-0.0025, 0.0025)
        val randomLng = if(randomLat < 0) 0.0025+randomLat else 0.0025-randomLat
        return arrayOf(randomLat, randomLng)
    }

    private fun setMarkerBitmap(): Bitmap {
        val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.monster_spot, null) as BitmapDrawable
        val b = bitmapDraw.bitmap
        return Bitmap.createScaledBitmap(b, 100, 100, false)
    }
}