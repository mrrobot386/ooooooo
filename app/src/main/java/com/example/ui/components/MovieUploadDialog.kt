package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.FilmAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun MovieUploadDialog(
    onDismiss: () -> Unit,
    onUploadMovie: (
        title: String,
        uri: String,
        fileSize: String,
        durationFormatted: String,
        durationSec: Int,
        format: String
    ) -> Unit
) {
    var movieTitleInput by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("MP4") }

    // Android Storage Video Picker Launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val first = uris.first()
            val derivedName = first.lastPathSegment?.substringAfterLast("/") ?: "Imported Feature Film"
            onUploadMovie(
                derivedName.substringBeforeLast("."),
                first.toString(),
                "3.8 GB",
                "01:48:30",
                6510,
                "MP4"
            )
            onDismiss()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .testTag("movie_upload_dialog"),
            colors = CardDefaults.cardColors(containerColor = StudioDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "UPLOAD LICENSED MOVIE",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Drag & Drop / Device Picker Zone
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(StudioSurface)
                        .border(1.5.dp, Brush.horizontalGradient(listOf(NeonCyan, ElectricViolet)), RoundedCornerShape(12.dp))
                        .clickable { videoPickerLauncher.launch("video/*") }
                        .padding(16.dp)
                        .testTag("device_picker_dropzone"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.VideoFile,
                            contentDescription = "Drop video",
                            tint = NeonCyan,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Drag & Drop Movie File or Click to Browse",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Supports MP4, MOV, MKV, AVI • Large 4K Video Files",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Supported Formats Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("MP4", "MOV", "MKV", "AVI").forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else StudioSurface)
                                .border(1.dp, if (isSelected) NeonCyan else StudioBorder, RoundedCornerShape(6.dp))
                                .clickable { selectedFormat = fmt }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fmt,
                                color = if (isSelected) NeonCyan else Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Demo Licensed Movies (1-Click Instant AI Processing)
                Text(
                    text = "OR LOAD LICENSED DEMO FILM VAULT",
                    color = FilmAmber,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                DemoFilmItem(
                    title = "Operation Dhaka: Protocol 7",
                    meta = "4.2 GB • 01:54:20 • 4K UHD • Espionage",
                    badge = "LICENSED",
                    onClick = {
                        onUploadMovie(
                            "Operation Dhaka: Protocol 7",
                            "licensed_vault/dhaka_protocol_7_master.mp4",
                            "4.2 GB",
                            "01:54:20",
                            6860,
                            "MP4"
                        )
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                DemoFilmItem(
                    title = "The Himalayas Echo",
                    meta = "2.8 GB • 01:42:15 • 1080p • Survival Thriller",
                    badge = "LICENSED",
                    onClick = {
                        onUploadMovie(
                            "The Himalayas Echo",
                            "licensed_vault/himalayas_echo_master.mov",
                            "2.8 GB",
                            "01:42:15",
                            6135,
                            "MOV"
                        )
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                DemoFilmItem(
                    title = "Royal Bengal Protocol",
                    meta = "5.1 GB • 02:10:00 • 4K UHD • High-Impact Action",
                    badge = "LICENSED",
                    onClick = {
                        onUploadMovie(
                            "Royal Bengal Protocol",
                            "licensed_vault/royal_bengal_protocol.mkv",
                            "5.1 GB",
                            "02:10:00",
                            7800,
                            "MKV"
                        )
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun DemoFilmItem(
    title: String,
    meta: String,
    badge: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudioSurface)
            .border(1.dp, StudioBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(NeonCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = meta,
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(SuccessGreen.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = badge,
                color = SuccessGreen,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
