package com.udacity.asteroidradar.utils

import android.app.Activity
import android.app.Application
import com.udacity.asteroidradar.db.NewsDataBase


val instanceApp: App by lazy {
    App.instance
}

val dbApp: NewsDataBase by lazy {
    App.dbApp
}
    class App : Application() {

        private var mCurrentActivity: Activity? = null


        companion object {
             lateinit var instance: App
            lateinit var dbApp: NewsDataBase

            fun getAppInstance(): App {
                return instance
            }
        }

        override fun onCreate() {
            super.onCreate()
            instance = this
             dbApp = NewsDataBase.getInstance(applicationContext)
        }

        fun getCurrentActivity(): Activity? {
            return mCurrentActivity
        }

        fun setCurrentActivity(mCurrentActivity: Activity?) {
            this.mCurrentActivity = mCurrentActivity
        }


 }