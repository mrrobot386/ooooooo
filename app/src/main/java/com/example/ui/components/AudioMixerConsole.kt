package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActionRed
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun AudioMixerConsole(
    originalVol: Float,
    dubbedVol: Float,
    bgMusicVol: Float,
    sfxVol: Float,
    noiseReduction: Boolean,
    voiceEnhance: Boolean,
    autoDucking: Boolean,
    volumeNorm: Boolean,
    onOriginalVolChange: (Float) -> Unit,
    onDubbedVolChange: (Float) -> Unit,
    onBgMusicVolChange: (Float) -> Unit,
    onSfxVolChange: (Float) -> Unit,
    onToggleNoiseReduction: () -> Unit,
    onToggleVoiceEnhance: () -> Unit,
    onToggleAutoDucking: () -> Unit,
    onToggleVolumeNorm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vu_meter")
    val meterLevel by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "meter"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(StudioDark)
            .border(1.dp, StudioBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("audio_mixer_console")
    ) {
        // Mixer Console Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Equalizer,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "4-CHANNEL STUDIO AUDIO MIXER",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Real-time voice separation, ducking & LUFS normalization",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp
                    )
                }
            }

            // Target DB Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, StudioBorder, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "-14 LUFS TARGET",
                    color = FilmAmber,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Mixing Faders
        MixerChannelRow(
            label = "ORIGINAL DIALOGUE",
            icon = Icons.AutoMirrored.Filled.VolumeDown,
            accentColor = Color(0xFF94A3B8),
            volume = originalVol,
            onVolumeChange = onOriginalVolChange,
            testTag = "channel_original"
        )

        Spacer(modifier = Modifier.height(8.dp))

        MixerChannelRow(
            label = "DUBBED VOICE (BN / EN / HI)",
            icon = Icons.Default.Mic,
            accentColor = NeonCyan,
            volume = dubbedVol,
            onVolumeChange = onDubbedVolChange,
            testTag = "channel_dubbed"
        )

        Spacer(modifier = Modifier.height(8.dp))

        MixerChannelRow(
            label = "BACKGROUND SCORE (BGM)",
            icon = Icons.Default.MusicNote,
            accentColor = ElectricViolet,
            volume = bgMusicVol,
            onVolumeChange = onBgMusicVolChange,
            testTag = "channel_bgm"
        )

        Spacer(modifier = Modifier.height(8.dp))

        MixerChannelRow(
            label = "SOUND EFFECTS (SFX)",
            icon = Icons.Default.GraphicEq,
            accentColor = FilmAmber,
            volume = sfxVol,
            onVolumeChange = onSfxVolChange,
            testTag = "channel_sfx"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Audio Processing Toggle Buttons (Noise Reduction, Ducking, Voice Enhance, Norm)
        Text(
            text = "AI AUDIO ENHANCEMENTS",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EnhanceTogglePill(
                title = "Auto Ducking",
                subtitle = "Lower BGM on voice",
                enabled = autoDucking,
                accentColor = NeonCyan,
                onClick = onToggleAutoDucking,
                modifier = Modifier.weight(1f)
            )
            EnhanceTogglePill(
                title = "Noise Cut",
                subtitle = "AI room de-noise",
                enabled = noiseReduction,
                accentColor = SuccessGreen,
                onClick = onToggleNoiseReduction,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EnhanceTogglePill(
                title = "Voice Clarity",
                subtitle = "Presence & EQ boost",
                enabled = voiceEnhance,
                accentColor = ElectricViolet,
                onClick = onToggleVoiceEnhance,
                modifier = Modifier.weight(1f)
            )
            EnhanceTogglePill(
                title = "Normalization",
                subtitle = "-14 LUFS Shorts standard",
                enabled = volumeNorm,
                accentColor = FilmAmber,
                onClick = onToggleVolumeNorm,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MixerChannelRow(
    label: String,
    icon: ImageVector,
    accentColor: Color,
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(StudioSurface)
            .border(1.dp, StudioBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${(volume * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                modifier = Modifier.height(26.dp),
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = Color(0xFF1E293B)
                )
            )
        }
    }
}

@Composable
private fun EnhanceTogglePill(
    title: String,
    subtitle: String,
    enabled: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) accentColor.copy(alpha = 0.12f) else StudioSurface)
            .border(
                1.dp,
                if (enabled) accentColor.copy(alpha = 0.6f) else StudioBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Color.White else Color(0xFF94A3B8)
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color(0xFF64748B)
            )
        }

        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(if (enabled) accentColor else Color(0xFF334155))
        )
    }
}
