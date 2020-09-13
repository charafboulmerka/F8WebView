package com.sham.chataraby
/*

 */
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
//import com.facebook.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class Utils {

    private lateinit var mCtx:Context
    private lateinit var mShared:SharedPreferences
    private lateinit var mInterstitialAd:com.google.android.gms.ads.InterstitialAd
    private var chanceToShowAd= arrayListOf<Boolean>(true,false)
    constructor(mCtx:Context){
        this.mCtx=mCtx
       // mShared=mCtx.getSharedPreferences("mData",Context.MODE_PRIVATE)
        MobileAds.initialize(mCtx) {}

    }

    fun showAdmobFull(){
        //chanceToShowAd.shuffle()
        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(mCtx)
        mInterstitialAd.adUnitId = mCtx.getString(R.string.admobFull)

        mInterstitialAd.loadAd(AdRequest.Builder().build())
        //mInterstitialAd.show()
        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdClosed() {
               // mInterstitialAd.loadAd(AdRequest.Builder().build())
            }

            override fun onAdLoaded() {
                Log.e("HAHAHA","LOADED")
                mInterstitialAd.show()
            }

        }


    }
    /*

   fun saveAdsData(ads: adsModel){
      var editShared = mShared.edit()
       editShared.putString("adsEnable",ads.adsEnable)
       editShared.putString("adsType",ads.adsType)
       editShared.putString("admobID",ads.admobID)
       editShared.putString("admobBanner",ads.admobBanner)
       editShared.putString("admobFull",ads.admobFull)
       editShared.putString("fanBanner",ads.fanBannerAdsPlacementId)
       editShared.putString("fanFull",ads.fanInterstitialAdsPlacementId)
       editShared.commit()
   }

   fun getAdsData():adsModel{
       val data = adsModel()
       data.adsEnable=mShared.getString("adsEnable","true")
       data.adsType=mShared.getString("adsType","admob")
       data.admobID=mShared.getString("admobID","")
       data.admobBanner=mShared.getString("admobBanner","")
       data.admobFull=mShared.getString("admobFull","")
       data.fanBannerAdsPlacementId=mShared.getString("fanBanner","")
       data.fanInterstitialAdsPlacementId=mShared.getString("fanFull","")
       return data
   }

   fun isNetworkAvailable(): Boolean {
       val connectivityManager =
           mCtx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       val activeNetworkInfo = connectivityManager.activeNetworkInfo
       return activeNetworkInfo != null && activeNetworkInfo.isConnected
   }


   fun showFanBanner(adContainer:LinearLayout){
       adContainer.visibility=View.VISIBLE
       val adView = AdView(mCtx, getAdsData().fanBannerAdsPlacementId, AdSize.BANNER_HEIGHT_50)

       // Add the ad view to your activity layout
       adContainer.addView(adView)

       // Request an ad
       adView.loadAd()
   }

   fun showAdmobBanner(adView_admob:LinearLayout){
       adView_admob.visibility=View.VISIBLE

       val mAdView = AdView(mCtx)
       mAdView.adSize = com.google.android.gms.ads.AdSize.BANNER
       mAdView.adUnitId = getAdsData().admobBanner
       val builder = AdRequest.Builder()
       mAdView.loadAd(builder.build())
       adView_admob.addView(mAdView)
   }





   fun showFanFull(){
       chanceToShowAd.shuffle()
       val interstitialAd =  InterstitialAd(mCtx, getAdsData().fanInterstitialAdsPlacementId)
       interstitialAd.loadAd();

       interstitialAd.setAdListener(object : InterstitialAdListener {
           override fun onInterstitialDisplayed(p0: Ad?) {
           }

           override fun onAdClicked(p0: Ad?) {
           }

           override fun onInterstitialDismissed(p0: Ad?) {
           }

           override fun onError(p0: Ad?, p1: AdError?) {
           }

           override fun onLoggingImpression(p0: Ad?) {
           }

           override fun onAdLoaded(p0: Ad?) {
               interstitialAd.show()
           }

       });
       // load the ad
   }

    */
}