package me.bkkn.poplibapp1.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.bkkn.myfirstmaterialapp.domain.NasaRepository
import me.bkkn.poplibapp1.Const.network_error
import me.bkkn.poplibapp1.Util
import java.io.IOException

class MainViewModel(private val repository: NasaRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _image: MutableStateFlow<String?> = MutableStateFlow(null)
    val image: Flow<String?> = _image

    private val _title: MutableStateFlow<String?> = MutableStateFlow(null)
    val title: Flow<String?> = _title

    private val _explanation: MutableStateFlow<String?> = MutableStateFlow(null)
    val explanation: Flow<String?> = _explanation

    private val _error: MutableSharedFlow<String> = MutableSharedFlow()
    val error: Flow<String> = _error

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestPictureOfTheDay() {

        _loading.value = true

        viewModelScope.launch {
            try {
                val url = repository.pictureOfTheDay().url
                _image.emit(url)
                val title = repository.pictureOfTheDay().title
                _title.emit(title)
                val expl = repository.pictureOfTheDay().explanation
                _explanation.emit(expl)
            } catch (e: IOException) {
                _error.emit(network_error)
            }

            _loading.emit(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestPictureByDate(delta: Long) {

        _loading.value = true

        viewModelScope.launch {
            try {
                val date = Util.getDateString(delta)
                val url = repository.pictureByDate(date).url
                _image.emit(url)
                val title = repository.pictureByDate(date).title
                _title.emit(title)
                val explanation = repository.pictureByDate(date).explanation
                _explanation.emit(explanation)
            } catch (e: IOException) {
                _error.emit(network_error)
            }

            _loading.emit(false)
        }
    }
}

class MainViewModelFactory(private val repository: NasaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(repository) as T
}