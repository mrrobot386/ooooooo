package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shorts")
data class ShortEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieId: Long,
    val title: String,
    val shortType: String, // Story Short, Action Short, Emotional Short, Funny Moment, Suspense Moment, Character Moment, Best Dialogue
    val durationSec: Int,
    val aspectRatio: String = "9:16",
    val openingHook: String,
    val originalDialogue: String,
    val banglaDub: String,
    val englishDub: String,
    val hindiDub: String,
    val activeAudioLang: String = "bn", // bn, en, hi, orig
    val subtitleTrack: String = "bn", // bn, en, hi, none
    val smartCropOffsetX: Float = 0.5f,
    val dynamicZoom: Float = 1.15f,
    val isExported: Boolean = false,
    val exportQuality: String = "1080p", // 1080p, 4K
    val downloadUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
