package com.example.moviedatabase.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedatabase.R
import com.example.moviedatabase.adapters.SavedMovieAdapter
import com.example.moviedatabase.database.MovieDao
import com.example.moviedatabase.database.MovieDatabase
import com.example.moviedatabase.database.MovieEntity
import com.example.moviedatabase.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedActivity : AppCompatActivity() {

    private lateinit var savedRecyclerView: RecyclerView
    private lateinit var savedMovieAdapter: SavedMovieAdapter
    private lateinit var movieDao: MovieDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        // Atur judul activity
        title = getString(R.string.saved_movies_title)

        savedRecyclerView = findViewById(R.id.savedRecyclerView)
        savedRecyclerView.layoutManager = GridLayoutManager(this, 3)
        savedMovieAdapter = SavedMovieAdapter { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("movie", movie.toMovie()) // Konversi MovieEntity menjadi Movie
            startActivity(intent)
        }
        savedRecyclerView.adapter = savedMovieAdapter

        movieDao = MovieDatabase.getDatabase(this).movieDao()

        // Retrieve saved movies from Room database
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                movieDao.getAllFavoriteMovies().collect { savedMovies ->
                    savedMovieAdapter.setMovies(savedMovies)
                }
            }
        }
    }

    // Fungsi ekstensi untuk konversi MovieEntity menjadi Movie
    private fun MovieEntity.toMovie(): Movie {
        return Movie(id, title, description, imgPoster, releaseDate)
    }
}
