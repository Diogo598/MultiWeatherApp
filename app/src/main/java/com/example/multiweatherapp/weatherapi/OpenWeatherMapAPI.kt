package com.example.multiweatherapp.weatherapi

import com.example.multiweatherapp.HttpRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder


class OpenWeatherMapAPI private constructor(queryString: String): WeatherAPI {
    private val weatherdata: JSONObject

    companion object {

        private const val API_KEY = "148bc012af15fef79a91f8e5375b7ad4"
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?lang=de&APPID=$API_KEY&"
        @Throws(IOException::class, JSONException::class)

        fun fromLocationName(locationName: String?): WeatherAPI {

            return OpenWeatherMapAPI("q=" + URLEncoder.encode(locationName, "UTF-8"))

        }

        @Throws(IOException::class, JSONException::class)

        fun fromLatLon(lat: Double, lon: Double): WeatherAPI {

            return OpenWeatherMapAPI("lat=$lat&lon=$lon")

        }

    }

    @get:Throws(JSONException::class)

    override val temperature: Int

        get() {

            val main = weatherdata.getJSONObject("main")

            val tempKelvin = main.getDouble("temp")

            return (tempKelvin - 273.15).toInt()

        }

    @get:Throws(JSONException::class)

    override val description: String

        get() {

            val weather = weatherdata.getJSONArray("weather")

            return weather.getJSONObject(0).getString("description")

        }

    @get:Throws(JSONException::class)

    override val iconUrl: String

        get() {

            val weather = weatherdata.getJSONArray("weather")

            return "https://openweathermap.org/img/w/${weather.getJSONObject(0).getString("icon")}.png"

        }

    @get:Throws(JSONException::class)

    override val location: String

        get() {

            return weatherdata.getString("name")

        }

    override val providerUrl: String

        get() = "https://www.openweathermap.org"

    init {

        val result = HttpRequest.request(BASE_URL + queryString)

        weatherdata = JSONObject(result)

        println(weatherdata.toString()) // Debug

    }
}