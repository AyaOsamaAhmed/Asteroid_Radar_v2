package com.udacity.asteroidradar.db

import androidx.room.*

@Dao
interface NewsDataBaseDao  {
    //picture_of_day
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: PictureOfDayModel)
    @get:Query("SELECT * FROM picture_of_day")
    val getAllPictureOfDay:List<PictureOfDayModel>

    //asteroid
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(asteroidModel: AsteroidModel)
    @get:Query("SELECT * FROM asteroid")
    val getAllAsteroid:List<AsteroidModel>
    @Query("SELECT * FROM asteroid WHERE close_approachDate= :current_date")
    fun getTodayAsteroid(
        current_date:String
    ):List<AsteroidModel>
}