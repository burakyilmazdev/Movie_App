package com.example.movie_app.ui.favoriteMovies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.movie_app.R
import com.example.movie_app.data.models.Result
import com.example.movie_app.databinding.FragmentFavoriteBinding
import com.example.movie_app.databinding.FragmentMovieDetailBinding
import com.example.movie_app.utility.SwipeGesture
import com.example.movie_app.viewModel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding:FragmentFavoriteBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private val favoriteAdapter=FavoriteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieViewModel.favoriteMovies.observe(this, Observer {
            favoriteAdapter.setMovieList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        //DELETE
        val swipeGesture = object : SwipeGesture(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)

                val deletedMovie = favoriteAdapter.movieList[viewHolder.adapterPosition]
                movieViewModel.deleteMovie(deletedMovie)
                Toast.makeText(context,"${deletedMovie.title} Removed!",Toast.LENGTH_SHORT).show()
                binding.rvFavorites.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }

        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvFavorites)


        //SEARCH
        binding.search.clearFocus()
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    Log.d("TEST5","text empty")
                    movieViewModel.favoriteMovies.observe(viewLifecycleOwner, Observer {
                        favoriteAdapter.setMovieList(it)
                    })
                }else{
                    Log.d("TEST5","$query text not empty")
                    val searchedQuery = "%$query%"
                    movieViewModel.searchMovie(searchedQuery).observe(viewLifecycleOwner, Observer {
                        favoriteAdapter.setMovieList(it)
                    })
                }
                return true
            }

        })



        binding.rvFavorites.adapter = favoriteAdapter

        // Inflate the layout for this fragment
        return binding.root
    }

}