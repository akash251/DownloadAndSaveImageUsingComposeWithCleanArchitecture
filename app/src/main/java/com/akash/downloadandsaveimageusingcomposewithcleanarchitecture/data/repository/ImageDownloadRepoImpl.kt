package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.data.remote.DownloadImageApi
import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.domain.repository.ImageDownloadRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.cert.Extension
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ImageDownloadRepoImpl @Inject constructor(
    private val api: DownloadImageApi,
) : ImageDownloadRepo {
    override suspend fun downloadAndSaveImage(
        imageUrl: String, context: Context,
    ): Flow<String> {
        return flow {
            try {
                val data = api.downloadImage(imageUrl)

                val inputStream = data.body()?.byteStream()
                //input stream will include the file
                if (inputStream != null) {
                    // when stream is not null then we will
                    //pass to save function
                    saveDownloadedImage(context,inputStream)
                    emit("Downloading success")
                } else {
                    emit("no data")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                emit(e.message.toString())

            }

        }
    }


    private fun saveDownloadedImage(context: Context, stream: InputStream) {

        // lets define the name for downloaded image which must be unique

        val fileName = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.ENGLISH
        ).format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            //setting name of file
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            // mime type we are downloading image for mime type of image
            // if you want to save other type of file then use mime type for that
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // saving to downloads directory
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }
        val resolver = context.contentResolver


        // Android 10 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = resolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )
            if (uri != null){
                try {
                    stream.use { input ->
                        resolver.openOutputStream(uri).use { output->
                            input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                        }

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

        // Below Android 10

        if (Build.VERSION.SDK_INT <Build.VERSION_CODES.Q){

            val target = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                ),
                "$fileName.jpeg"
            )
            // here we added extension by our self you can add
            // different types of extension based file you want to save
            try {
                FileOutputStream(target).use { output->
                    stream.copyTo(output)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }


    }


}