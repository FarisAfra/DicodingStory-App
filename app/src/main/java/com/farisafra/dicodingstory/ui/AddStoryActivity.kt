package com.farisafra.dicodingstory.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.getTempUriFromFile
import com.farisafra.dicodingstory.data.reduceFileImage
import com.farisafra.dicodingstory.data.uriToFile
import com.farisafra.dicodingstory.data.viewmodel.AddStoryViewModel
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.farisafra.dicodingstory.databinding.ActivityAddStoryBinding
import com.farisafra.dicodingstory.ui.customview.ResponseView
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

        progressBar = binding.progressBar

        setupViewModel()
        openCamera()
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
            if (currentImageUri == null && desc.isEmpty()) {
                showToast("Silakan pilih gambar dan isi deskripsi terlebih dahulu")
            } else if (desc.isEmpty()) {
                showToast("Silakan isi deskripsi terlebih dahulu")
            } else if (currentImageUri == null ) {
                showToast("Silakan pilih gambar terlebih dahulu")
            } else {
                createStory(desc)
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
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
            addStoryViewModel.postStory(image, desc).observe(this@AddStoryActivity) { result ->
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


    private fun convertDescription(description: String): RequestBody {
        return description.toRequestBody("text/plain".toMediaType())
    }

    private fun openCamera() {
        binding.btnCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error).show()
    }

    private fun successResponse() {
        binding.edAddDescription.text?.clear()
        deleteImage()
        ResponseView(this, R.string.register_message, R.drawable.registered).show()
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
                    Glide.with(this).load(tempUri).into(binding.ivStoryPhoto)
                    showDeleteBtn()
                }
                REQUEST_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        currentImageUri = uri
                        binding.ivStoryPhoto.setImageURI(uri)
                        Glide.with(this).load(currentImageUri).into(binding.ivStoryPhoto)
                        showDeleteBtn()
                    }
                }
            }
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

    private fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_PICK_IMAGE = 2
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }
}