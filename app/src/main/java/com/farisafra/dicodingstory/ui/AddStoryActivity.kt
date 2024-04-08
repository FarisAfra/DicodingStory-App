package com.farisafra.dicodingstory.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.getTempUriFromFile
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.reduceFileImage
import com.farisafra.dicodingstory.data.response.story.StoryResponse
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import com.farisafra.dicodingstory.data.uriToFile
import com.farisafra.dicodingstory.databinding.ActivityAddStoryBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        openCamera()
        openGallery()
        goToUpload()
    }

    private fun goToUpload() {
        binding.BtnAddStory.setOnClickListener {
            val desc = binding.edAddDescription.text.toString().trim()
            if (currentImageUri == null && currentImageUri.toString().isEmpty() && desc.isEmpty()) {
                showToast("Silakan pilih gambar dan isi deskripsi terlebih dahulu")
            } else if (desc.isEmpty()) {
                showToast("Silakan isi deskripsi terlebih dahulu")
            } else if (currentImageUri == null && currentImageUri.toString().isEmpty()) {
                showToast("Silakan pilih gambar terlebih dahulu")
            } else {
                uploadImage()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString().trim()
            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val loginPreference = LoginPreference(this@AddStoryActivity)
                    val token = loginPreference.getToken() ?: ""
                    val apiService = ApiClient.getApiService(token)
                    val successResponse = apiService.addStory(multipartBody, requestBody)
                    showToast("Telah Berhasil Upload Story")
                    showLoading(false)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
                    showToast(errorResponse.message)
                    showLoading(false)
                }
            }

        } ?: showToast("Gambar Kosong")
    }

    private fun openCamera() {
        binding.btnCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery() {
        binding.btnGallery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.ivStoryPhoto.setImageBitmap(imageBitmap)
                    val tempUri = getTempUriFromFile(this, imageBitmap)
                    currentImageUri = tempUri
                }
                REQUEST_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        currentImageUri = uri
                        binding.ivStoryPhoto.setImageURI(uri)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_PICK_IMAGE = 2
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}