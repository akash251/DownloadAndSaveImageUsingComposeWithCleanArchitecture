package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.domain.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ImageDownloadRepo {

    suspend fun downloadAndSaveImage(
        imageUrl:String,context: Context
    ):Flow<String>
}