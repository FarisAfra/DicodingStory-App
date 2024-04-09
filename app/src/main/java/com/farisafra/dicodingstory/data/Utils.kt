package com.farisafra.dicodingstory.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


private const val MAXIMAL_SIZE = 1000000


fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = File(context.cacheDir, "${System.currentTimeMillis()}_temp_image.jpg") // Membuat file sementara di direktori cache

    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
    inputStream?.use { input ->
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(4 * 1024) // Buffer ukuran 4 KB
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
        outputStream.flush()
    }

    return myFile
}
fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun getTempUriFromFile(context: Context, image: Bitmap): Uri {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}_temp_image.jpg")
    val outputStream = FileOutputStream(file)
    image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return Uri.fromFile(file)
}

