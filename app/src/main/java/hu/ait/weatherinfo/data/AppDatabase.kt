package hu.ait.weatherinfo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [City::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): CityDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "database"
                )
                    .fallbackToDestructiveMigration() // when you update a database version you create a brand new data
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}