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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MovieEntity
import com.example.data.model.ShortEntity
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun ExportCenterScreen(
    selectedMovie: MovieEntity?,
    shorts: List<ShortEntity>,
    onExportAll: (quality: String) -> Unit,
    onExportSingleShort: (short: ShortEntity, quality: String, lang: String) -> Unit,
    onOpenLicenseDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedQuality by remember { mutableStateOf("4K") }
    var selectedRatio by remember { mutableStateOf("9:16") }

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
                    text = "PROFESSIONAL EXPORT & MASTERING VAULT",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Broadcast-Ready 9:16 Shorts with Bangla, English & Hindi Tracks and SRT Subtitles",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
            }
        }

        // Export Settings & Quality Selector
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
                        text = "MASTERING PRESETS",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quality Selector: 1080p vs 4K
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("1080p Full HD" to "1080p", "4K Ultra HD" to "4K").forEach { (label, code) ->
                            val isSelected = selectedQuality == code
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonCyan else StudioBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedQuality = code }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.HighQuality,
                                        contentDescription = null,
                                        tint = if (isSelected) NeonCyan else Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) NeonCyan else Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Ratio Selector: 9:16 vs 16:9
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("9:16 Vertical (Shorts/Reels/TikTok)" to "9:16", "16:9 Widescreen Master" to "16:9").forEach { (label, ratio) ->
                            val isSelected = selectedRatio == ratio
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ElectricViolet.copy(alpha = 0.2f) else Color(0xFF0F172A))
                                    .border(
                                        1.dp,
                                        if (isSelected) ElectricViolet else StudioBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedRatio = ratio }
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ElectricViolet else Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Export Package Overview: 5 Key Assets Generated per Short
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
                        text = "MULTI-ASSET OUTPUT BUNDLE",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutputAssetPill(
                        title = "🇧🇩 Bangla Dubbed Shorts (বাংলা ভার্সন)",
                        detail = "9:16 • 4K/1080p • Embedded Subtitles • Auto Ducked BGM",
                        color = SuccessGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutputAssetPill(
                        title = "🇬🇧 English Dubbed Shorts",
                        detail = "9:16 • 4K/1080p • Hollywood Pacing • Synchronized Lip-Sync",
                        color = NeonCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutputAssetPill(
                        title = "🇮🇳 Hindi Dubbed Shorts (हिंदी वर्शन)",
                        detail = "9:16 • 4K/1080p • Bollywood Cadence • -14 LUFS Normalized",
                        color = FilmAmber
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutputAssetPill(
                        title = "🎬 Original Theatrical Master Short",
                        detail = "Clean uncut sound mix with dynamic camera crop",
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutputAssetPill(
                        title = "📄 Synchronized Subtitle Pack (.SRT / .VTT)",
                        detail = "Timecoded Bengali (বাংলা), English, and Hindi caption files",
                        color = ElectricViolet
                    )
                }
            }
        }

        // Batch Export CTA Button
        item {
            ElevatedButton(
                onClick = { onExportAll(selectedQuality) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = NeonCyan,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("export_all_shorts_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FolderZip,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BATCH EXPORT ALL ${shorts.size} SHORTS ($selectedQuality BUNDLE)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Individual Rendered Shorts List
        item {
            Text(
                text = "INDIVIDUAL SHORT DOWNLOADS",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        items(shorts) { shortItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, StudioBorder, RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(containerColor = StudioSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = shortItem.title,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${shortItem.shortType} • ${shortItem.durationSec}s • $selectedQuality • 9:16",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }

                    ElevatedButton(
                        onClick = { onExportSingleShort(shortItem, selectedQuality, "All") },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF1E293B),
                            contentColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Desktop Workstation Deployment Note
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, StudioBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DesktopWindows,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "VAST AI WORKSTATION ENGINE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "VAST AI operates as a unified cloud & desktop pipeline. You can export complete project packages, video stems, audio dub tracks, and SRT subtitle files ready for social publishing.",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun OutputAssetPill(
    title: String,
    detail: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(detail, color = Color(0xFF94A3B8), fontSize = 9.sp)
        }
    }
}
