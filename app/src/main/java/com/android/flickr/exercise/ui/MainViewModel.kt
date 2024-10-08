package com.android.flickr.exercise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.flickr.exercise.data.FlickrPhotoItem
import com.android.flickr.exercise.data.FlickrPhotoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: FlickrPhotoRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.Empty)
    val uiState: StateFlow<State> = _uiState

    fun fetchFlickrPhotoData(searchQuery: String) {
        if (searchQuery.isEmpty()) return
        _uiState.value = State.Loading
        viewModelScope.launch {
            _uiState.value = repo.getFlickrPhotoList(searchQuery)
        }
    }

    sealed class State {
        object Empty : State()
        object Loading : State()
        object Result {
            data class Success(val data: List<FlickrPhotoItem>) : State()
            data class Error(val error: String) : State()
        }
    }
}