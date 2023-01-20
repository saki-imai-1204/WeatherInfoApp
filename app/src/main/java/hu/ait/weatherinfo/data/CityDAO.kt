package hu.ait.weatherinfo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDAO {

    @Query("SELECT * FROM city")
    fun getAllItems() : LiveData<List<City>>

    @Query("SELECT * FROM city WHERE name LIKE '%' || :text || '%'")
    fun findItems(text: String) : LiveData<List<City>>

    @Insert
    fun addItem(city: City)

    @Delete
    fun deleteItem(city: City)

    @Query("DELETE FROM city")
    fun deleteAllItems()

    @Update
    fun updateItem(city: City)

}