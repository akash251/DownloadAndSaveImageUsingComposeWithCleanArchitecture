package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.di

import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.data.remote.DownloadImageApi
import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.data.repository.ImageDownloadRepoImpl
import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.domain.repository.ImageDownloadRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    //lets create a client it's not necessary
    // but it helps to log the api response
    // so that it will be easier to resolve issue

    @Provides
    @ViewModelScoped
    fun provideOkHttpClient():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
    }


    //for image download api
    @Provides
    @ViewModelScoped
    fun provideDownloadImageApi(client: OkHttpClient):DownloadImageApi{
        return Retrofit.Builder()
            .baseUrl("https://fakeurl.com/")
            .client(client)
            .build()
            .create(DownloadImageApi::class.java)
    }
    // lets add dependency for repository also

    @Provides
    @ViewModelScoped
    fun provideImageDownloadRepository(api: DownloadImageApi):ImageDownloadRepo{
        return ImageDownloadRepoImpl(api)
    }






}