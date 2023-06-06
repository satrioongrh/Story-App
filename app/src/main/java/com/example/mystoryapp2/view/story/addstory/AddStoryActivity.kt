package com.example.mystoryapp2.view.story.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp2.data.viewmodel.StoryViewModel
import com.example.mystoryapp2.databinding.ActivityAddStoryBinding
import com.example.mystoryapp2.view.story.Home.HomeActivity
import com.example.mystoryapp2.view.utils.SessionPref
import com.example.mystoryapp2.view.utils.rotateFile
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var pref: SessionPref
    private var temptoken = ""
    private var getFile: File? = null


    companion object {
        const val CAMERA_X_RESULT = 200

        const val EXTRA_PHOTO_RESULT = "PHOTO_RESULT"
        const val EXTRA_CAMERA_MODE = "CAMERA_MODE"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyViewModel = ViewModelProvider(this).get(StoryViewModel::class.java)
        pref = SessionPref(this)
        temptoken = "bearer ${pref.getToken}"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnUploadCamera.setOnClickListener { startCameraX() }
        binding.buttonAdd.setOnClickListener {
            if (getFile != null && binding.edAddDescription.text.isNotEmpty()) {
                uploadImage(getFile!!, binding.edAddDescription.text.toString())
            } else {
                if (getFile == null) {
                    Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
        storyViewModel.let { vm ->
            vm.isSuccesUpStory.observe(this) {
                Toast.makeText(this, "uploaded succesfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
            vm.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.storyImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun uploadImage(image: File, description: String) {
        if (temptoken != null) {
            storyViewModel?.addStory(temptoken!!, image, description)
        } else {
            Toast.makeText(this, "invalid token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}