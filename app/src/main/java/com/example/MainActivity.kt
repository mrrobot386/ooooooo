package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.components.LicenseDialog
import com.example.ui.components.MovieUploadDialog
import com.example.ui.components.PipelineProgressBar
import com.example.ui.components.StudioHeader
import com.example.ui.screens.AudioMixerScreen
import com.example.ui.screens.ExportCenterScreen
import com.example.ui.screens.SceneAnalysisScreen
import com.example.ui.screens.ShortsEditorScreen
import com.example.ui.screens.StudioOverviewScreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.VastAiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VastAiTheme {
                val viewModel: MainViewModel = viewModel()
                MainStudioApp(viewModel)
            }
        }
    }
}

@Composable
fun MainStudioApp(viewModel: MainViewModel) {
    val movies by viewModel.allMovies.collectAsState()
    val shorts by viewModel.allShorts.collectAsState()
    val selectedMovie by viewModel.selectedMovie.collectAsState()
    val scenes by viewModel.scenes.collectAsState()
    val selectedShort by viewModel.selectedShort.collectAsState()

    val activeTab by viewModel.activeTab.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val progressPercent by viewModel.progressPercent.collectAsState()
    val progressMessage by viewModel.progressMessage.collectAsState()

    val isPlaying by viewModel.isPlaying.collectAsState()
    val aspectRatio by viewModel.aspectRatio.collectAsState()
    val activeAudioLang by viewModel.activeAudioLang.collectAsState()
    val activeSubtitleLang by viewModel.activeSubtitleLang.collectAsState()
    val faceTrackingEnabled by viewModel.faceTrackingEnabled.collectAsState()
    val dynamicZoomEnabled by viewModel.dynamicZoomEnabled.collectAsState()

    val originalVol by viewModel.originalAudioVol.collectAsState()
    val dubbedVol by viewModel.dubbedVoiceVol.collectAsState()
    val bgMusicVol by viewModel.bgMusicVol.collectAsState()
    val sfxVol by viewModel.sfxVol.collectAsState()
    val noiseReduction by viewModel.noiseReduction.collectAsState()
    val voiceEnhance by viewModel.voiceEnhancement.collectAsState()
    val autoDucking by viewModel.autoDucking.collectAsState()
    val volumeNorm by viewModel.volumeNormalization.collectAsState()

    val licenseConfirmed by viewModel.licenseConfirmed.collectAsState()
    val licenseKey by viewModel.licenseKey.collectAsState()
    val licenseHolder by viewModel.licenseHolder.collectAsState()
    val showLicenseDialog by viewModel.showLicenseDialog.collectAsState()
    val exportNotice by viewModel.exportSuccessNotice.collectAsState()

    var showUploadDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(exportNotice) {
        exportNotice?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.dismissExportNotice()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                StudioHeader(
                    licenseVerified = licenseConfirmed,
                    onOpenLicense = { viewModel.openLicenseDialog() },
                    onUploadClick = { showUploadDialog = true }
                )
                PipelineProgressBar(
                    isProcessing = isProcessing,
                    progressPercent = progressPercent,
                    progressMessage = progressMessage
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = StudioDark,
                contentColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = StudioBorder)
                    .testTag("studio_bottom_nav")
            ) {
                val navItems = listOf(
                    NavigationItem("STUDIO", "Studio", Icons.Default.Dashboard),
                    NavigationItem("SHORTS", "Shorts", Icons.Default.PlayCircle),
                    NavigationItem("SCENES", "Scenes", Icons.Default.Analytics),
                    NavigationItem("MIXER", "Mixer", Icons.Default.Equalizer),
                    NavigationItem("EXPORT", "Export", Icons.Default.FileDownload)
                )

                navItems.forEach { item ->
                    val isSelected = activeTab == item.id
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(item.id) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF64748B),
                            indicatorColor = NeonCyan
                        ),
                        modifier = Modifier.testTag("nav_tab_${item.id.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(StudioDark)
        ) {
            when (activeTab) {
                "STUDIO" -> {
                    StudioOverviewScreen(
                        movies = movies,
                        selectedMovie = selectedMovie,
                        isProcessing = isProcessing,
                        onSelectMovie = { viewModel.selectMovie(it) },
                        onRunPipeline = { viewModel.runFullPipelineForMovie(it) },
                        onUploadClick = { showUploadDialog = true },
                        onNavigateToShorts = { viewModel.setTab("SHORTS") }
                    )
                }

                "SHORTS" -> {
                    ShortsEditorScreen(
                        shorts = shorts,
                        selectedShort = selectedShort,
                        isPlaying = isPlaying,
                        aspectRatio = aspectRatio,
                        activeAudioLang = activeAudioLang,
                        activeSubtitleLang = activeSubtitleLang,
                        faceTrackingEnabled = faceTrackingEnabled,
                        dynamicZoomEnabled = dynamicZoomEnabled,
                        onSelectShort = { viewModel.selectShort(it) },
                        onTogglePlay = { viewModel.togglePlayback() },
                        onToggleAspectRatio = { viewModel.toggleAspectRatio() },
                        onToggleFaceTracking = { viewModel.toggleFaceTracking() },
                        onToggleDynamicZoom = { viewModel.toggleDynamicZoom() },
                        onSelectAudioLang = { viewModel.setAudioLang(it) },
                        onSelectSubtitleLang = { viewModel.setSubtitleLang(it) },
                        onPlayDubbingSpeech = { text, lang -> viewModel.ttsEngine.speak(text, lang) },
                        onExportShort = { short ->
                            viewModel.exportShort(short, "4K", activeAudioLang)
                        }
                    )
                }

                "SCENES" -> {
                    SceneAnalysisScreen(
                        scenes = scenes,
                        movieTitle = selectedMovie?.title ?: "Licensed Movie",
                        onGenerateShortFromScene = {
                            viewModel.setTab("SHORTS")
                        }
                    )
                }

                "MIXER" -> {
                    AudioMixerScreen(
                        originalVol = originalVol,
                        dubbedVol = dubbedVol,
                        bgMusicVol = bgMusicVol,
                        sfxVol = sfxVol,
                        noiseReduction = noiseReduction,
                        voiceEnhance = voiceEnhance,
                        autoDucking = autoDucking,
                        volumeNorm = volumeNorm,
                        selectedShort = selectedShort,
                        activeAudioLang = activeAudioLang,
                        isPlaying = isPlaying,
                        onOriginalVolChange = { viewModel.setOriginalAudioVol(it) },
                        onDubbedVolChange = { viewModel.setDubbedVoiceVol(it) },
                        onBgMusicVolChange = { viewModel.setBgMusicVol(it) },
                        onSfxVolChange = { viewModel.setSfxVol(it) },
                        onToggleNoiseReduction = { viewModel.toggleNoiseReduction() },
                        onToggleVoiceEnhance = { viewModel.toggleVoiceEnhance() },
                        onToggleAutoDucking = { viewModel.toggleAutoDucking() },
                        onToggleVolumeNorm = { viewModel.toggleVolumeNorm() },
                        onSelectAudioLang = { viewModel.setAudioLang(it) },
                        onTogglePlayAudio = { viewModel.togglePlayback() }
                    )
                }

                "EXPORT" -> {
                    ExportCenterScreen(
                        selectedMovie = selectedMovie,
                        shorts = shorts,
                        onExportAll = { quality ->
                            selectedShort?.let { viewModel.exportShort(it, quality, "All Languages") }
                        },
                        onExportSingleShort = { short, quality, lang ->
                            viewModel.exportShort(short, quality, lang)
                        },
                        onOpenLicenseDialog = { viewModel.openLicenseDialog() }
                    )
                }
            }

            // Modals
            if (showUploadDialog) {
                MovieUploadDialog(
                    onDismiss = { showUploadDialog = false },
                    onUploadMovie = { title, uri, size, dur, durSec, fmt ->
                        viewModel.addMovieUpload(title, uri, size, dur, durSec, fmt)
                    }
                )
            }

            if (showLicenseDialog) {
                LicenseDialog(
                    currentKey = licenseKey,
                    currentHolder = licenseHolder,
                    onDismiss = { viewModel.closeLicenseDialog() },
                    onVerify = { key, holder ->
                        viewModel.verifyLicense(key, holder)
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val id: String,
    val label: String,
    val icon: ImageVector
)
