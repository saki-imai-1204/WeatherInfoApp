package hu.ait.weatherinfo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true) var _cityId: Long?,
    @ColumnInfo(name = "name") var name: String
)