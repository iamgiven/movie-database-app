package com.example.moviedatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieList: MutableList<Movie>
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("movies")
        movieRecyclerView = findViewById(R.id.movieRecyclerView)
        movieRecyclerView.layoutManager = LinearLayoutManager(this)
        movieList = mutableListOf()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    movieList.clear()
                    for (movieSnapshot in snapshot.children) {
                        val movie = movieSnapshot.getValue(Movie::class.java)
                        if (movie != null) {
                            movieList.add(movie)
                        }
                    }
                    movieAdapter = MovieAdapter(this@MainActivity, movieList)
                    movieRecyclerView.adapter = movieAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
