package com.example.moviedatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedatabase.R
import com.example.moviedatabase.database.MovieEntity

class SavedMovieAdapter : RecyclerView.Adapter<SavedMovieAdapter.SavedMovieViewHolder>() {

    private var savedMovies: List<MovieEntity> = listOf()

    class SavedMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return SavedMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedMovieViewHolder, position: Int) {
        val movie = savedMovies[position]
        holder.title.text = movie.title
        val imgUri = movie.imgPoster.toUri().buildUpon().scheme("https").build()
        Glide.with(holder.itemView.context).load(imgUri).into(holder.imgPoster)
    }

    override fun getItemCount(): Int {
        return savedMovies.size
    }

    fun setMovies(movies: List<MovieEntity>) {
        this.savedMovies = movies
        notifyDataSetChanged()
    }
}
