package com.example.moviedatabase.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedatabase.R
import com.example.moviedatabase.adapters.SavedMovieAdapter
import com.example.moviedatabase.database.MovieDao
import com.example.moviedatabase.database.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SavedActivity : AppCompatActivity() {

    private lateinit var savedRecyclerView: RecyclerView
    private lateinit var savedMovieAdapter: SavedMovieAdapter
    private lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        savedRecyclerView = findViewById(R.id.savedRecyclerView)
        savedRecyclerView.layoutManager = LinearLayoutManager(this)
        savedMovieAdapter = SavedMovieAdapter()

        movieDao = MovieDatabase.getDatabase(this).movieDao()

        // Retrieve saved movies from Room database
        GlobalScope.launch(Dispatchers.Main) {
            movieDao.getAllFavoriteMovies().collect { savedMovies ->
                savedMovieAdapter.setMovies(savedMovies)
            }
        }

        savedRecyclerView.adapter = savedMovieAdapter
    }
}
