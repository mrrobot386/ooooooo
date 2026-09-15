package com.example.data.repository

import com.example.data.db.VastDao
import com.example.data.model.MovieEntity
import com.example.data.model.SceneEntity
import com.example.data.model.ShortEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MovieRepository(private val dao: VastDao) {

    val allMovies: Flow<List<MovieEntity>> = dao.getAllMovies()
    val allShorts: Flow<List<ShortEntity>> = dao.getAllShorts()

    fun getMovie(id: Long): Flow<MovieEntity?> = dao.getMovieById(id)
    fun getScenesForMovie(movieId: Long): Flow<List<SceneEntity>> = dao.getScenesForMovie(movieId)
    fun getShortsForMovie(movieId: Long): Flow<List<ShortEntity>> = dao.getShortsForMovie(movieId)
    fun getShort(id: Long): Flow<ShortEntity?> = dao.getShortById(id)

    suspend fun addMovie(movie: MovieEntity): Long {
        return dao.insertMovie(movie)
    }

    suspend fun updateMovie(movie: MovieEntity) {
        dao.updateMovie(movie)
    }

    suspend fun deleteMovie(id: Long) {
        dao.deleteMovieById(id)
        dao.deleteScenesForMovie(id)
    }

    suspend fun saveScenes(scenes: List<SceneEntity>) {
        dao.insertScenes(scenes)
    }

    suspend fun saveShorts(shorts: List<ShortEntity>) {
        dao.insertShorts(shorts)
    }

    suspend fun updateShort(short: ShortEntity) {
        dao.updateShort(short)
    }

    suspend fun deleteShort(id: Long) {
        dao.deleteShortById(id)
    }

    suspend fun ensureSampleDataLoaded() {
        val existing = dao.getAllMovies().firstOrNull()
        if (existing.isNullOrEmpty()) {
            // Load Demo Licensed Movie: "Dhaka Underworld: Protocol 7"
            val sampleMovieId = dao.insertMovie(
                MovieEntity(
                    title = "Operation Dhaka: Protocol 7",
                    filePath = "licensed_vault/dhaka_protocol_7_master.mp4",
                    fileSize = "4.2 GB",
                    durationFormatted = "01:54:20",
                    durationSeconds = 6860,
                    resolution = "4K UHD (3840x2160)",
                    format = "MP4",
                    status = "READY",
                    licenseKey = "LIC-BGD-2026-9941-VAST",
                    licenseHolder = "Apex Cinematic Ventures Ltd.",
                    licenseType = "Worldwide Theatrical & Digital Rights",
                    licenseVerified = true,
                    analysisSummary = "High-octane espionage thriller analyzed. Detected 42 scenes, 6 major characters, 18 high-impact moments. Filtered 7 boring/repeated sequence segments. Generated 7 multi-genre shorts with complete Bangla, English, and Hindi dialogue dubs."
                )
            )

            // Insert Analyzed Scenes
            val scenes = listOf(
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 1,
                    timeRange = "00:08:15 - 00:09:20",
                    startSec = 495,
                    endSec = 560,
                    sceneType = "ACTION",
                    impactScore = 98,
                    characterNames = "Major Kabir, Agent Zoya",
                    dialogueOriginal = "Hold your position! The server breach is happening in thirty seconds!",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Rapid kinetic cuts, high frequency audio transients, face tracking locked on protagonist."
                ),
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 2,
                    timeRange = "00:24:40 - 00:25:35",
                    startSec = 1480,
                    endSec = 1535,
                    sceneType = "EMOTIONAL",
                    impactScore = 94,
                    characterNames = "Kabir, Mother Rahima",
                    dialogueOriginal = "I never left to abandon you, Ma. I left so you wouldn't have to live in fear.",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Sustained close-up portrait, warm color grading, dynamic center crop with soft bokeh."
                ),
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 3,
                    timeRange = "00:41:10 - 00:41:55",
                    startSec = 2470,
                    endSec = 2515,
                    sceneType = "SUSPENSE",
                    impactScore = 92,
                    characterNames = "The Shadow Informant",
                    dialogueOriginal = "If you unlock that vault, your name will be erased from every database tonight.",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Low-key chiaroscuro lighting, ticking clock pacing, dramatic zoom punch-in on eye contact."
                ),
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 4,
                    timeRange = "00:58:05 - 00:58:45",
                    startSec = 3485,
                    endSec = 3525,
                    sceneType = "BEST_DIALOGUE",
                    impactScore = 96,
                    characterNames = "Kabir, Chief Masud",
                    dialogueOriginal = "Fear isn't what stops bullets. It's the decision you make before pulling the trigger.",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Punchy rhythm, golden ratio framing, prominent karaoke text overlay."
                ),
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 5,
                    timeRange = "01:12:30 - 01:13:20",
                    startSec = 4350,
                    endSec = 4400,
                    sceneType = "FUNNY",
                    impactScore = 88,
                    characterNames = "Tech Babu, Kabir",
                    dialogueOriginal = "You asked for an invisible tracker! I didn't say it was waterproof against Dhaka rain!",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Quick reaction whip-pans, comedic timing pauses, bright contrast."
                ),
                SceneEntity(
                    movieId = sampleMovieId,
                    sceneNumber = 6,
                    timeRange = "01:35:00 - 01:36:10",
                    startSec = 5700,
                    endSec = 5770,
                    sceneType = "STORY_CLIMAX",
                    impactScore = 99,
                    characterNames = "Kabir, Mastermind Tariq",
                    dialogueOriginal = "The game is over, Tariq. The whole city is watching this live feed right now!",
                    isHighImpact = true,
                    isBoringOrRepeated = false,
                    pacingNotes = "Maximum dynamic range, orchestral crescendo, dynamic zoom oscillation."
                )
            )
            dao.insertScenes(scenes)

            // Insert 7 Generated Shorts Variations
            val shorts = listOf(
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "Operation Dhaka: The 30-Second Breach",
                    shortType = "Action Short",
                    durationSec = 32,
                    aspectRatio = "9:16",
                    openingHook = "⚡ NEVER HACK A MILITARY SERVER IN DHAKA!",
                    originalDialogue = "Hold your position! The server breach is happening in thirty seconds!",
                    banglaDub = "নিজের পজিশন ধরে রাখো! আর ঠিক ত্রিশ সেকেন্ডের মধ্যে সার্ভার ভেঙে পড়বে!",
                    englishDub = "Hold your position right now! The main server breaches in thirty seconds!",
                    hindiDub = "अपनी जगह पर डटे रहो! ठीक तीस सेकंड में मेन सर्वर टूट जाएगा!",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.5f,
                    dynamicZoom = 1.2f,
                    isExported = true,
                    exportQuality = "4K"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "A Soldier's True Promise",
                    shortType = "Emotional Short",
                    durationSec = 44,
                    aspectRatio = "9:16",
                    openingHook = "💔 The hardest words a soldier ever had to speak...",
                    originalDialogue = "I never left to abandon you, Ma. I left so you wouldn't have to live in fear.",
                    banglaDub = "তোমাকে ফেলে চলে যাবার জন্য যাইনি মা। আমি গিয়েছিলাম যাতে তোমাকে আর কোনোদিন ভয়ে বাঁচতে না হয়।",
                    englishDub = "I never left to abandon you, Mother. I left so you would never have to live in fear again.",
                    hindiDub = "मैं आपको छोड़कर भागने के लिए नहीं गया था माँ। मैं इसलिए गया था ताकि आपको कभी डर में न जीना पड़े।",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.48f,
                    dynamicZoom = 1.1f,
                    isExported = false,
                    exportQuality = "1080p"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "The Golden Rule of Combat",
                    shortType = "Best Dialogue",
                    durationSec = 28,
                    aspectRatio = "9:16",
                    openingHook = "🔥 The dialogue that shook the entire theatre!",
                    originalDialogue = "Fear isn't what stops bullets. It's the decision you make before pulling the trigger.",
                    banglaDub = "ভয় দিয়ে কখনো গুলির আঘাত থামানো যায় না। আসল জিনিস হলো ট্রিগার টানার আগের মুহূর্তের সিদ্ধান্ত!",
                    englishDub = "Fear is not what stops bullets. It is the conviction you make right before you pull the trigger.",
                    hindiDub = "डर गोलियों को नहीं रोकता। सबसे बड़ा फैसला वो होता है जो तुम ट्रिगर दबाने से ठीक पहले लेते हो!",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.52f,
                    dynamicZoom = 1.25f,
                    isExported = true,
                    exportQuality = "4K"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "Dhaka Rain vs Cyber Tracker",
                    shortType = "Funny Moment",
                    durationSec = 25,
                    aspectRatio = "9:16",
                    openingHook = "😂 When high-tech meets Bangladeshi monsoon rain!",
                    originalDialogue = "You asked for an invisible tracker! I didn't say it was waterproof against Dhaka rain!",
                    banglaDub = "তুমি তো চেয়েছিলে অদৃশ্য ট্র্যাকার! আমি কি বলেছি যে এটা ঢাকার মেঘভাঙা বৃষ্টি সহ্য করতে পারবে?!",
                    englishDub = "You asked for an invisible tracker! I never promised it was waterproof against Dhaka monsoon rain!",
                    hindiDub = "तुमने अदृश्य ट्रैकर माँगा था! मैंने ये थोड़ी कहा था कि ये ढाका की मूसलाधार बारिश झेल लेगा!",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.5f,
                    dynamicZoom = 1.15f,
                    isExported = false,
                    exportQuality = "1080p"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "The Midnight Threat",
                    shortType = "Suspense Moment",
                    durationSec = 35,
                    aspectRatio = "9:16",
                    openingHook = "⚠️ DO NOT OPEN THE VAULT AFTER MIDNIGHT...",
                    originalDialogue = "If you unlock that vault, your name will be erased from every database tonight.",
                    banglaDub = "যদি তুমি ওই ভল্ট খোলার দুঃসাহস করো, তাহলে আজ রাতেই দুনিয়ার সমস্ত ডেটাবেস থেকে তোমার নাম মুছে ফেলা হবে।",
                    englishDub = "If you dare unlock that vault tonight, your entire identity will be wiped from every database.",
                    hindiDub = "अगर तुमने उस तिजोरी को छुआ, तो आज रात ही हर सरकारी डेटाबेस से तुम्हारा नामोनिशान मिटा दिया जाएगा।",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.45f,
                    dynamicZoom = 1.22f,
                    isExported = false,
                    exportQuality = "4K"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "The Final Checkmate Live",
                    shortType = "Story Short",
                    durationSec = 52,
                    aspectRatio = "9:16",
                    openingHook = "💥 10 Million people watching this live stream right now!",
                    originalDialogue = "The game is over, Tariq. The whole city is watching this live feed right now!",
                    banglaDub = "তোমার খেলা শেষ তারিক! এই মুহূর্তে পুরো রাজধানী লাইভ ফিডে তোমার মুখোশ খুলে দেখছে!",
                    englishDub = "Game over, Tariq. The entire metropolis is tuned into this live broadcast right now!",
                    hindiDub = "तुम्हारा खेल खत्म तारिक! इस वक्त पूरा शहर इस लाइव फीड पर तुम्हारी असलियत देख रहा है!",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.5f,
                    dynamicZoom = 1.18f,
                    isExported = false,
                    exportQuality = "1080p"
                ),
                ShortEntity(
                    movieId = sampleMovieId,
                    title = "Major Kabir: Unbroken Stance",
                    shortType = "Character Moment",
                    durationSec = 38,
                    aspectRatio = "9:16",
                    openingHook = "⚔️ How a true special operative handles betrayal...",
                    originalDialogue = "You took my badge. You didn't take what makes me who I am.",
                    banglaDub = "তোমরা হয়তো আমার ব্যাজ কেড়ে নিয়েছো। কিন্তু আমাকে যে মানুষ বানিয়েছে, সেটা কেড়ে নেওয়ার ক্ষমতা তোমাদের নেই!",
                    englishDub = "You may have revoked my badge. But you can never take away what forged my core.",
                    hindiDub = "तुमने मेरा बैज भले ही छीन लिया हो। लेकिन मुझे जो पहचान मिली है, उसे छीनने की औकात किसी में नहीं है!",
                    activeAudioLang = "bn",
                    subtitleTrack = "bn",
                    smartCropOffsetX = 0.5f,
                    dynamicZoom = 1.3f,
                    isExported = true,
                    exportQuality = "4K"
                )
            )
            dao.insertShorts(shorts)
        }
    }
}
