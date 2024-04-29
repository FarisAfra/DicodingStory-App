package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.getImageUri
import com.farisafra.dicodingstory.data.reduceFileImage
import com.farisafra.dicodingstory.data.uriToFile
import com.farisafra.dicodingstory.data.viewmodel.AddStoryViewModel
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.farisafra.dicodingstory.databinding.ActivityAddStoryBinding
import com.farisafra.dicodingstory.ui.customview.ResponseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var vmFactory: ViewModelFactory
    private val addStoryViewModel: AddStoryViewModel by viewModels { vmFactory }
    private lateinit var progressBar: ProgressBar
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, R.string.permission, Toast.LENGTH_LONG).show()
                getDeviceLocation()
            } else {
                Toast.makeText(this, R.string.permission, Toast.LENGTH_LONG).show()
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        progressBar = binding.progressBar

        setupViewModel()
        startCamera()
        openGallery()
        goToUpload()
        deleteImage()
        btnBack()

    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun goToUpload() {
        binding.buttonAdd.setOnClickListener {
            val desc = binding.edAddDescription.text.toString().trim()
            when {
                currentImageUri == null && desc.isEmpty() -> {
                    Toast.makeText(this, R.string.pick_image_desc, Toast.LENGTH_SHORT).show()
                }
                desc.isEmpty() -> {
                    Toast.makeText(this, R.string.select_image, Toast.LENGTH_SHORT).show()
                }
                currentImageUri == null -> {
                    Toast.makeText(this, R.string.fill_desc, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (isNetworkAvailable(this)) {
                        if (binding.switchLocation.isChecked) {
                            getDeviceLocation()
                        } else {
                            createStory(desc)
                        }
                    } else {
                        errorResponse()
                    }
                    moveToMain()
                }
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun createStory(description: String) {
        currentImageUri?.let { uri ->
            fun convertImage(): MultipartBody.Part {
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                return MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
            }
            val image = convertImage()
            val desc = convertDescription(description)
            addStoryViewModel.postStory(image, desc, lat, lon).observe(this@AddStoryActivity) { result ->
                if (result != null) {
                    when(result) {
                        is com.farisafra.dicodingstory.data.repository.Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is com.farisafra.dicodingstory.data.repository.Result.Error -> {
                            progressBar.visibility = View.GONE
                            errorResponse()
                        }
                        is com.farisafra.dicodingstory.data.repository.Result.Success -> {
                            successResponse()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun createStoryWithLocation(description: String) {
        currentImageUri?.let { uri ->
            fun convertImage(): MultipartBody.Part {
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                return MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
            }

            val image = convertImage()
            val desc = convertDescription(description)


            addStoryViewModel.postStory(image, desc, lat, lon).observe(this@AddStoryActivity) { result ->
                if (result != null) {
                    when(result) {
                        is com.farisafra.dicodingstory.data.repository.Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is com.farisafra.dicodingstory.data.repository.Result.Error -> {
                            progressBar.visibility = View.GONE
                            errorResponse()
                        }
                        is com.farisafra.dicodingstory.data.repository.Result.Success -> {
                            successResponse()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        lat = location.latitude
                        lon = location.longitude
                        createStoryWithLocation(binding.edAddDescription.text.toString().trim())
                    }
                }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun convertDescription(description: String): RequestBody {
        return description.toRequestBody("text/plain".toMediaType())
    }


    private fun startCamera() {
        binding.btnCamera.setOnClickListener {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivStoryPhoto.setImageURI(it)
        }
    }


    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error, moveToMain()).show()
    }

    private fun successResponse() {
        deleteImage()
        ResponseView(this, R.string.register_message, R.drawable.uploaded, moveToMain()).show()
    }

    private fun moveToMain(): () -> Unit {
        return {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            currentImageUri = uri
            binding.ivStoryPhoto.setImageURI(uri)
            Glide.with(this).load(currentImageUri).into(binding.ivStoryPhoto)
            showDeleteBtn()
        }
    }

    private fun openGallery() {
        binding.btnGallery.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun deleteImage() {
        binding.btnDeleteImage.setOnClickListener {
            currentImageUri = null
            Glide.with(this)
                .load(R.drawable.add_image)
                .into(binding.ivStoryPhoto)
            binding.btnDeleteImage.visibility = View.GONE
        }
    }

    private fun showDeleteBtn() {
        binding.btnDeleteImage.visibility = View.VISIBLE
    }

    private fun btnBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}