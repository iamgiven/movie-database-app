package com.example.moviedatabase.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedatabase.R
import com.example.moviedatabase.adapters.MovieAdapter
import com.example.moviedatabase.models.Movie
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieList: MutableList<Movie>
    private lateinit var filteredList: MutableList<Movie>
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("movies")
        movieRecyclerView = findViewById(R.id.movieRecyclerView)
        movieRecyclerView.layoutManager = GridLayoutManager(this, 3)
        movieList = mutableListOf()
        filteredList = mutableListOf()
        searchView = findViewById(R.id.searchView)

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
                    filteredList.addAll(movieList)
                    movieAdapter = MovieAdapter(this@MainActivity, filteredList) { movie ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra("movie", movie)
                        startActivity(intent)
                    }
                    movieRecyclerView.adapter = movieAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList.clear()
                val searchText = newText?.lowercase() ?: ""
                if (searchText.isNotEmpty()) {
                    movieList.forEach {
                        if (it.title.lowercase().contains(searchText)) {
                            filteredList.add(it)
                        }
                    }
                } else {
                    filteredList.addAll(movieList)
                }
                movieAdapter.notifyDataSetChanged()
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_saved -> {
                startActivity(Intent(this, SavedActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
