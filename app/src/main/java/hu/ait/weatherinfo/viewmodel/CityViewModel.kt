package hu.ait.weatherinfo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hu.ait.weatherinfo.data.AppDatabase
import hu.ait.weatherinfo.data.City
import hu.ait.weatherinfo.data.CityDAO

class CityViewModel(application: Application) :
    AndroidViewModel(application) {

    val allItems: LiveData<List<City>>

    private var cityDAO: CityDAO

    init {
        cityDAO = AppDatabase.getInstance(application).
        itemDao()
        allItems = cityDAO.getAllItems()
    }

    fun insertItem(city: City)  {
        Thread {
            cityDAO.addItem(city)
        }.start()
    }

    fun deleteItem(city: City)  {
        Thread {
            cityDAO.deleteItem(city)
        }.start()
    }

    fun deleteAllItems(){
        Thread {
            cityDAO.deleteAllItems()
        }.start()
    }
}