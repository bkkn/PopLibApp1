package me.bkkn.myfirstmaterialapp.domain

import me.bkkn.myfirstmaterialapp.api.PictureOfTheDayResponse

interface NasaRepository {
    suspend fun pictureOfTheDay(): PictureOfTheDayResponse

    suspend fun pictureByDate(data: String): PictureOfTheDayResponse
}