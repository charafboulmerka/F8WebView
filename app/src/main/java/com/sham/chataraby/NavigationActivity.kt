package com.sham.chataraby

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.net.http.SslError
import android.os.*
import android.util.Base64
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
//import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
/*
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
 */

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    LifecycleObserver {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        chance50ToShowAds.shuffle()
        when (item.itemId) {

            R.id.nav_home -> {
                url=getString(R.string.web_url)
            }

            R.id.nav_refresh -> {
                url=myWebView.url.toString()
                setmURL()
              //  url=getString(R.string.web_url)
            }

            R.id.nav_privacy-> {
                url="https://www.shamtalk.net/privacy.php"
            }



            R.id.nav_rate-> {
                url="rate"

                startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID )))
            }
            R.id.nav_close-> {
                url="close"
                System.exit(0)
            }
            R.id.nav_share-> {
                try {
                    url = "share"
                    var shareIntent =  Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    var shareMessage= "\nأريد ان اشارك معك هذا التطبيق الرائع حمله الآن من الرابط التالي\n\n"
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "إختر واحدة"))
                } catch( e:Exception) {
                    //e.toString();
                }
            }

        }
        drawer.closeDrawer(GravityCompat.START)
        if (url!="share" && url!="rate" && url!="close")
        setmURL()
        //FOR FACEBOOK
        /*
        if (chance50ToShowAds[0]){
           showFullAd()
        }

         */
        return true
    }

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var url = ""
    //lateinit var mAdView : AdView
    var isPickFileAlreadyOpened = false
    private  var mProgress: ProgressBar?=null
    var typeImage = "*/*"
    private var mWebviewPop: WebView?=null
    // private val target_url_prefix = ""
    private lateinit var mContainer: FrameLayout
    private lateinit var prBar: ProgressBar
    private  lateinit var myWebView: WebView
    private var mUploadMessage: ValueCallback<Uri>? = null
    private val mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val mCameraPhotoPath: String? = null

    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    private val ReqPer = 2
    var  progressDialog: ProgressDialog?=null
    lateinit var adView_fan:LinearLayout
    lateinit var adView_admob:LinearLayout
    var chance50ToShowAds = arrayListOf<Boolean>(true,false)
    var chance30ToShowAds = arrayListOf<Boolean>(true,false,false,false)
    var alertNoConnection = false
    private lateinit var mUtils:Utils
   // private lateinit var mInterstitialAd: com.google.android.gms.ads.InterstitialAd
   var DESKTOP_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 6.0.1; en-US; SM-J500F Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.9.7.1153 Mobile Safari/537.36"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)
       // adView_fan=findViewById(R.id.adView_fan)
       // adView_admob=findViewById(R.id.adView_admob)



                try {
            var info = packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES)
                    for ( signature: Signature in info.signatures) {
                var md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e:PackageManager.NameNotFoundException ) {
            e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
                }

        ProcessLifecycleOwner.get().lifecycle.addObserver(object :LifecycleObserver{

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onMoveToForeground() { // app moved to foreground
                Utils(this@NavigationActivity).showAdmobFull()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onMoveToBackground() { // app moved to background
            }
        })




/*
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()


 */
        //mUtils=Utils(this)
        /*
        if (mUtils.getAdsData().adsType.equals("block")){
            BlockTheAPP()
        }

         */

        navView.setNavigationItemSelectedListener(this)
        /*
        intAds()
        mInterstitialAd = com.google.android.gms.ads.InterstitialAd(this@NavigationActivity)
        mInterstitialAd.adUnitId = mUtils.getAdsData().admobFull


         */
        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        fab.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            }
            else{
                drawer.openDrawer(GravityCompat.START)
            }
        }



        if (url==""){
            url = getString(R.string.web_url)

        }
        var ex = intent.extras
        if (ex !=null){
            url = ex.getString("LINK",getString(R.string.web_url))

        }


        myWebView=findViewById(R.id.mWebView)
        //PROGRESS BAR
        prBar = findViewById(R.id.mProgressBar)
        prBar.progressDrawable.setColorFilter(resources.getColor(R.color.mBleu),android.graphics.PorterDuff.Mode.SRC_IN)
        mContainer =  findViewById(R.id.webview_frame)
        // mAdView = findViewById(R.id.adView)
        //val adRequest = AdRequest.Builder().build()
        // mAdView.loadAd(adRequest)

        setmURL()

    }


    fun BlockTheAPP(){
        var alert = AlertDialog.Builder(this)
        alert.setMessage(getString(R.string.block_app))
        alert.setCancelable(false)
        alert.setPositiveButton("خروج") { dialog, which ->
            //Close APP
            System.exit(0)
            //Toast.makeText(mCtx,"CLOSE",Toast.LENGTH_LONG).show()
        }


        alert.create().show()
    }




