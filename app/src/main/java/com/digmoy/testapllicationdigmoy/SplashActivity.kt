package com.digmoy.testapllicationdigmoy

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.digmoy.testapllicationdigmoy.Adapter.ViewPagerAdapter
import com.digmoy.testapllicationdigmoy.databinding.ActivitySplashBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException
import java.text.DateFormat
import java.util.*

class SplashActivity : AppCompatActivity(),View.OnClickListener {

    private val binding : ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private var dotscount = 0
    private lateinit var dots: Array<ImageView?>

    private val TAG: String = SplashActivity::class.java.getSimpleName()
    private var mLastUpdateTime: String? = null

    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    private val REQUEST_CHECK_SETTINGS = 100

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null
    var status = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        checkAppPermission()
        checkPermission()
        restoreValuesFromBundle(savedInstanceState)
        status = true

        binding.tvSkip.setOnClickListener(this)

        var viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewpager.adapter = viewPagerAdapter

        dotscount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotscount)

        for (i in 0 until dotscount) {
            dots[i] = ImageView(this)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.default_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.SliderDots.addView(dots[i], params)
        }

        dots[0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.selected_dot))

        binding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotscount) {
                    dots[i]!!.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.default_dot
                        )
                    )
                }
                dots[position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.selected_dot
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_skip ->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkAppPermission() {
        Log.e("thtyuytuiylkjluyk", "jhgjkhgjghjhgjuikyu")
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_WIFI_STATE)
                            != PackageManager.PERMISSION_GRANTED)) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) || ((
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED))) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_WIFI_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // go_next();
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA),
                    REQUEST_CHECK_SETTINGS)
            }
        }
    }


    private fun init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    private fun restoreValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates")
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location")
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on")
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            val lat = mCurrentLocation!!.latitude
            val lng = mCurrentLocation!!.longitude
            latitude = lat.toString()
            longitude = lng.toString()
            Log.e("latitude", "" + latitude)
            Log.e("Longitude", "" + longitude)
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName
                val countryCode = addresses[0].countryCode


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates!!)
        outState.putParcelable("last_known_location", mCurrentLocation)
        outState.putString("last_updated_on", mLastUpdateTime)
    }

    private fun checkPermission() {
        Dexter.withActivity(this@SplashActivity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mRequestingLocationUpdates = true
                    startLocationUpdates()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        // open device settings when the permission is
                        // denied permanently
                        openSettings()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package",
            BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun startLocationUpdates() {
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(this) {
                Log.i(TAG, "All location settings are satisfied.")

                // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                updateLocationUI()
            }
            .addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                "location settings ")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@SplashActivity, REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(TAG, "PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        Toast.makeText(this@SplashActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
                updateLocationUI()
            }
    }

    fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener(this) {
                // Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> Log.e(TAG, "User agreed to make required location settings changes.")
                RESULT_CANCELED -> {
                    Log.e(TAG, "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates!! && checkPermissions()) {
            startLocationUpdates()
        }
        updateLocationUI()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }


    override fun onPause() {
        super.onPause()
        if (mRequestingLocationUpdates!!) {
            // pausing location updates
            stopLocationUpdates()
        }
    }

    companion object{
        var latitude = ""
        var longitude = ""
    }
}