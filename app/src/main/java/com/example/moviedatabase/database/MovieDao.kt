package com.example.moviedatabase.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToFavorites(movie: MovieEntity)

    @Delete
    suspend fun removeMovieFromFavorites(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: String): Flow<MovieEntity?>
}
