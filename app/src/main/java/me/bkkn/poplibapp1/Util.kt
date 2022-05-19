package me.bkkn.poplibapp1

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Util {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateString(delta: Long): String {
        val zone = ZoneId.of("America/New_York")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd",)
        val tomorrow = LocalDate.now(zone).minusDays(delta)
        return tomorrow.format(dateFormatter)
    }
}