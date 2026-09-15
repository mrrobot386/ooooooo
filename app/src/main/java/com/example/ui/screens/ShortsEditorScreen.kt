package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShortEntity
import com.example.ui.components.ShortsPlayerView
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun ShortsEditorScreen(
    shorts: List<ShortEntity>,
    selectedShort: ShortEntity?,
    isPlaying: Boolean,
    aspectRatio: String,
    activeAudioLang: String,
    activeSubtitleLang: String,
    faceTrackingEnabled: Boolean,
    dynamicZoomEnabled: Boolean,
    onSelectShort: (ShortEntity) -> Unit,
    onTogglePlay: () -> Unit,
    onToggleAspectRatio: () -> Unit,
    onToggleFaceTracking: () -> Unit,
    onToggleDynamicZoom: () -> Unit,
    onSelectAudioLang: (String) -> Unit,
    onSelectSubtitleLang: (String) -> Unit,
    onPlayDubbingSpeech: (text: String, lang: String) -> Unit,
    onExportShort: (ShortEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header: 7 Shorts Variations
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI SHORTS VARIATION SUITE",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${shorts.size} Variations Generated",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Carousel of 7 Variation Types
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    shorts.forEach { itemShort ->
                        val isSelected = itemShort.id == selectedShort?.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else StudioSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else StudioBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectShort(itemShort) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("short_pill_${itemShort.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(
                                    text = itemShort.shortType,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NeonCyan else Color.White
                                )
                                Text(
                                    text = "${itemShort.durationSec}s • 9:16",
                                    fontSize = 9.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Shorts Player View
        if (selectedShort != null) {
            item {
                ShortsPlayerView(
                    short = selectedShort,
                    isPlaying = isPlaying,
                    aspectRatioSetting = aspectRatio,
                    activeAudioLang = activeAudioLang,
                    activeSubtitleLang = activeSubtitleLang,
                    faceTrackingEnabled = faceTrackingEnabled,
                    dynamicZoomEnabled = dynamicZoomEnabled,
                    onTogglePlay = onTogglePlay,
                    onToggleAspectRatio = onToggleAspectRatio,
                    onToggleFaceTracking = onToggleFaceTracking,
                    onToggleDynamicZoom = onToggleDynamicZoom,
                    onSelectAudioLang = onSelectAudioLang,
                    onSelectSubtitleLang = onSelectSubtitleLang
                )
            }

            // Multilingual Dubbing & Subtitles Translation Lab
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, StudioBorder, RoundedCornerShape(14.dp))
                        .testTag("multilingual_dub_panel"),
                    colors = CardDefaults.cardColors(containerColor = StudioSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MULTILINGUAL DUBBING TRACKS",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Subtitle Toggle
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("SUBTITLES: ", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                listOf("bn" to "বাংলা", "en" to "EN", "hi" to "हिंदी", "none" to "OFF").forEach { (code, label) ->
                                    val isSelected = activeSubtitleLang == code
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isSelected) ElectricViolet else Color(0xFF1E293B))
                                            .clickable { onSelectSubtitleLang(code) }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 9.sp,
                                            color = Color.White,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 🇧🇩 Bangla Dub Box
                        DubLineCard(
                            language = "🇧🇩 BANGLA DUB (বাংলা সংলাপ)",
                            text = selectedShort.banglaDub,
                            accentColor = SuccessGreen,
                            isActive = activeAudioLang == "bn",
                            onPlayVoice = { onPlayDubbingSpeech(selectedShort.banglaDub, "bn") },
                            onSelectAsAudio = { onSelectAudioLang("bn") },
                            testTag = "dub_bangla_card"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 🇬🇧 English Dub Box
                        DubLineCard(
                            language = "🇬🇧 ENGLISH DUB (CINEMATIC)",
                            text = selectedShort.englishDub,
                            accentColor = NeonCyan,
                            isActive = activeAudioLang == "en",
                            onPlayVoice = { onPlayDubbingSpeech(selectedShort.englishDub, "en") },
                            onSelectAsAudio = { onSelectAudioLang("en") },
                            testTag = "dub_english_card"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 🇮🇳 Hindi Dub Box
                        DubLineCard(
                            language = "🇮🇳 HINDI DUB (हिंदी संवाद)",
                            text = selectedShort.hindiDub,
                            accentColor = FilmAmber,
                            isActive = activeAudioLang == "hi",
                            onPlayVoice = { onPlayDubbingSpeech(selectedShort.hindiDub, "hi") },
                            onSelectAsAudio = { onSelectAudioLang("hi") },
                            testTag = "dub_hindi_card"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Original Dialogue Line
                        DubLineCard(
                            language = "🎬 ORIGINAL MASTER AUDIO",
                            text = selectedShort.originalDialogue,
                            accentColor = Color(0xFF94A3B8),
                            isActive = activeAudioLang == "orig",
                            onPlayVoice = { onPlayDubbingSpeech(selectedShort.originalDialogue, "orig") },
                            onSelectAsAudio = { onSelectAudioLang("orig") },
                            testTag = "dub_original_card"
                        )
                    }
                }
            }

            // Export Action Button for this Short
            item {
                ElevatedButton(
                    onClick = { onExportShort(selectedShort) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("export_active_short_button"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NeonCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EXPORT THIS SHORT (1080P / 4K • 9:16)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun DubLineCard(
    language: String,
    text: String,
    accentColor: Color,
    isActive: Boolean,
    onPlayVoice: () -> Unit,
    onSelectAsAudio: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) accentColor.copy(alpha = 0.08f) else Color(0xFF0F172A))
            .border(
                1.dp,
                if (isActive) accentColor.copy(alpha = 0.6f) else StudioBorder,
                RoundedCornerShape(8.dp)
            )
            .padding(10.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = language,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Speak preview
                    IconButton(
                        onClick = onPlayVoice,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Speak Dub",
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Set as active audio track
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isActive) accentColor else StudioSurface)
                            .border(1.dp, if (isActive) accentColor else StudioBorder, RoundedCornerShape(4.dp))
                            .clickable { onSelectAsAudio() }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isActive) "ACTIVE AUDIO" else "SET AUDIO",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) Color.Black else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                lineHeight = 17.sp
            )
        }
    }
}
