package com.dezzapps.mapkotlin_pokemoon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermison()
        loadPokemon()
    }

    var ACCESS_LOCATION = 123
    fun checkPermison(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
                return
            }
        }
        getUserLocation()
    }

    fun getUserLocation(){



        Toast.makeText(this, "User has the permiss", Toast.LENGTH_SHORT).show()

        var myLocation = MyLocationListener()

        var myPosition = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        myPosition.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        var myThread = myThread()
        myThread.start()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESS_LOCATION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }else {
                    Toast.makeText(this, "We cannot access to your location", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        // Add a marker in Sydney and move the camera
          }

    //Get user location

    var location: Location? = null

    inner class MyLocationListener: LocationListener {


        constructor(){
            location = Location("start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0

        }

        override fun onLocationChanged(p0: Location?) {

            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {


        }

        override fun onProviderEnabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation: Location? = null
    inner  class myThread:Thread {

        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run() {

            while(true){

                try {
                    if(oldLocation!!.distanceTo(location)== 0f){
                        continue
                    }
                    oldLocation= location
                    runOnUiThread{
                        mMap!!.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me").snippet("Here")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        // Show Pokemon

                        for(i in 0..listPokemon.size-1){
                            var newPokemon=listPokemon[i]

                            if(newPokemon.isCatch == false){

                                val pokemon = LatLng(newPokemon.location!!.latitude, newPokemon.location!!.longitude)
                                mMap!!.addMarker(MarkerOptions()
                                    .position(pokemon)
                                    .title(newPokemon.name)
                                    .snippet(newPokemon.desc + " - Power: " + newPokemon.power)
                                    .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pokemon, 14f))


                                if(location!!.distanceTo(newPokemon.location) < 2){
                                    newPokemon.isCatch = true
                                    listPokemon[i] = newPokemon
                                    playerPower += newPokemon.power!!

                                    Toast.makeText(applicationContext, "You catch a pokemon and you new power is $playerPower", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                    }

                    Thread.sleep(1000)
                }catch (ex: Exception){

                }

            }
        }
    }

    var listPokemon = ArrayList<Pokemon>()
    var playerPower = 0.0

    fun loadPokemon(){

        listPokemon.add(Pokemon("Charmander", "Fuego", R.drawable.charmander, 50, 37.7789994893035, -122.401846647263))
        listPokemon.add(Pokemon("squirtle", "Acuatico", R.drawable.squirtle, 150, 37.7949568502667, -122.410494089127))
        listPokemon.add(Pokemon("bulbasaur", "Bosques", R.drawable.bulbasaur, 90, 37.7816621152613, -122.41225361824))

    }
}
