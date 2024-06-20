package com.example.multiweatherapp
import com.example.multiweatherapp.weatherapi.OpenWeatherMapAPI
import com.example.multiweatherapp.weatherapi.SpringWeatherAPI
import com.example.multiweatherapp.weatherapi.WeatherStackAPI
import org.json.JSONException
import org.junit.Assert.assertEquals
import java.io.IOException
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test

    @Throws(IOException::class, JSONException::class)

    fun openWeatherMap_getResponseFromName() {

        val api = OpenWeatherMapAPI.fromLocationName("San Francisco")

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }

    @Test

    @Throws(IOException::class, JSONException::class)

    fun openWeatherMap_getResponseFromLatLon() {

        val api = OpenWeatherMapAPI.fromLatLon(37.77, -122.42)

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }

    @Test

    @Throws(IOException::class, JSONException::class)

    fun weatherStack_getResponseFromName() {

        val api = WeatherStackAPI.fromLocationName("San Francisco")

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }

    @Test

    @Throws(IOException::class, JSONException::class)

    fun weatherStack_getResponseFromLatLon() {

        val api = WeatherStackAPI.fromLatLon(37.77, -122.42)

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }

    @Test

    @Throws(IOException::class, JSONException::class)

    fun springWeatherAPI_getResponseFromName() {

        val api = SpringWeatherAPI.fromLocationName("San Francisco")

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }

    @Test

    @Throws(IOException::class, JSONException::class)

    fun springWeatherAPI_getResponseFromLatLon() {

        val api = SpringWeatherAPI.fromLatLon(37.77, -122.42)

        println("Temp: ${api.temperature}")

        println("Description: ${api.description}")

        println("Icon: ${api.iconUrl}")

        println("Location: ${api.location}")

    }
}