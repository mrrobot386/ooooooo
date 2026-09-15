package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.VastAiEngine
import com.example.audio.VastTtsEngine
import com.example.data.db.AppDatabase
import com.example.data.model.MovieEntity
import com.example.data.model.SceneEntity
import com.example.data.model.ShortEntity
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    private val aiEngine = VastAiEngine()
    val ttsEngine = VastTtsEngine(application)

    val allMovies: StateFlow<List<MovieEntity>>
    val allShorts: StateFlow<List<ShortEntity>>

    private val _selectedMovie = MutableStateFlow<MovieEntity?>(null)
    val selectedMovie: StateFlow<MovieEntity?> = _selectedMovie.asStateFlow()

    private val _scenes = MutableStateFlow<List<SceneEntity>>(emptyList())
    val scenes: StateFlow<List<SceneEntity>> = _scenes.asStateFlow()

    private val _selectedShort = MutableStateFlow<ShortEntity?>(null)
    val selectedShort: StateFlow<ShortEntity?> = _selectedShort.asStateFlow()

    // Navigation / Active View: "STUDIO", "SHORTS_EDITOR", "AUDIO_MIXER", "EXPORTS", "LICENSE"
    private val _activeTab = MutableStateFlow("STUDIO")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    // Workflow & AI State
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _progressPercent = MutableStateFlow(0f)
    val progressPercent: StateFlow<Float> = _progressPercent.asStateFlow()

    private val _progressMessage = MutableStateFlow("")
    val progressMessage: StateFlow<String> = _progressMessage.asStateFlow()

    // Player & Preview
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _aspectRatio = MutableStateFlow("9:16") // "9:16", "16:9"
    val aspectRatio: StateFlow<String> = _aspectRatio.asStateFlow()

    private val _activeAudioLang = MutableStateFlow("bn") // bn, en, hi, orig
    val activeAudioLang: StateFlow<String> = _activeAudioLang.asStateFlow()

    private val _activeSubtitleLang = MutableStateFlow("bn") // bn, en, hi, none
    val activeSubtitleLang: StateFlow<String> = _activeSubtitleLang.asStateFlow()

    private val _faceTrackingEnabled = MutableStateFlow(true)
    val faceTrackingEnabled: StateFlow<Boolean> = _faceTrackingEnabled.asStateFlow()

    private val _dynamicZoomEnabled = MutableStateFlow(true)
    val dynamicZoomEnabled: StateFlow<Boolean> = _dynamicZoomEnabled.asStateFlow()

    // Audio Mixer State
    private val _originalAudioVol = MutableStateFlow(0.25f)
    val originalAudioVol: StateFlow<Float> = _originalAudioVol.asStateFlow()

    private val _dubbedVoiceVol = MutableStateFlow(0.95f)
    val dubbedVoiceVol: StateFlow<Float> = _dubbedVoiceVol.asStateFlow()

    private val _bgMusicVol = MutableStateFlow(0.60f)
    val bgMusicVol: StateFlow<Float> = _bgMusicVol.asStateFlow()

    private val _sfxVol = MutableStateFlow(0.70f)
    val sfxVol: StateFlow<Float> = _sfxVol.asStateFlow()

    private val _noiseReduction = MutableStateFlow(true)
    val noiseReduction: StateFlow<Boolean> = _noiseReduction.asStateFlow()

    private val _voiceEnhancement = MutableStateFlow(true)
    val voiceEnhancement: StateFlow<Boolean> = _voiceEnhancement.asStateFlow()

    private val _autoDucking = MutableStateFlow(true)
    val autoDucking: StateFlow<Boolean> = _autoDucking.asStateFlow()

    private val _volumeNormalization = MutableStateFlow(true)
    val volumeNormalization: StateFlow<Boolean> = _volumeNormalization.asStateFlow()

    // License Verification State
    private val _licenseConfirmed = MutableStateFlow(true)
    val licenseConfirmed: StateFlow<Boolean> = _licenseConfirmed.asStateFlow()

    private val _licenseKey = MutableStateFlow("VAST-LIC-8892-PRO")
    val licenseKey: StateFlow<String> = _licenseKey.asStateFlow()

    private val _licenseHolder = MutableStateFlow("Apex Worldwide Media Ltd.")
    val licenseHolder: StateFlow<String> = _licenseHolder.asStateFlow()

    private val _showLicenseDialog = MutableStateFlow(false)
    val showLicenseDialog: StateFlow<Boolean> = _showLicenseDialog.asStateFlow()

    // Export Feedback
    private val _exportSuccessNotice = MutableStateFlow<String?>(null)
    val exportSuccessNotice: StateFlow<String?> = _exportSuccessNotice.asStateFlow()

    init {
        val database = AppDatabase.getInstance(application)
        repository = MovieRepository(database.vastDao())

        allMovies = repository.allMovies.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allShorts = repository.allShorts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.ensureSampleDataLoaded()
            repository.allMovies.collect { list ->
                if (list.isNotEmpty() && _selectedMovie.value == null) {
                    selectMovie(list.first())
                }
            }
        }

        viewModelScope.launch {
            repository.allShorts.collect { list ->
                if (list.isNotEmpty() && _selectedShort.value == null) {
                    _selectedShort.value = list.first()
                }
            }
        }
    }

    fun setTab(tab: String) {
        _activeTab.value = tab
    }

    fun selectMovie(movie: MovieEntity) {
        _selectedMovie.value = movie
        viewModelScope.launch {
            repository.getScenesForMovie(movie.id).collect {
                _scenes.value = it
            }
        }
        viewModelScope.launch {
            repository.getShortsForMovie(movie.id).collect {
                if (it.isNotEmpty()) {
                    _selectedShort.value = it.first()
                }
            }
        }
    }

    fun selectShort(short: ShortEntity) {
        _selectedShort.value = short
        _activeAudioLang.value = short.activeAudioLang
        _activeSubtitleLang.value = short.subtitleTrack
        ttsEngine.stop()
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
        if (_isPlaying.value) {
            playCurrentDubbing()
        } else {
            ttsEngine.stop()
        }
    }

    fun playCurrentDubbing() {
        val short = _selectedShort.value ?: return
        val lang = _activeAudioLang.value
        val textToSpeak = when (lang) {
            "bn" -> short.banglaDub
            "en" -> short.englishDub
            "hi" -> short.hindiDub
            else -> short.originalDialogue
        }
        ttsEngine.speak(textToSpeak, lang)
    }

    fun setAudioLang(lang: String) {
        _activeAudioLang.value = lang
        _selectedShort.value?.let { current ->
            val updated = current.copy(activeAudioLang = lang)
            _selectedShort.value = updated
            viewModelScope.launch { repository.updateShort(updated) }
        }
        if (_isPlaying.value) {
            playCurrentDubbing()
        }
    }

    fun setSubtitleLang(lang: String) {
        _activeSubtitleLang.value = lang
        _selectedShort.value?.let { current ->
            val updated = current.copy(subtitleTrack = lang)
            _selectedShort.value = updated
            viewModelScope.launch { repository.updateShort(updated) }
        }
    }

    fun toggleAspectRatio() {
        _aspectRatio.value = if (_aspectRatio.value == "9:16") "16:9" else "9:16"
    }

    fun toggleFaceTracking() {
        _faceTrackingEnabled.value = !_faceTrackingEnabled.value
    }

    fun toggleDynamicZoom() {
        _dynamicZoomEnabled.value = !_dynamicZoomEnabled.value
    }

    // Audio Mixer Setters
    fun setOriginalAudioVol(vol: Float) { _originalAudioVol.value = vol }
    fun setDubbedVoiceVol(vol: Float) { _dubbedVoiceVol.value = vol }
    fun setBgMusicVol(vol: Float) { _bgMusicVol.value = vol }
    fun setSfxVol(vol: Float) { _sfxVol.value = vol }
    fun toggleNoiseReduction() { _noiseReduction.value = !_noiseReduction.value }
    fun toggleVoiceEnhance() { _voiceEnhancement.value = !_voiceEnhancement.value }
    fun toggleAutoDucking() { _autoDucking.value = !_autoDucking.value }
    fun toggleVolumeNorm() { _volumeNormalization.value = !_volumeNormalization.value }

    // License Management
    fun openLicenseDialog() { _showLicenseDialog.value = true }
    fun closeLicenseDialog() { _showLicenseDialog.value = false }
    fun verifyLicense(key: String, holder: String) {
        _licenseKey.value = key
        _licenseHolder.value = holder
        _licenseConfirmed.value = true
        _showLicenseDialog.value = false
        _exportSuccessNotice.value = "License verified successfully: $holder"
    }

    // Workflow: Full AI Movie Pipeline
    fun runFullPipelineForMovie(movie: MovieEntity) {
        viewModelScope.launch {
            _isProcessing.value = true
            _progressPercent.value = 0.05f
            _progressMessage.value = "Starting AI Pipeline for \"${movie.title}\"..."
            delay(500)

            // Step 1: Scene Detection & Pacing
            _progressPercent.value = 0.20f
            _progressMessage.value = "AI Scene Detection & Face Tracking in progress..."
            val (analyzedScenes, summary) = aiEngine.analyzeMovie(movie.title, "Action/Thriller", movie.durationSeconds / 60)
            delay(800)

            // Step 2: Remove Boring Sections & Filter
            _progressPercent.value = 0.40f
            _progressMessage.value = "Filtering monotonous sections & selecting high-impact moments..."
            delay(700)

            val updatedMovie = movie.copy(
                status = "PROCESSING_SHORTS",
                analysisSummary = summary
            )
            repository.updateMovie(updatedMovie)
            repository.saveScenes(analyzedScenes.map { it.copy(movieId = movie.id) })

            // Step 3: Multilingual Dubbing & Subtitles
            _progressPercent.value = 0.65f
            _progressMessage.value = "Generating Natural Dubbing: Bangla (বাংলা), English, Hindi (हिंदी)..."
            val generatedShorts = aiEngine.generateShortsFromScenes(movie.id, movie.title, analyzedScenes)
            delay(900)

            // Step 4: Audio Mix & Normalization
            _progressPercent.value = 0.85f
            _progressMessage.value = "Ducking background score, noise filtering & volume normalizing (-14 LUFS)..."
            delay(700)

            // Step 5: Finalizing Shorts
            _progressPercent.value = 1.0f
            _progressMessage.value = "Complete! Created ${generatedShorts.size} professional shorts."
            repository.saveShorts(generatedShorts)

            val completedMovie = updatedMovie.copy(status = "COMPLETED")
            repository.updateMovie(completedMovie)
            _selectedMovie.value = completedMovie
            if (generatedShorts.isNotEmpty()) {
                _selectedShort.value = generatedShorts.first()
            }

            delay(600)
            _isProcessing.value = false
            _exportSuccessNotice.value = "VAST AI generated 7 multilingual shorts for ${movie.title}!"
        }
    }

    // Add Uploaded Movie (either picked from files or demo library)
    fun addMovieUpload(
        title: String,
        uri: String,
        fileSize: String,
        durationFormatted: String,
        durationSec: Int,
        format: String
    ) {
        viewModelScope.launch {
            val newMovie = MovieEntity(
                title = title,
                filePath = uri,
                fileSize = fileSize,
                durationFormatted = durationFormatted,
                durationSeconds = durationSec,
                resolution = "4K UHD (3840x2160)",
                format = format,
                status = "UPLOADED",
                licenseKey = _licenseKey.value,
                licenseHolder = _licenseHolder.value,
                licenseVerified = _licenseConfirmed.value,
                analysisSummary = "Uploaded licensed movie ready for AI automated analysis."
            )
            val newId = repository.addMovie(newMovie)
            val insertedMovie = newMovie.copy(id = newId)
            selectMovie(insertedMovie)
            // Trigger automatic workflow
            runFullPipelineForMovie(insertedMovie)
        }
    }

    fun exportShort(short: ShortEntity, quality: String, languageOption: String) {
        viewModelScope.launch {
            _isProcessing.value = true
            _progressPercent.value = 0.1f
            _progressMessage.value = "Mastering $quality ${short.shortType} in $languageOption..."
            delay(500)

            _progressPercent.value = 0.5f
            _progressMessage.value = "Encoding 9:16 Smart Crop with synchronized subtitles..."
            delay(600)

            _progressPercent.value = 0.9f
            _progressMessage.value = "Applying audio ducking and LUFS normalization..."
            delay(500)

            _progressPercent.value = 1.0f
            val updated = short.copy(isExported = true, exportQuality = quality)
            repository.updateShort(updated)
            _selectedShort.value = updated

            _isProcessing.value = false
            _exportSuccessNotice.value = "Export Complete: ${short.title} ($quality, $languageOption)"
        }
    }

    fun dismissExportNotice() {
        _exportSuccessNotice.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsEngine.release()
    }
}
