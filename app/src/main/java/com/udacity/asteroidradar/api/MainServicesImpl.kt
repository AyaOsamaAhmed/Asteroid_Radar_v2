package com.udacity.asteroidradar.api

import android.content.Context
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.db.AsteroidModel
import com.udacity.asteroidradar.db.PictureOfDayModel
import com.udacity.asteroidradar.utils.App

object MainServicesImpl : Apis {

    private val httpClient by lazy {
        ApiClient.instanceApi
    }


    override suspend fun getDataMain(api_key: String): PictureOfDay {
        return httpClient.getDataMain(api_key)
    }

    override suspend fun getDataFeed(
        start_date: String,
        end_date: String,
        api_key: String
    ): String {
        return httpClient.getDataFeed(start_date,end_date,api_key)
    }

    suspend fun insertPictureOfDayDB(context: Context, pictureOfDay: PictureOfDayModel){
        App.dbApp.newsDataBaseDao.insertPictureOfDay(pictureOfDay)
    }
    suspend fun getPictureOfDayDB(context: Context): PictureOfDayModel? {
        var list = App.dbApp.newsDataBaseDao.getAllPictureOfDay
        if(list.isEmpty()){
            return null
        }
        return list.last()
    }
    suspend fun insertAsteroidDB(context: Context, asteroidModel: AsteroidModel){
        App.dbApp.newsDataBaseDao.insertAsteroid(asteroidModel)
    }
    suspend fun getAsteroidDB(context: Context):List<AsteroidModel>{
        return App.dbApp.newsDataBaseDao.getAllAsteroid
    }
    suspend fun getTodayAsteroidDB(context: Context, currentDate:String):List<AsteroidModel>{
        return App.dbApp.newsDataBaseDao.getTodayAsteroid(currentDate)
    }
}