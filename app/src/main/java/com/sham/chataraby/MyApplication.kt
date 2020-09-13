package com.sham.chataraby

import android.app.Application


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        /*
        context = applicationContext
        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
        //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(MyNotificationOpenedHandler(this))
            //.setNotificationReceivedHandler(MyNotificationReceivedHandler())
            .init()

         */
    }
/*
    companion object {

        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }


 */

}