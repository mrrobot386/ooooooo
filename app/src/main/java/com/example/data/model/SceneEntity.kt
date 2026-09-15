package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenes")
data class SceneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieId: Long,
    val sceneNumber: Int,
    val timeRange: String,
    val startSec: Int,
    val endSec: Int,
    val sceneType: String, // STORY_CLIMAX, ACTION, EMOTIONAL, FUNNY, SUSPENSE, CHARACTER_MOMENT, BEST_DIALOGUE
    val impactScore: Int, // 1 - 100
    val characterNames: String,
    val dialogueOriginal: String,
    val isHighImpact: Boolean = true,
    val isBoringOrRepeated: Boolean = false,
    val pacingNotes: String = "Fast cinematic pacing with rapid face cut"
)
