package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VastTtsEngine(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSpeakerLang = MutableStateFlow("bn")
    val currentSpeakerLang: StateFlow<String> = _currentSpeakerLang.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isPlaying.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isPlaying.value = false
                    }

                    override fun onError(utteranceId: String?) {
                        _isPlaying.value = false
                    }
                })
            } else {
                Log.w("VastTtsEngine", "TextToSpeech init failed with status: $status")
            }
        }
    }

    fun speak(text: String, languageCode: String) {
        if (!isInitialized || tts == null) {
            _isPlaying.value = false
            return
        }

        _currentSpeakerLang.value = languageCode
        val locale = when (languageCode) {
            "bn" -> Locale.forLanguageTag("bn-BD")
            "hi" -> Locale.forLanguageTag("hi-IN")
            else -> Locale.US
        }

        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English if device does not have regional voice installed
                tts?.setLanguage(Locale.US)
            }
            tts?.setSpeechRate(0.95f)
            tts?.setPitch(1.0f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "VAST_DUB_${System.currentTimeMillis()}")
            _isPlaying.value = true
        } catch (e: Exception) {
            Log.e("VastTtsEngine", "Error invoking speak", e)
            _isPlaying.value = false
        }
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
