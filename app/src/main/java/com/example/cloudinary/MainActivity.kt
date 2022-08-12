package com.example.cloudinary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.cloudinary.databinding.ActivityMainBinding
import com.example.cloudinary.utils.ImageExtension

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var imageUri: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        binding.image.setOnClickListener{
            camera()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun camera() {
        if (ImageExtension.checkPermissionGivenOrNot(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            openCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
            Log.d("camera","Camera Permission denied")
        }
    }
    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLaunch.launch(camera)
    }
    private var resultLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val bmp: Bitmap = data?.extras?.get("data") as Bitmap
                val a = ImageExtension.getImageUri(
                    bmp,
                    Bitmap.CompressFormat.JPEG,
                    25,
                    contentResolver!!
                )
                imageUri = a!!
                binding.image.setImageBitmap(bmp)
                // addImageToFirebase()
            }
        }

}