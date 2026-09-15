package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShortEntity
import com.example.ui.components.AudioMixerConsole
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun AudioMixerScreen(
    originalVol: Float,
    dubbedVol: Float,
    bgMusicVol: Float,
    sfxVol: Float,
    noiseReduction: Boolean,
    voiceEnhance: Boolean,
    autoDucking: Boolean,
    volumeNorm: Boolean,
    selectedShort: ShortEntity?,
    activeAudioLang: String,
    isPlaying: Boolean,
    onOriginalVolChange: (Float) -> Unit,
    onDubbedVolChange: (Float) -> Unit,
    onBgMusicVolChange: (Float) -> Unit,
    onSfxVolChange: (Float) -> Unit,
    onToggleNoiseReduction: () -> Unit,
    onToggleVoiceEnhance: () -> Unit,
    onToggleAutoDucking: () -> Unit,
    onToggleVolumeNorm: () -> Unit,
    onSelectAudioLang: (String) -> Unit,
    onTogglePlayAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        item {
            Column {
                Text(
                    text = "DUBBING & AUDIO MASTERING SUITE",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Speech-to-Transcript • AI Dubbing • Lip-Sync Adaptation • Auto Ducking",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
            }
        }

        // 4-Channel Audio Mixer Console
        item {
            AudioMixerConsole(
                originalVol = originalVol,
                dubbedVol = dubbedVol,
                bgMusicVol = bgMusicVol,
                sfxVol = sfxVol,
                noiseReduction = noiseReduction,
                voiceEnhance = voiceEnhance,
                autoDucking = autoDucking,
                volumeNorm = volumeNorm,
                onOriginalVolChange = onOriginalVolChange,
                onDubbedVolChange = onDubbedVolChange,
                onBgMusicVolChange = onBgMusicVolChange,
                onSfxVolChange = onSfxVolChange,
                onToggleNoiseReduction = onToggleNoiseReduction,
                onToggleVoiceEnhance = onToggleVoiceEnhance,
                onToggleAutoDucking = onToggleAutoDucking,
                onToggleVolumeNorm = onToggleVolumeNorm
            )
        }

        // Live Multilingual Voice Track Selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, StudioBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = StudioSurface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                    Text(
                        text = "ACTIVE DUBBED VOCAL TRACK",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DubVoiceModelRow(
                        title = "🇧🇩 Bangla (বাংলা) Neural Voice Model",
                        actor = "Actor Voice Clone #BGD-04 • Natural Dialect",
                        accentColor = SuccessGreen,
                        isSelected = activeAudioLang == "bn",
                        onClick = { onSelectAudioLang("bn") },
                        testTag = "voice_track_bangla"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DubVoiceModelRow(
                        title = "🇬🇧 English Cinematic Voice Model",
                        actor = "Hollywood Action Baritone #US-09 • Crisp Pacing",
                        accentColor = NeonCyan,
                        isSelected = activeAudioLang == "en",
                        onClick = { onSelectAudioLang("en") },
                        testTag = "voice_track_english"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DubVoiceModelRow(
                        title = "🇮🇳 Hindi (हिंदी) Dramatic Voice Model",
                        actor = "Bollywood Cinematic Lead #IND-02 • High Emotion",
                        accentColor = FilmAmber,
                        isSelected = activeAudioLang == "hi",
                        onClick = { onSelectAudioLang("hi") },
                        testTag = "voice_track_hindi"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DubVoiceModelRow(
                        title = "🎬 Original Master Audio Track",
                        actor = "Direct Theatrical Sound Mix • Passthrough",
                        accentColor = Color(0xFF94A3B8),
                        isSelected = activeAudioLang == "orig",
                        onClick = { onSelectAudioLang("orig") },
                        testTag = "voice_track_original"
                    )
                }
            }
        }

        // Lip-Sync & Timing Sync Diagnostics
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, StudioBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = StudioSurface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LIP-SYNC & PHONEME TIMING ENGINE",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SuccessGreen.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LOCKED [98.6%]",
                                color = SuccessGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "VAST AI aligns phonetic cadence to actor mouth movements in Bangla, English and Hindi, automatically stretching vowel durations to match on-screen gestures.",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Play/Test Mixed Audio Button
        item {
            ElevatedButton(
                onClick = onTogglePlayAudio,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("play_audio_mix_test_button")
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.Headphones,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPlaying) "STOP AUDIO MONITOR" else "PREVIEW LIVE DUBBED AUDIO MIX",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun DubVoiceModelRow(
    title: String,
    actor: String,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.12f) else Color(0xFF0F172A))
            .border(
                1.dp,
                if (isSelected) accentColor else StudioBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(10.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
            )
            Text(
                text = actor,
                fontSize = 9.sp,
                color = if (isSelected) accentColor else Color(0xFF64748B)
            )
        }

        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (isSelected) accentColor else Color(0xFF334155))
        )
    }
}
