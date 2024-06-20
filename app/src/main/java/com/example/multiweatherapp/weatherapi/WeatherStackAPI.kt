package com.example.multiweatherapp.weatherapi

import com.example.multiweatherapp.HttpRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

class WeatherStackAPI private constructor(queryString: String): WeatherAPI{
    private val weatherdata: JSONObject

    companion object {
        //Creditcardscam
        private const val API_KEY = "148bc012af15fef79a91f8e5375b7ad4"
        private const val BASE_URL = "http://api.weatherstack.com/current?access_key=$API_KEY&"
        @Throws(IOException::class, JSONException::class)

        fun fromLocationName(locationName: String?): WeatherAPI {

            return WeatherStackAPI("query=" + URLEncoder.encode(locationName, "UTF-8"))
        }
        @Throws(IOException::class, JSONException::class)
        fun fromLatLon(lat: Double, lon: Double): WeatherAPI {

            return WeatherStackAPI("query=$lat,$lon")
        }

    }
    @get:Throws(JSONException::class)

    override val temperature: Int

        get() {

            val current = weatherdata.getJSONObject("current")

            return current.getInt("temperature")

        }

    @get:Throws(JSONException::class)

    override val description: String

        get() {

            val descriptions = weatherdata.getJSONObject("current").getJSONArray("weather_descriptions")

            return descriptions[0] as String

        }

    @get:Throws(JSONException::class)

    override val iconUrl: String

        get() {

            val icons = weatherdata.getJSONObject("current").getJSONArray("weather_icons")

            return icons[0] as String

        }

    @get:Throws(JSONException::class)

    override val location: String

        get() {

            return weatherdata.getJSONObject("location").getString("name")

        }

    override val providerUrl: String

        get() = "https://weatherstack.com/"

    init {

        val result = HttpRequest.request(BASE_URL + queryString)

        weatherdata = JSONObject(result)

        println(weatherdata.toString())

    }

}