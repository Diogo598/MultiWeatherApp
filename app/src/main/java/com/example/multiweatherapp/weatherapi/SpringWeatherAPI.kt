package com.example.multiweatherapp.weatherapi

import android.util.Log
import com.example.multiweatherapp.HttpRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
class SpringWeatherAPI private constructor(queryString: String): WeatherAPI {
    private val weatherdata: JSONObject

    companion object {

        private var BASE_URL = ""

        fun setServerAddress(address: String?) {
            BASE_URL = "http://$address:8080/weather?"

            Log.d("SpringWeatherAPI", "BASE_URL set to: $BASE_URL")
        }
        @Throws(IOException::class, JSONException::class)
        fun getIconUrl(): String {
            val iconPath = "location=Darmstadt" // Example iconUrl construction
            return "$BASE_URL$iconPath"
        }

        fun fromLocationName(locationName: String?): WeatherAPI {

            return SpringWeatherAPI("location=" + URLEncoder.encode(locationName, "UTF-8"))

        }

        @Throws(IOException::class, JSONException::class)

        fun fromLatLon(lat: Double, lon: Double): WeatherAPI {

            return SpringWeatherAPI("query=$lat,$lon")

        }

    }

    @get:Throws(JSONException::class)

    override val temperature: Int

        get() {

            return weatherdata.getInt("temperature")

        }

    @get:Throws(JSONException::class)

    override val description: String

        get() {

            return weatherdata.getString("description")

        }

    @get:Throws(JSONException::class)

    override val location: String

        get() {

            return weatherdata.getString("locationName")

        }

    @get:Throws(JSONException::class)

    override val iconUrl: String

        get() {

            return weatherdata.getString("iconUrl")

        }

    @get:Throws(JSONException::class)

    override val providerUrl: String

        get() {

            return weatherdata.getString("providerUrl")

        }

    init {

        val result = HttpRequest.request(BASE_URL + queryString)

        weatherdata = JSONObject(result)

        println(weatherdata.toString())

    }
}