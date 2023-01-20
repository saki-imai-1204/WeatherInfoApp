package hu.ait.weatherinfo

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.ait.weatherinfo.databinding.ActivityDetailsBinding
import hu.ait.weatherinfo.network.WeatherAPI
import hu.ait.weatherinfo.network.WeatherResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            //The key argument here must match that used in the other activity
            binding.tvCityName.text = value
        }

        var retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var weatherAPI = retrofit.create(WeatherAPI::class.java)
        val call = weatherAPI.getWeatherInfo(binding.tvCityName.text.toString(),
            "metric",
            "dca1a9a7766cdeb4918dfb177591255b"
        )
        call.enqueue(object : Callback<WeatherResult> {
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {


                binding.tvWeatherInfo.text =
                    getString(R.string.weather_details,
                        response.body()!!.main!!.temp?.toFloat(),
                        response.body()!!.main!!.temp_max?.toFloat(),
                        response.body()!!.main!!.temp_min?.toFloat()
                        )

                Glide.with(this@DetailsActivity)
                    .load(
                        ("https://openweathermap.org/img/w/" +
                                response.body()?.weather?.get(0)?.icon
                                + ".png"))
                    .into(binding.ivWeather)
            }
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                binding.tvWeatherInfo.text = getString(R.string.error, t.message)
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnReturn.setOnClickListener {
            val intentDetails = Intent()
            intentDetails.setClass(
                this, ScrollingActivity::class.java
            )
            startActivity(intentDetails)
        }
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

        var location = binding.tvCityName.text.toString()
        var addressList: List<Address>? = null
        val geoCoder = Geocoder(this)
        try {
            addressList = geoCoder.getFromLocationName(location, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        val address = addressList!![0]
        val latLng = LatLng(address.latitude, address.longitude)
        mMap.addMarker(MarkerOptions().position(latLng).title(location))
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}