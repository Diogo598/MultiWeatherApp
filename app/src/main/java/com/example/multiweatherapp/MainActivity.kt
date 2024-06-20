package com.example.multiweatherapp

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.multiweatherapp.databinding.ActivityMainBinding
import com.example.multiweatherapp.weatherapi.SpringWeatherAPI
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.preference.CheckBoxPreference

class MainActivity : AppCompatActivity() {
    //Variablen festlegen
    private lateinit var locationManager: LocationManager
    private lateinit var gpsUpdate: GpsLocation

    val prefsChangedListener = object: SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
            println("prefs changed, key: $key")
            if (key == "spring_server_adress") {
                val newAddress = prefs?.getString(key, "")
                SpringWeatherAPI.setServerAddress(newAddress)
            }
            if (key == "use_gps") {
                //Wenn die Box gechecked ist, soll die updates funktion ausgeführt werden
                val useGps = prefs?.getBoolean("use_gps", false)
                if (useGps == true) {
                    gpsUpdate.startLocationUpdates()
                }
                if (useGps == false) {
                    gpsUpdate.stopLocationUpdates()
                }
            }
        }
    }

    inner class GpsLocation : LocationListener {
        private var locationManager: LocationManager? = null
        //Wenn die Location geändert wird sollen die aktuellen Koordinaten geloggt werden
        override fun onLocationChanged(location: Location) {
            Log.d("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
        }
            //Location aktualisieren
            fun startLocationUpdates() {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                try {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        this
                    )
                } catch (ex: SecurityException) {
                    ex.printStackTrace()
                }
            }

            //Updates stoppen
            fun stopLocationUpdates() {
                locationManager?.removeUpdates(this)
                Log.d("Location", "Updates stopped")
            }

        }

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            gpsUpdate = GpsLocation()

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            prefs.registerOnSharedPreferenceChangeListener(prefsChangedListener)
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


            val navView: BottomNavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }

}
