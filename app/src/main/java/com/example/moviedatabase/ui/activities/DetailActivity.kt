package com.example.moviedatabase.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.moviedatabase.R
import com.example.moviedatabase.models.Movie

class DetailActivity : AppCompatActivity() {

    private lateinit var imgPoster: ImageView
    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgPoster = findViewById(R.id.imgPoster)
        title = findViewById(R.id.title)
        releaseDate = findViewById(R.id.releaseDate)
        description = findViewById(R.id.description)

        val movie: Movie? = intent.getParcelableExtra("movie")
        if (movie != null) {
            bindMovieData(movie)
        } else {
            Toast.makeText(this, "Movie data is missing", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun bindMovieData(movie: Movie) {
        title.text = movie.title
        releaseDate.text = movie.releaseDate
        description.text = movie.description

        val imgUri = movie.imgPoster.toUri().buildUpon().scheme("https").build()
        Glide.with(this).load(imgUri).into(imgPoster)
    }
}
