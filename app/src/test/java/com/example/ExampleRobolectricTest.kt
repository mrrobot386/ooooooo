package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.MovieEntity
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("VAST AI", appName)
    }

    @Test
    fun `test movie repository and sample data loading`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getInstance(context)
        val repository = MovieRepository(db.vastDao())

        repository.ensureSampleDataLoaded()
        val movies = repository.allMovies.first()
        assertTrue("Movies list should not be empty", movies.isNotEmpty())

        val firstMovie = movies.first()
        assertNotNull(firstMovie.title)
        assertEquals("4K UHD (3840x2160)", firstMovie.resolution)

        val shorts = repository.getShortsForMovie(firstMovie.id).first()
        assertTrue("Generated shorts should exist", shorts.isNotEmpty())
    }
}