/*
    fun showFullAd(){
        if (Utils(this).getAdsData().adsEnable.equals("true")){
            if (Utils(this).getAdsData().adsType.equals("admob")){
                Utils(this).showAdmobFull()
            }else{
                Utils(this).showFanFull()
            }
        }
    }

    fun showBannerAd(){
        if (Utils(this).getAdsData().adsEnable.equals("true")){
            if (Utils(this).getAdsData().adsType.equals("admob")){
                Utils(this).showAdmobBanner(adView_admob)
            }else{
                Utils(this).showFanBanner(adView_fan)
            }
        }
    }

    fun intAds(){
        var mUt_data = Utils(this).getAdsData()
        if (mUt_data.adsType.equals("admob")){
            MobileAds.initialize(this, mUt_data.admobID)
            Log.e("CHARAF1013","ADMOB UES")
            showBannerAd()
        }else{
            FacebookSdk.sdkInitialize(this);
            AudienceNetworkAds.initialize(this)
            showBannerAd()

        }
    }



 */
    override fun onResume() {
        super.onResume()
        try{
            myWebView.onResume()
            mWebviewPop!!.onResume()
            if (url.equals("")){
                setmURL()
            }
        }catch (e:Exception){}





    }



    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (mWebView.canGoBack()){
                mWebView.goBack()
            }
            else{
                WantToClose()
            }
            //super.onBackPressed()
        }
        /*
                if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (mWebView.canGoBack()){
                mWebView.goBack()
                chance30ToShowAds.shuffle()
                if (chance30ToShowAds[0]){
                    showFullAd()
                }
            }
            else{
                WantToClose()
            }
            //super.onBackPressed()
        }
         */
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
       // menuInflater.inflate(R.menu.navigation, menu)
        return false
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged( newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item!!)
    }




    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetJavaScriptEnabled")
    fun setmURL(){
        //Toast.makeText(this@NavigationActivity,"SET URL:$url",Toast.LENGTH_LONG).show()

        if (!isOnline())
        // WantToRefresh()
        //Progress Bar
            mProgress =  findViewById(R.id.mProgressBar)

        //Cookie manager for the webview
        var cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        //Get outer container
        mContainer =  findViewById(R.id.webview_frame)
        val webSettings = myWebView.settings
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.javaScriptEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings!!.loadWithOverviewMode = true
        webSettings.allowContentAccess = true
        webSettings.loadWithOverviewMode = true
        webSettings.loadsImagesAutomatically = true
        webSettings.databaseEnabled = true
        webSettings.setAppCacheMaxSize( 10 * 1024 * 1024 ) // 10MB
        webSettings.setAppCachePath( applicationContext.cacheDir.absolutePath)
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled( true )
        webSettings.useWideViewPort = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        //webSettings.userAgentString = DESKTOP_USER_AGENT
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webSettings.allowFileAccess = true

        //webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.pluginState = WebSettings.PluginState.ON

        webSettings.textZoom = 80 // where 90 is 90%; default value is ... 100
        myWebView.clearCache(false)
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // load online by default

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            // older android version, disable hardware acceleration
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }



        if ( !isOnline() ) { // loading offline
            if(!alertNoConnection){
                AlertToCheckNet()
            }
            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }


        myWebView.webViewClient = MyCustomWebViewClient()
        myWebView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

        myWebView.webChromeClient = MyCustomChromeClient()





        myWebView.loadUrl(url)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //FOR FACEBOOK
        //CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);
        isPickFileAlreadyOpened = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    if (resultCode != Activity.RESULT_OK) {
                        result = null
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                    /*
                    Toast.makeText(
                        applicationContext, "activity :$e",
                        Toast.LENGTH_LONG
                    ).show()

                     */
                }

                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }

    private inner class MyCustomWebViewClient : WebViewClient() {


        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val host = Uri.parse(url).host
            //Log.d("shouldOverrideUrlLoading", url);
            /*
            if (host == target_url_prefix || host=="accounts.hsoub.com") {
                // This is my web site, so do not override; let my WebView load
                // the page
               // view.loadUrl(url)
                if (mWebviewPop != null) {
                    mWebviewPop!!.visibility = View.GONE
                    mContainer.removeView(mWebviewPop)
                    mWebviewPop = null
                }
                return false
            }

            chance30ToShowAds.shuffle()
            if (chance30ToShowAds[0]){
                showFullAd()
            }


  */

            //facebook.com/sharer/sharer.php
            //change this for diffrent url
            /*
            if (url.contains("facebook.com/BgahaCom")) {
          var facebookIntent =  Intent(Intent.ACTION_VIEW);
                var facebookUrl = getFacebookPageURL(this@NavigationActivity);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent)
                view.loadUrl(getString(R.string.web_url))
                myWebView.loadUrl(getString(R.string.web_url))
                return false
                // return false
            }


            if (url.contains("twitter.com/bgahacom")  ) {
                var intent:Intent? = null;
                var profile_id="bgahacom"
                try {
                    // get the Twitter app if possible
                  getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent =  Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$profile_id"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (e:Exception) {
                    // no Twitter app, revert to browser
                    intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$profile_id"));
                }
                startActivity(intent);
                view.loadUrl(getString(R.string.web_url))
                myWebView.loadUrl(getString(R.string.web_url))
                return false
                // return false
            }

                if (url.contains("instagram.com/bgahacom") ) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                view.loadUrl(getString(R.string.web_url))
                myWebView.loadUrl(getString(R.string.web_url))
                return false
                // return false
            }


             */


            if (url.contains("whatsapp://send") ) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                this@NavigationActivity.url=getString(R.string.web_url)
                view.loadUrl(getString(R.string.web_url))
                myWebView.loadUrl(getString(R.string.web_url))
                return true
                // return false
            }


            if ( url.contains("facebook.com/sharer/sharer")) {
                if(isPackageInstalled("com.facebook.katana",this@NavigationActivity.packageManager)){
                    val share= Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    share.putExtra(Intent.EXTRA_TEXT, url.split("?u=")[1])
                    share.setPackage("com.facebook.katana") //Facebook App package
                    startActivity(Intent.createChooser(share, ""))
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))


                }else{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
                if (mWebviewPop != null) {
                    mWebviewPop!!.visibility = View.GONE
                    mContainer.removeView(mWebviewPop)
                    mWebviewPop = null
                }
                return true

            }

            if ( url.contains("twitter.com/intent/tweet") || url.contains("https://twitter.com/share")) {
                if(isPackageInstalled("com.twitter.android",this@NavigationActivity.packageManager)){
                    var share= Intent(Intent.ACTION_SEND)
                    share.type = "text/plain"
                    // var user_url="https://"+url.split("bgaha.com")[1]
                   // var user_name =url.split("bgaha.com/sendto?u=")[1]
                    //share.putExtra(Intent.EXTRA_TEXT, "يا أصدقائي دا رابط حسابي علي موقع #بجاحة! "+user_name+"\nعليه شوية اسئله, اختاروا اي سؤال فيهم و جاوبوه و طبعا بسرية\n"+user_url);
                    share.putExtra(Intent.EXTRA_TEXT, url.split("?text=")[1])
                    share.setPackage("com.twitter.android") //Facebook App package
                    startActivity(Intent.createChooser(share, ""))
                }else{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
                if (mWebviewPop != null) {
                    mWebviewPop!!.visibility = View.GONE
                    mContainer.removeView(mWebviewPop)
                    mWebviewPop = null
                }
                return true

                // return false
            }

            if (url.contains("admin.php")) {

                if (mWebviewPop != null) {
                    mWebviewPop!!.visibility = View.GONE
                    mContainer.removeView(mWebviewPop)
                    mWebviewPop = null
                }
                myWebView.loadUrl(url)
                return false
            }

            if (host == "m.facebook.com" || host == "www.facebook.com") {
                return false
            }
            else{
                if (mWebviewPop != null) {

                    mWebviewPop!!.visibility = View.GONE
                    mContainer.removeView(mWebviewPop)
                    mWebviewPop = null
                }



                return false
            }


            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            // val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            // mAct.startActivity(intent)
            // return true
        }

        /*
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (progressDialog == null) {
                progressDialog =  ProgressDialog(this@MainActivity);
                progressDialog!!.setMessage("Loading...");
                progressDialog!!.show();
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            try {
                // Close progressDialog
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss();
                    progressDialog = null;
                }
            } catch ( exception:Exception) {
                exception.printStackTrace();
            }
        }




        override fun onReceivedSslError(
            view: WebView, handler: SslErrorHandler,
            error: SslError
        ) {
            Log.d("onReceivedSslError", "onReceivedSslError")
            //super.onReceivedSslError(view, handler, error);
        }
*/
    }

    fun isPackageInstalled(packageName:String, packageManager: PackageManager):Boolean {
    try {
        packageManager.getPackageInfo(packageName, 0)
        return true
    } catch ( e:PackageManager.NameNotFoundException) {
        return false
    }
}

    override fun onPause() {
        super.onPause()
        try{
            myWebView.onPause()
            mWebviewPop!!.onPause()
        }catch (e:Exception){}
    }


 fun findTwitterClient(): Intent? {
    var  twitterApps = arrayListOf<String> (
            // package // name - nb installs (thousands)
            "com.facebook.katana", // official - 10 000
            "com.twidroid", // twidroid - 5 000
            "com.android.chrome", // Tweecaster - 5 000
            "com.thedeck.android" ) // TweetDeck - 5 000 };
    var tweetIntent =  Intent()

     tweetIntent.type = "text/plain"
     var  packageManager = packageManager as PackageManager
     var list = packageManager.queryIntentActivities(
            tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)

     for ( i in 0..twitterApps.size) {
        for ( resolveInfo:ResolveInfo in list) {
            var p = resolveInfo.activityInfo.packageName
            if (p != null && p.startsWith(twitterApps[i])) {
                tweetIntent.setPackage(p)
                return tweetIntent
            }
        }
    }

    return null
 }

