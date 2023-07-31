package com.project.fat

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.project.fat.data.marker.Marker
import com.project.fat.data.permission.Permission
import com.project.fat.databinding.ActivityMapsBinding
import com.project.fat.databinding.CustomDialogBinding
import com.project.fat.location.LocationProvider
import java.lang.Exception
import java.util.jar.Manifest
import kotlin.random.Random
import kotlin.system.exitProcess

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mMap : GoogleMap

    private var cachedBitmap: Bitmap? = null
    private var numOfMarker = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, Permission.PERMISSIONS, Permission.PERMISSION_FLAG)
            Log.d("LocationProvider", "need permissions")
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lifecycleScope.launchWhenCreated {
            Log.d("LocationProvider", "fusedLocationClient")

            val icon = setMarkerBitmap()

            LocationProvider.setLocationCallback(object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        Log.d("onLocationResult in MapsActivity", "${location.latitude}, ${location.longitude}")
                        setLocation(icon, location)
                    }
                }
            })

            LocationProvider.setFusedLocationProviderClient(this@MapsActivity)
            LocationProvider.setLocationRequest()
            LocationProvider.requestLocationUpdates(this@MapsActivity)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationProvider.stopLocationUpdates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)

        if(mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_simple_style))){
            Log.d("LocationProvider", "setMapStyle success")
        }else{
            Log.d("LocationProvider", "setMapStyle false")
            try{
                startActivity(Intent.makeRestartActivityTask(packageManager.getLaunchIntentForPackage(packageName)?.component))
                exitProcess(0)
            }catch (e : Exception){
                Log.d("LocationProvider", "setMapStyle restart false")
                Toast.makeText(this@MapsActivity, "오류 : 재시작을 실패했습니다. 앱을 나갔다가 다시 실행해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

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
        Log.d("LocationProvider", "map setting")
    }

    private fun setLocation(icon: Bitmap, location: Location) {
        Log.d("LocationProvider", "setLocation")
        val myLocation = LatLng(location.latitude, location.longitude)

        val cameraOption = CameraPosition.builder()
            .target(myLocation)
            .zoom(17.0f)
            .build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)

        val markerCnt = numOfMarker

        mMap.moveCamera(camera)

        for(i in markerCnt..Marker.MAXNUM_MARKER) {
            setMonsterMarker(icon, location)
        }
    }

    private fun setMonsterMarker(icon : Bitmap, location : Location) {
        Log.d("LocationProvider", "setMonsterMarker")
        val rdLatLng = randomLatLng()
        val markerLocation = LatLng(location.latitude+rdLatLng[0], location.longitude+rdLatLng[1])

        val markerOption = MarkerOptions()
            .position(markerLocation)
            .icon(BitmapDescriptorFactory.fromBitmap(icon))

        mMap.addMarker(markerOption)

        numOfMarker++
    }

    private fun randomLatLng() : Array<Double> {
        Log.d("LocationProvider", "make random LatLng")
        val randomLat = Random.nextDouble(-0.0025, 0.0025)
        val randomLng = if(randomLat < 0) 0.0025+randomLat else 0.0025-randomLat
        return arrayOf(randomLat, randomLng)
    }

    private fun setMarkerBitmap(): Bitmap {
        if(cachedBitmap == null){
            val bitmapDraw = ResourcesCompat.getDrawable(resources, R.drawable.monster_spot, null) as BitmapDrawable
            val b = bitmapDraw.bitmap
            cachedBitmap = Bitmap.createScaledBitmap(b, 100, 100, false)
        }
        return cachedBitmap!!
    }
}