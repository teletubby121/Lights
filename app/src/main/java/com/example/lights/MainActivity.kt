package com.example.lights

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {


    private val CAMERA_PERMISSION = 200
    var flashLightStatus: Boolean = false
    //var btAction: Button? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();   // hides bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        /////////////////////////////////////////
        /////////////////////////////////////////

        MobileAds.initialize(this) {}
        lateinit var mAdView : AdView
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        /////////////////////////////////////////
        ////////////////////////////////////////

        val btAction = findViewById<ImageButton>(R.id.switchl)

        btAction!!.setOnClickListener({
            val permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (permissions != PackageManager.PERMISSION_GRANTED)
                    setupPermissions()
                else {
                    openFlashLight()
                }
            } else {
                openFlashLight()
            }
        })
    }


    private fun setupPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isEmpty() || !grantResults[0].equals(PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                } else {
                    openFlashLight()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun openFlashLight() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false
            } catch (e: CameraAccessException) {
            }
        }

    }
}