private var FACEBOOK_URL="https://www.facebook.com/BgahaCom"
    private var FACEBOOK_PAGE_ID="100018732761426"

    fun getFacebookPageURL(context:Context):String {
        var packageManager = context.packageManager
        try {
            var versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode

            var activated =  packageManager.getApplicationInfo("com.facebook.katana", 0).enabled
            if(activated){
                if ((versionCode >= 3002850)) {
                    Log.d("main","fb first url")
                    return "fb://facewebmodal/f?href=" + FACEBOOK_URL
                } else {
                    return "fb://page/" + FACEBOOK_PAGE_ID
                }
            }else{
                return FACEBOOK_URL
            }
        } catch (e:PackageManager.NameNotFoundException ) {
            return FACEBOOK_URL
        }
    }




    private inner class MyCustomChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage!!.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                myWebView.onResume()
            }
            return super.onConsoleMessage(consoleMessage)        }
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onPermissionRequest(request: PermissionRequest?) {
            request!!.grant(request.resources)

        }
        protected fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
            mUploadMessage = uploadMsg
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = typeImage
            if (!isPickFileAlreadyOpened){
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
                isPickFileAlreadyOpened=true
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onShowFileChooser(
            mWebView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: WebChromeClient.FileChooserParams
        ): Boolean {
            if (mUploadMessage != null) {
                mUploadMessage!!.onReceiveValue(null)
                mUploadMessage = null
            }

            mFilePathCallback = filePathCallback

            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = typeImage
            if (!isPickFileAlreadyOpened){
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
                isPickFileAlreadyOpened=true
            }
            try {
                if (!isPickFileAlreadyOpened){
                    startActivityForResult(i, INPUT_FILE_REQUEST_CODE)
                    isPickFileAlreadyOpened=true
                }
            } catch (e: Exception) {
                mUploadMessage = null
                Log.e("ERROR",e.message.toString())
                return false
            }

            return true
        }

        protected fun openFileChooser(
            uploadMsg: ValueCallback<Array<Uri>>,
            acceptType: String,
            capture: String
        ) {
            mFilePathCallback = uploadMsg
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = typeImage
            Toast.makeText(this@NavigationActivity,"o3",Toast.LENGTH_LONG).show()
            if (!isPickFileAlreadyOpened){
                startActivityForResult(
                    Intent.createChooser(intent, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
                isPickFileAlreadyOpened=true
            }

        }

        protected fun openFileChooser(uploadMsg: ValueCallback<Array<Uri>>) {
            mFilePathCallback = uploadMsg
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = typeImage
            Toast.makeText(this@NavigationActivity,"o4",Toast.LENGTH_LONG).show()
            if (!isPickFileAlreadyOpened){
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
                isPickFileAlreadyOpened=true
            }
        }

        override fun onCreateWindow(
            view: WebView, isDialog: Boolean,
            isUserGesture: Boolean, resultMsg: Message
        ): Boolean {
            mWebviewPop = WebView(this@NavigationActivity)
            mWebviewPop!!.isVerticalScrollBarEnabled = false
            mWebviewPop!!.isHorizontalScrollBarEnabled = false
            mWebviewPop!!.webViewClient = MyCustomWebViewClient()
            mWebviewPop!!.settings.javaScriptEnabled = true

            mWebviewPop!!.settings.savePassword = false
            mWebviewPop!!.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mContainer.addView(mWebviewPop)
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = mWebviewPop
            resultMsg.sendToTarget()

            return true
        }
        override fun onProgressChanged(view: WebView, progress: Int) {

            if (progress < 100 && prBar.visibility === ProgressBar.GONE) {
                prBar.visibility = ProgressBar.VISIBLE
            }

            prBar.progress = progress
            if (progress == 100) {
                prBar.visibility = ProgressBar.GONE
            }


        }

        override fun onCloseWindow(window: WebView) {
            Log.d("onCloseWindow", "called")
        }

    }


    fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }



    fun WantToClose(){
        var alert = AlertDialog.Builder(this)
        alert.setMessage("هل ترغب في إغلاق التطبيق؟")
        alert.setPositiveButton("نعم") { dialog, which ->
            //Close APP
            finish()
            //Toast.makeText(mCtx,"CLOSE",Toast.LENGTH_LONG).show()
        }
        alert.setNegativeButton("لا") { dialog, which ->

        }

        alert.create().show()
    }

    fun WantToRefresh(){
        val alert = AlertDialog.Builder(this)
        alert.setCancelable(false)
        alert.setMessage("الرجاء تفقد إتصالك بالأنترنت و إعادة المحاولة")
        alert.setPositiveButton("إعادة المحاولة") { dialog, which ->
            setmURL()
        }


        alert.create().show()
    }

    fun AlertToCheckNet(){
        val alert = AlertDialog.Builder(this)
        alert.setCancelable(false)
        alert.setMessage("الرجاء تفقد إتصالك بالأنترنت ")
        alert.setPositiveButton("حسنا") { dialog, which ->
            //setmURL()
            alertNoConnection=true
        }


        alert.create().show()
    }
}
