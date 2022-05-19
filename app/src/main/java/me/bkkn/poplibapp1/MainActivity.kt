package me.bkkn.poplibapp1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesKey = "shared preferences key"
    private val themeKey = "theme key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getCurrentTheme())
        setContentView(R.layout.main_activity)
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, MODE_PRIVATE)
        return sharedPreferences.getInt(themeKey, -1)
    }

    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, MODE_PRIVATE)
        sharedPreferences.edit().putInt(themeKey, currentTheme).apply()
        recreate()
    }
}