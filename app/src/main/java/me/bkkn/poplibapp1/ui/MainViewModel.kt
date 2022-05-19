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
import me.bkkn.myfirstmaterialapp.api.PictureOfTheDayResponse
import me.bkkn.myfirstmaterialapp.domain.NasaRepository
import me.bkkn.poplibapp1.Const.network_error
import me.bkkn.poplibapp1.Util
import java.io.IOException

class MainViewModel(private val repository: NasaRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _response: MutableSharedFlow<PictureOfTheDayResponse?> = MutableSharedFlow()
    val response: Flow<PictureOfTheDayResponse?> = _response

    private val _error: MutableSharedFlow<String> = MutableSharedFlow()
    val error: Flow<String> = _error

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestPictureByDate(delta: Long) {
        _loading.value = true
        viewModelScope.launch {
            try {
                _response.emit(repository.pictureByDate(Util.getDateString(delta)))
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