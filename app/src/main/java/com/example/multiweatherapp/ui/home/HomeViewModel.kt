package com.example.multiweatherapp.ui.home

import android.Manifest
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.multiweatherapp.R
import com.example.multiweatherapp.weatherapi.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.multiweatherapp.weatherapi.OpenWeatherMapAPI

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _location: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _temperature: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _description: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _provider: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _iconBitmap: MutableLiveData<Bitmap> by lazy {
        MutableLiveData<Bitmap>()
    }
    val location: LiveData<String> = _location
    val temperature: LiveData<String> = _temperature
    val description: LiveData<String> = _description
    val provider: LiveData<String> = _provider
    val iconBitmap: LiveData<Bitmap> = _iconBitmap

    fun retrieveWeatherData() {
        CoroutineScope(Dispatchers.Main).launch() {

            var weather : WeatherAPI? = null
            var bitmap : Bitmap? = null
            val app= getApplication() as Application
            val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())
            val locationName= prefs.getString("location_name", "Darmstadt")
            val providerClassName = prefs.getString(app.getString(R.string.weather_provider), "OpenWeatherMapAPI")
            //Wert festlegen
            val useGps = prefs.getBoolean("use_gps", false)

            withContext(Dispatchers.IO) {
                try {
                    if(useGps&& hasLocationPermission()){
                    //Wenn Gps aktiv ist und die LocationPermission erteilt wurde sollen Longitude und Latitude verwendet werden
                        val locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        val latitude = lastKnownLocation?.latitude
                        val longitude = lastKnownLocation?.longitude


                    weather=OpenWeatherMapAPI.fromLatLon(latitude!!,longitude!!)
                }
                //Sonst wird wie gewohnt der Name verwendet
                else {
                        val cls =
                            Class.forName("com.example.multiweatherapp.weatherapi.$providerClassName").kotlin
                        val func =
                            cls.companionObject?.declaredFunctions?.find { it.name == "fromLocationName" }

                        weather =
                            func?.call(cls.companionObjectInstance, locationName) as? WeatherAPI
                    }

                    Log.d(javaClass.simpleName, "Temp: ${weather?.temperature}")
                    Log.d(javaClass.simpleName, "Description:${weather?.description}")
                    Log.d(javaClass.simpleName, "Icon-URL: ${weather?.iconUrl}")
                    Log.d(javaClass.simpleName, "Provider:${weather?.providerUrl}")

                    val iconUrl = URL(weather?.iconUrl) // java.net!
                    val inputStream = iconUrl.openStream()
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    //Diese Catch ist erforderlich da sonst die Location nicht vewendet werden kann
                } catch (ex: SecurityException) {
                    Log.e(javaClass.simpleName, "SecurityException: ${ex.message}")
                } catch (ex: Exception) {
                    Log.e(javaClass.simpleName, ex.cause.toString())
                }
            }
            updateValues(weather, bitmap)
        }
    }
    //Diese Funktion ist seit API level 23 erforderlich um Zugriff auf die Location zu haben
    //Dies ist erforderlich obwohl man die Permissions bei der Manifest deklariert hat
    private fun hasLocationPermission(): Boolean {
        val context = getApplication<Application>()

        val fineLocationPermission = ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val coarseLocationPermission = ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission || coarseLocationPermission
    }
    fun updateValues(weather: WeatherAPI?, bitmap: Bitmap?) {
        _location.value = weather?.location
        _temperature.value = "${weather?.temperature} °C"
        _description.value = weather?.description
        _provider.value = weather?.providerUrl
        _iconBitmap.value = bitmap
    }
}
