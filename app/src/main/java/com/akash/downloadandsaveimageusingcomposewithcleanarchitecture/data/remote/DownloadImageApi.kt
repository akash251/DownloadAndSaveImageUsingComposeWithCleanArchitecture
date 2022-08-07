package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadImageApi {

    @Streaming
    @GET
    suspend fun downloadImage(@Url imageUrl:String):Response<ResponseBody>
}