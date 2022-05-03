package me.bkkn.myfirstmaterialapp.domain

import me.bkkn.myfirstmaterialapp.api.NasaApi
import me.bkkn.myfirstmaterialapp.api.PictureOfTheDayResponse
import me.bkkn.poplibapp1.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaRepositoryImpl : NasaRepository {

    private val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.nasa.gov/")
        .client(
            OkHttpClient.Builder().apply
            {
                addInterceptor(HttpLoggingInterceptor().apply
                {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }.build()
        )
        .build()
        .create(NasaApi::class.java)

    override suspend fun pictureOfTheDay(): PictureOfTheDayResponse =
        api.pictureOfTheDay(BuildConfig.NASA_API_KEY)

    override suspend fun pictureByDate(date: String): PictureOfTheDayResponse =
        api.pictureByDate(BuildConfig.NASA_API_KEY,date)
}
