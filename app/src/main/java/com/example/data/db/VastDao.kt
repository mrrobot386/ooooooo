package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MovieEntity
import com.example.data.model.SceneEntity
import com.example.data.model.ShortEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VastDao {
    // Movies
    @Query("SELECT * FROM movies ORDER BY timestamp DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun getMovieById(id: Long): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity): Long

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

    // Scenes
    @Query("SELECT * FROM scenes WHERE movieId = :movieId ORDER BY sceneNumber ASC")
    fun getScenesForMovie(movieId: Long): Flow<List<SceneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<SceneEntity>)

    @Query("DELETE FROM scenes WHERE movieId = :movieId")
    suspend fun deleteScenesForMovie(movieId: Long)

    // Shorts
    @Query("SELECT * FROM shorts ORDER BY timestamp DESC")
    fun getAllShorts(): Flow<List<ShortEntity>>

    @Query("SELECT * FROM shorts WHERE movieId = :movieId ORDER BY id ASC")
    fun getShortsForMovie(movieId: Long): Flow<List<ShortEntity>>

    @Query("SELECT * FROM shorts WHERE id = :id LIMIT 1")
    fun getShortById(id: Long): Flow<ShortEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShorts(shorts: List<ShortEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShort(short: ShortEntity): Long

    @Update
    suspend fun updateShort(short: ShortEntity)

    @Query("DELETE FROM shorts WHERE id = :id")
    suspend fun deleteShortById(id: Long)
}
