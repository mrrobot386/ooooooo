package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ShortEntity
import com.example.ui.theme.ActionRed
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun ShortsPlayerView(
    short: ShortEntity,
    isPlaying: Boolean,
    aspectRatioSetting: String,
    activeAudioLang: String,
    activeSubtitleLang: String,
    faceTrackingEnabled: Boolean,
    dynamicZoomEnabled: Boolean,
    onTogglePlay: () -> Unit,
    onToggleAspectRatio: () -> Unit,
    onToggleFaceTracking: () -> Unit,
    onToggleDynamicZoom: () -> Unit,
    onSelectAudioLang: (String) -> Unit,
    onSelectSubtitleLang: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "player_motion")
    val zoomScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isPlaying && dynamicZoomEnabled) 1.08f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "zoom"
    )

    val faceBoxPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "faceBox"
    )

    var progress by remember { mutableFloatStateOf(0.35f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(StudioDark)
            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag("shorts_player_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Player Video Canvas Container
        val targetRatio = if (aspectRatioSetting == "9:16") (9f / 16f) else (16f / 9f)
        val maxHeightDp = if (aspectRatioSetting == "9:16") 420.dp else 230.dp

        Box(
            modifier = Modifier
                .height(maxHeightDp)
                .aspectRatio(targetRatio)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
                .border(2.dp, if (isPlaying) NeonCyan.copy(alpha = 0.8f) else StudioBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Background Video Simulation
            Image(
                painter = painterResource(id = R.drawable.img_sample_movie),
                contentDescription = "Movie Scene",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(zoomScale),
                contentScale = ContentScale.Crop
            )

            // Dark subtle vignette gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.45f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            // Top Header in Video: Hook & Live Badges
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ratio Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (aspectRatioSetting == "9:16") "9:16 VERTICAL" else "16:9 CINEMA",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Face Track status
                    if (faceTrackingEnabled) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SuccessGreen.copy(alpha = 0.2f))
                                .border(1.dp, SuccessGreen, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FACE LOCKED [98%]",
                                color = SuccessGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Viral Opening Hook Banner
                if (short.openingHook.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(FilmAmber.copy(alpha = 0.95f), ActionRed.copy(alpha = 0.9f))
                                )
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = short.openingHook,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // AI Face Tracking Box overlay (Simulating OpenCV/AI Tracker)
            if (faceTrackingEnabled) {
                Box(
                    modifier = Modifier
                        .size(110.dp, 125.dp)
                        .align(Alignment.Center)
                        .scale(faceBoxPulse)
                        .border(1.5.dp, NeonCyan.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .background(NeonCyan)
                            .padding(horizontal = 3.dp, vertical = 1.dp)
                    ) {
                        Text("TRACK #1", color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Subtitles Box at bottom of video
            if (activeSubtitleLang != "none") {
                val subtitleText = when (activeSubtitleLang) {
                    "bn" -> short.banglaDub
                    "en" -> short.englishDub
                    "hi" -> short.hindiDub
                    else -> short.originalDialogue
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Subtitles,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (activeSubtitleLang) {
                                    "bn" -> "বাংলা সাবটাইটেল (AUTO-SYNC)"
                                    "hi" -> "हिंदी सबटाइटल (AUTO-SYNC)"
                                    else -> "ENGLISH CAPTION (AUTO-SYNC)"
                                },
                                fontSize = 9.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitleText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Central Play Overlay Icon if paused
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(2.dp, NeonCyan, CircleShape)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = NeonCyan,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Timeline Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "0:${((progress * short.durationSec).toInt()).toString().padStart(2, '0')}",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )

            Slider(
                value = progress,
                onValueChange = { progress = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = NeonCyan,
                    activeTrackColor = NeonCyan,
                    inactiveTrackColor = Color(0xFF1E293B)
                )
            )

            Text(
                text = "0:${short.durationSec.toString().padStart(2, '0')}",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Medium
            )
        }

        // Quick Player Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Play / Pause Toggle
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(36.dp)
                        .background(NeonCyan, CircleShape)
                        .testTag("player_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 9:16 vs 16:9 Aspect ratio toggle
                IconButton(
                    onClick = onToggleAspectRatio,
                    modifier = Modifier
                        .size(36.dp)
                        .background(StudioSurface, CircleShape)
                        .border(1.dp, StudioBorder, CircleShape)
                        .testTag("toggle_ratio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CropRotate,
                        contentDescription = "Toggle Aspect Ratio",
                        tint = if (aspectRatioSetting == "9:16") NeonCyan else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Face Tracking toggle
                IconButton(
                    onClick = onToggleFaceTracking,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (faceTrackingEnabled) SuccessGreen.copy(alpha = 0.2f) else StudioSurface,
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (faceTrackingEnabled) SuccessGreen else StudioBorder,
                            CircleShape
                        )
                        .testTag("toggle_face_tracking_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "Toggle Face Tracking",
                        tint = if (faceTrackingEnabled) SuccessGreen else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Dynamic Zoom toggle
                IconButton(
                    onClick = onToggleDynamicZoom,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (dynamicZoomEnabled) ElectricViolet.copy(alpha = 0.2f) else StudioSurface,
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (dynamicZoomEnabled) ElectricViolet else StudioBorder,
                            CircleShape
                        )
                        .testTag("toggle_dynamic_zoom_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Toggle Dynamic Zoom",
                        tint = if (dynamicZoomEnabled) ElectricViolet else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Audio Track Switcher Pills
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(
                    "bn" to "🇧🇩 বাংলা",
                    "en" to "🇬🇧 ENG",
                    "hi" to "🇮🇳 हिंदी",
                    "orig" to "CINEMA"
                ).forEach { (code, label) ->
                    val isSelected = activeAudioLang == code
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) NeonCyan else StudioSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else StudioBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onSelectAudioLang(code) }
                            .padding(horizontal = 7.dp, vertical = 5.dp)
                            .testTag("lang_audio_$code")
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}
