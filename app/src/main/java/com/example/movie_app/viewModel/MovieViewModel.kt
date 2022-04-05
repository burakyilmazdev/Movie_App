package com.example.movie_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.movie_app.data.models.MovieResponse
import com.example.movie_app.data.models.Resource
import com.example.movie_app.data.models.Status
import com.example.movie_app.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val MovieRepository:MovieRepository
): ViewModel(){

    val movieList = MutableLiveData<Resource<Response<MovieResponse>>>()

    init {
        getMovies()
    }

    fun getMovies(){
        viewModelScope.launch(Dispatchers.Main) {
          MovieRepository.getMovies().collect {
            movieList.value = it
          }
        }

    }

}