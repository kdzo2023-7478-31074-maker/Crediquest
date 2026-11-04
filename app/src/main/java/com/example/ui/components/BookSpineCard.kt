package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JournalismModule
import com.example.ui.theme.GoldFoilSoft
import com.example.ui.theme.GoldLeaf
import com.example.ui.theme.LineBorder

@Composable
fun BookSpineCard(
    module: JournalismModule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookColor = try {
        Color(android.graphics.Color.parseColor(module.colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier
            .testTag("book_card_${module.id}")
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, topStart = 3.dp, bottomStart = 3.dp)
                ),
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, topStart = 3.dp, bottomStart = 3.dp),
            colors = CardDefaults.cardColors(containerColor = bookColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Book spine groove gradient (shadow -> highlight -> book surface)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(12.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.45f),
                                    Color.Black.copy(alpha = 0.15f),
                                    Color.White.copy(alpha = 0.22f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Gold foil accent border line along the spine
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 13.dp)
                        .width(1.5.dp)
                        .background(GoldLeaf.copy(alpha = 0.55f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 22.dp, end = 12.dp, top = 14.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Module Number Tag
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = Color.Black.copy(alpha = 0.35f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, GoldFoilSoft.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "MODULE ${module.moduleNumber}",
                                    color = GoldFoilSoft,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }

                            if (module.progressPct == 100) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = GoldLeaf
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Finished",
                                        tint = Color.Black,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .padding(1.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title
                        Text(
                            text = module.title,
                            color = Color(0xFFF9F7F2),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                lineHeight = 17.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Gold hairline divider
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(1.5.dp)
                                .background(GoldFoilSoft.copy(alpha = 0.7f))
                        )
                    }

                    // Bottom publisher footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CREDIQUEST",
                            color = Color(0xFFF9F7F2).copy(alpha = 0.6f),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        // Progress line with rounded bar
        LinearProgressIndicator(
            progress = { (module.progressPct / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = if (module.progressPct == 100) GoldLeaf else MaterialTheme.colorScheme.primary,
            trackColor = LineBorder.copy(alpha = 0.45f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${module.pageCount} page${if (module.pageCount > 1) "s" else ""}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = when {
                    module.progressPct == 100 -> "Completed"
                    module.progressPct > 0 -> "${module.progressPct}% read"
                    else -> "Unread"
                },
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (module.progressPct > 0) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (module.progressPct == 100) GoldLeaf else if (module.progressPct > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
