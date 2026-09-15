package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val fileSize: String,
    val durationFormatted: String,
    val durationSeconds: Int,
    val resolution: String = "4K UHD (3840x2160)",
    val format: String = "MP4",
    val status: String = "READY", // UPLOADING, ANALYZING, READY, PROCESSING_SHORTS, COMPLETED
    val licenseKey: String = "VAST-LIC-8892-PRO",
    val licenseHolder: String = "Apex Worldwide Media Ltd.",
    val licenseType: String = "Commercial Global Distribution",
    val licenseVerified: Boolean = true,
    val analysisSummary: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
