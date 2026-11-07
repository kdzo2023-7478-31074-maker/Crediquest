package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JournalismModule
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialInk
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
        EditorialCrimson
    }

    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val cardSurface = if (isDark) DarkSurface else Color(0xFFFAF6EE)

    Soft3DCard(
        modifier = modifier
            .height(204.dp)
            .testTag("book_card_${module.id}"),
        containerColor = cardSurface,
        shape = RoundedCornerShape(14.dp),
        extrudeDepth = 4.dp,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Color-blocked Bento Header Band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                bookColor,
                                bookColor.copy(
                                    red = (bookColor.red * 0.85f).coerceIn(0f, 1f),
                                    green = (bookColor.green * 0.85f).coerceIn(0f, 1f),
                                    blue = (bookColor.blue * 0.85f).coerceIn(0f, 1f)
                                )
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = EditorialInk
                    ) {
                        Text(
                            text = "MODULE ${module.moduleNumber}",
                            color = EditorialCream,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                        )
                    }

                    if (module.progressPct == 100) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GoldLeaf,
                            border = BorderStroke(1.dp, EditorialInk)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Finished",
                                tint = EditorialInk,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(2.dp)
                            )
                        }
                    } else {
                        LiquidGlassBox(
                            shape = RoundedCornerShape(5.dp),
                            tintColor = EditorialInk,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${module.pageCount}P",
                                color = EditorialCream,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // Crisp Editorial Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(EditorialInk)
            )

            // Card Body & Progress Footer
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = module.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { (module.progressPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (module.progressPct == 100) GoldLeaf else EditorialCrimson,
                        trackColor = LineBorder.copy(alpha = 0.22f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when {
                                module.progressPct == 100 -> "COMPLETED"
                                module.progressPct > 0 -> "${module.progressPct}% READ"
                                else -> "UNREAD"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = when {
                                module.progressPct == 100 -> GoldLeaf
                                module.progressPct > 0 -> EditorialCrimson
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )

                        Text(
                            text = "OPEN ›",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = EditorialCrimson
                        )
                    }
                }
            }
        }
    }
}
