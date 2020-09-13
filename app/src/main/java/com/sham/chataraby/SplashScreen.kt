package com.sham.chataraby

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
/*
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

 */


class SplashScreen : AppCompatActivity() {
    private lateinit var imgLogo : ImageView
    var hasAnimationStarted:Boolean=false
    private val ReqPer = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //FirebaseApp.initializeApp(this)
        imgLogo = findViewById(R.id.mSplash)
        //Handler().postDelayed({checkPermission()},1300)
        // OneSignal Initialization
       // createNotificationChannel();
       // getFirebaseData()

    }

    val NOTIFICATION_CHANNEL_ID = "download_channel_id"

    /*
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "NotificationName",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

    }

     */

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !hasAnimationStarted) {
            hasAnimationStarted = true
            val metrics = resources.displayMetrics
            val translationY = ObjectAnimator.ofFloat(
                imgLogo,
                "y",
                metrics.heightPixels / 2 - imgLogo.height.toFloat() / 2
            ) // metrics.heightPixels or root.getHeight()
            translationY.duration = 1000
            translationY.start()
            translationY.doOnEnd {
                if(isNetworkAvailable()){
                    //getFirebaseData()
                    checkPermission()
                }
                else{
                    noNetwork()
                }
            }
        }

    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun noNetwork(){
        var alert = AlertDialog.Builder(this)
        alert.setMessage("فشل الإتصال بالأنترنت الرجاء إعادة المحاولة")
        alert.setPositiveButton("إعادة المحاولة") { dialog, which ->
            //Close APP
           // finish()
            recreate()
            //Toast.makeText(mCtx,"CLOSE",Toast.LENGTH_LONG).show()
        }
        alert.setNegativeButton("إغلاق") { dialog, which ->
            System.exit(0)
        }

        alert.create().show()
    }



    fun checkPermission(){
        if (Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,android.Manifest.permission.RECORD_AUDIO),ReqPer)
                return
            }
        }
        nextAct()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            ReqPer->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    nextAct()
                }
                else{
                    Toast.makeText(this,"الرجاء إعطاء الصلاحيات الازمة للتطبيق",Toast.LENGTH_SHORT).show()
                }
            }
            else->{super.onRequestPermissionsResult(requestCode, permissions, grantResults) }
        }
    }

    fun nextAct(){
        startActivity(Intent(this,NavigationActivity::class.java))
        finish()
    }
/*
    fun getFirebaseData(){
        val mRef = FirebaseDatabase.getInstance().reference
        mRef.child("Ads").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    val adsData = dataSnapshot.getValue(adsModel::class.java)
                    val mUtils = Utils(this@SplashScreen).saveAdsData(adsData!!)
                    Log.e("LOGCHARAF",adsData.admobBanner!!)
                    Log.e("LOGCHARAF",adsData.admobFull!!)
                    Log.e("LOGCHARAF",adsData.admobID!!)
                    Log.e("LOGCHARAF",adsData.adsEnable!!)
                    Log.e("LOGCHARAF",adsData.adsType!!)
                    Log.e("LOGCHARAF",adsData.fanBannerAdsPlacementId!!)
                    Log.e("LOGCHARAF",adsData.fanInterstitialAdsPlacementId!!)

                    if (adsData.adsType.equals("block")){
                        BlockTheAPP()
                    }else{
                        checkPermission()
                    }
                }
                else{
                //TODO TEST NETWORK
                }

            }

        })
    }


 */
    fun BlockTheAPP(){
        /*
        var alert = AlertDialog.Builder(this)
        alert.setMessage(getString(R.string.block_app))
        alert.setCancelable(false)
        alert.setPositiveButton("خروج") { dialog, which ->
            //Close APP
           System.exit(0)
            //Toast.makeText(mCtx,"CLOSE",Toast.LENGTH_LONG).show()

        }


        alert.create().show()

         */
    }



}
