package hu.ait.weatherinfo.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//
// Host: http://api.openweathermap.org/
// PATH: api/latest
// ?
// Query parameters: access_key=dca1a9a7766cdeb4918dfb177591255b

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getWeatherInfo(@Query("q") city: String,
                          @Query("units") units: String,
                          @Query("appid") appid: String): Call<WeatherResult>
}