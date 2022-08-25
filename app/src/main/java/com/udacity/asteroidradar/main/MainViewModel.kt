package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.MainServicesImpl
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidModel
import com.udacity.asteroidradar.db.PictureOfDayModel
import com.udacity.asteroidradar.utils.instanceApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    var requestDataLiveData = MutableLiveData<Any>()
    var requestFeedDataLiveData = MutableLiveData<ArrayList<AsteroidModel>>()

    init {


    }


    fun mainData(){
        viewModelScope.launch(Dispatchers.IO ) {

          var response = MainServicesImpl.getDataMain(API_KEY)
            insertPicOfDayIntoDB(
                context = instanceApp,
                pictureOfDay = PictureOfDayModel(
                    mediaType = response.mediaType?:"",
                    title = response.title,
                    url = response.url
                )
            )
            delay(1000L)
            getPicOfDayFromDB()

        }
    }
    private fun insertPicOfDayIntoDB(context: Context, pictureOfDay: PictureOfDayModel) {
        viewModelScope.launch(Dispatchers.IO) {
            MainServicesImpl.insertPictureOfDayDB(context = context, pictureOfDay = pictureOfDay)
        }
    }

    fun getPicOfDayFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            requestDataLiveData.postValue(MainServicesImpl.getPictureOfDayDB(instanceApp))

        }
    }

    fun feedData( start_date: String, end_date: String){
        viewModelScope.launch(Dispatchers.IO ) {

                //MainServicesImpl.getDataFeed(start_date,end_date,API_KEY)
    val list =  parseAsteroidsJsonResult(JSONObject(MainServicesImpl.getDataFeed(start_date,end_date, API_KEY)))
            for (asteroid in list) {
                insertAsteroidIntoDB( asteroid = AsteroidModel(
                    id = asteroid.id,
                    absoluteMagnitude = asteroid.absoluteMagnitude,
                    closeApproachDate = asteroid.closeApproachDate,
                    codename = asteroid.codename,
                    distanceFromEarth = asteroid.distanceFromEarth,
                    estimatedDiameter = asteroid.estimatedDiameter,
                    isPotentiallyHazardous = asteroid.isPotentiallyHazardous,
                    relativeVelocity = asteroid.relativeVelocity
                )
                )


            }

            delay(1000L)
            getAllAsteroidFromDB()
        }
    }

    fun getPlanetaryApodImageFromAPI(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            var response = MainServicesImpl.getDataMain(API_KEY)
            insertPicOfDayIntoDB(
                context = context,
                pictureOfDay = PictureOfDayModel(
                    mediaType = response.mediaType?:"",
                    title = response.title,
                    url = response.url
                )
            )
            delay(1000L)
            getPicOfDayFromDB()
        }
    }


    private fun insertAsteroidIntoDB( asteroid: AsteroidModel) {
        viewModelScope.launch(Dispatchers.IO) {
            MainServicesImpl.insertAsteroidDB(instanceApp, asteroidModel = asteroid)
    }
}

fun getAllAsteroidFromDB(){
    viewModelScope.launch (Dispatchers.IO){
        val list =  MainServicesImpl.getAsteroidDB(instanceApp)
        requestFeedDataLiveData.postValue(
            list as ArrayList<AsteroidModel>
        )
    }
}
fun getTodayAsteroidFromDB(today : String){
    viewModelScope.launch (Dispatchers.IO){
        val list =  MainServicesImpl.getTodayAsteroidDB(context = instanceApp, currentDate = today)
        requestFeedDataLiveData.postValue(
            list as ArrayList<AsteroidModel>
        )
    }
}



}