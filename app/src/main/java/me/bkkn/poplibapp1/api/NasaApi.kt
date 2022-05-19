package me.bkkn.myfirstmaterialapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("planetary/apod")
    suspend fun pictureOfTheDay(@Query("api_key") key: String): PictureOfTheDayResponse

    @GET("planetary/apod")
    suspend fun pictureByDate(@Query("api_key") key: String,
                              @Query("date") date: String,): PictureOfTheDayResponse
}