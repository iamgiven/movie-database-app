package com.example.moviedatabase.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedatabase.R
import com.example.moviedatabase.database.MovieEntity
import com.example.moviedatabase.models.Movie
import com.example.moviedatabase.database.MovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieAdapter(
    private val context: Context,
    private val movieList: List<Movie>,
    private val itemClickListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movieDao = MovieDatabase.getDatabase(context).movieDao()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val title: TextView = itemView.findViewById(R.id.title)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.title.text = movie.title
        val imgUri = movie.imgPoster.toUri().buildUpon().scheme("https").build()
        Glide.with(context).load(imgUri).into(holder.imgPoster)

        holder.itemView.setOnClickListener { itemClickListener(movie) }

        coroutineScope.launch {
            val isFavorite = withContext(Dispatchers.IO) {
                movieDao.getMovieById(movie.id).firstOrNull() != null
            }
            holder.favoriteButton.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }

        holder.favoriteButton.setOnClickListener {
            coroutineScope.launch {
                val isFavorite = withContext(Dispatchers.IO) {
                    movieDao.getMovieById(movie.id).firstOrNull() != null
                }
                if (isFavorite) {
                    withContext(Dispatchers.IO) {
                        movieDao.removeMovieFromFavorites(MovieEntity(movie.id, movie.title, movie.description, movie.imgPoster, movie.releaseDate))
                    }
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                } else {
                    withContext(Dispatchers.IO) {
                        movieDao.addMovieToFavorites(MovieEntity(movie.id, movie.title, movie.description, movie.imgPoster, movie.releaseDate))
                    }
                    holder.favoriteButton.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}
