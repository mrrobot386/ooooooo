package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.SuccessGreen

@Composable
fun PipelineProgressBar(
    isProcessing: Boolean,
    progressPercent: Float,
    progressMessage: String,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val workflowSteps = listOf(
        "Upload",
        "Analyze",
        "Best Scenes",
        "Shorts",
        "Translate",
        "Dub",
        "Sync",
        "Subtitle",
        "Mix",
        "Export"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioSurface)
            .border(1.dp, StudioBorder)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("pipeline_progress_bar")
    ) {
        // Active Progress banner if running
        if (isProcessing || progressPercent > 0f) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = NeonCyan,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (progressMessage.isNotBlank()) progressMessage else "AI Workflow Engine active",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = NeonCyan,
                trackColor = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Horizontal visual workflow diagram: UPLOAD -> ANALYZE -> FIND BEST SCENES ...
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            workflowSteps.forEachIndexed { index, step ->
                val stepThreshold = (index + 1).toFloat() / workflowSteps.size.toFloat()
                val isCompleted = progressPercent >= stepThreshold
                val isCurrent = isProcessing && progressPercent < stepThreshold && (index == 0 || progressPercent >= (index).toFloat() / workflowSteps.size.toFloat())

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isCompleted -> SuccessGreen.copy(alpha = 0.2f)
                                    isCurrent -> NeonCyan.copy(alpha = 0.25f)
                                    else -> Color(0xFF1E293B)
                                }
                            )
                            .border(
                                1.dp,
                                when {
                                    isCompleted -> SuccessGreen
                                    isCurrent -> NeonCyan
                                    else -> StudioBorder
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = "${index + 1}. $step",
                                fontSize = 10.sp,
                                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isCompleted -> SuccessGreen
                                    isCurrent -> NeonCyan
                                    else -> Color(0xFF64748B)
                                }
                            )
                        }
                    }

                    if (index < workflowSteps.size - 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "→",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }
        }
    }
}
