package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var loginPreference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getNameId()
        backActivity()
        addStory()
        setupLanguage()
        switchTheme()
        moveAbout()
        openGallery()

        loadImageFromStorage()
    }

    private fun getNameId() {
        loginPreference = LoginPreference(this)
        val userData = loginPreference.getLogin()
        binding.tvUsername.text = userData.userId
        binding.tvID.text = userData.name

        binding.actionLogout.setOnClickListener {
            loginPreference.clearLogin()
            moveToLoginOrOnBoarding()
            deleteProfileImage()
        }
    }

    private fun openGallery() {
        binding.btnEdit.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                binding.ivAvatar.setImageBitmap(bitmap)
                saveImage(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        val filename = "profile_image.jpg"
        val directory = File(filesDir, "images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, filename)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show()

            saveImageFileName(filename)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageFileName(fileName: String) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imageFileName", fileName)
        editor.apply()
    }

    private fun loadImageFromStorage() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fileName = sharedPreferences.getString("imageFileName", "")

        if (!fileName.isNullOrEmpty()) {
            val directory = File(filesDir, "images")
            val file = File(directory, fileName)

            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding.ivAvatar.setImageBitmap(bitmap)
            } else {
                binding.ivAvatar.setImageResource(R.drawable.avatar)
            }
        }
    }

    private fun deleteProfileImage() {
        val filename = "profile_image.jpg"
        val directory = File(filesDir, "images")

        val file = File(directory, filename)
        if (file.exists()) {
            file.delete()
            Toast.makeText(this, "Profile image deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Profile image not found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun moveToLoginOrOnBoarding() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addStory() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLanguage() {
        binding.btnSwitchLang.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun switchTheme() {
        binding.btnSwitchTheme.setOnClickListener {
            Toast.makeText(this, R.string.coming_soon, Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveAbout() {
        binding.btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PREFS_NAME = "MyPrefs"
    }

}