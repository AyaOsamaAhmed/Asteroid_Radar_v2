package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidModel::class , PictureOfDayModel::class], version = 2,  exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {

    abstract val newsDataBaseDao : NewsDataBaseDao

    companion object {

        @Volatile
        private var INSTANCE: NewsDataBase? = null

        fun getInstance(context: Context): NewsDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDataBase::class.java,
                        "detected_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

}
}