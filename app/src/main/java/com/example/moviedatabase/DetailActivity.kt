package com.example.moviedatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var imgPoster: ImageView
    private lateinit var title: TextView
    private lateinit var releaseDate: TextView
    private lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        lifecycle.addObserver(this)  // Add observer

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
        Glide.with(this)
            .load(imgUri)
            .into(imgPoster)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyEvent() {
        Glide.with(this).clear(imgPoster)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Glide.with(this).clear(imgPoster) // Not needed as it's handled by the lifecycle observer
    }
}
