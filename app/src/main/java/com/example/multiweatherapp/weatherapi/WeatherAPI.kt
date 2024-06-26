package com.example.multiweatherapp.weatherapi
import org.json.JSONException

interface WeatherAPI {
    @get:Throws(JSONException::class)
    val temperature: Int
    @get:Throws(JSONException::class)
    val description: String
    @get:Throws(JSONException::class)
    val iconUrl: String
    @get:Throws(JSONException::class)
    val location: String
    val providerUrl: String
}