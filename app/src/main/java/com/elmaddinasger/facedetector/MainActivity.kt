package com.elmaddinasger.facedetector

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elmaddinasger.facedetector.databinding.ActivityMainBinding
import com.elmaddinasger.facedetector.dedection.CameraManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraManager = CameraManager(
            this,
            binding.viewCameraPreview,
            binding.viewGraphicOverlay,
            this
        )
        askCameraPermission()
        buttonClicks()
    }

    private fun buttonClicks() {
        binding.buttonTurnCamera.setOnClickListener {
            cameraManager.changeCamera()
        }
        binding.buttonStopCamera.setOnClickListener {
            cameraManager.cameraStop()
            buttonVisibility(false)
        }
        binding.buttonStartCamera.setOnClickListener {
            cameraManager.cameraStart()
            buttonVisibility(true)
        }
    }

    private fun buttonVisibility(forStart : Boolean) {
        if (forStart) {
            binding.buttonStopCamera.visibility = View.VISIBLE
            binding.buttonStartCamera.visibility = View.INVISIBLE
        } else {
            binding.buttonStopCamera.visibility = View.INVISIBLE
            binding.buttonStartCamera.visibility = View.VISIBLE
        }
    }

    private fun askCameraPermission() {
        if (arrayOf(android.Manifest.permission.CAMERA).all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            cameraManager.cameraStart()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraManager.cameraStart()
        } else {
            Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
}