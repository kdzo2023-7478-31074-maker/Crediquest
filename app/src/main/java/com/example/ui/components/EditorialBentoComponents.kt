package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkLine
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCreamExtrude
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkExtrude
import com.example.ui.theme.GlassDarkBottom
import com.example.ui.theme.GlassDarkTop
import com.example.ui.theme.GlassLightBottom
import com.example.ui.theme.GlassLightTop
import com.example.ui.theme.GlassRimDark
import com.example.ui.theme.GlassRimLight
import com.example.ui.theme.GoldFoilSoft
import com.example.ui.theme.GoldLeaf

/**
 * Translucent Liquid Glass container with multi-stop frosted gradient,
 * specular top-left refractive rim, and subtle inner radial sheen.
 */
@Composable
fun LiquidGlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    tintColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val topGlass = tintColor?.copy(alpha = 0.78f) ?: if (isDark) GlassDarkTop else GlassLightTop
    val bottomGlass = tintColor?.copy(alpha = 0.52f) ?: if (isDark) GlassDarkBottom else GlassLightBottom
    val rimStart = if (isDark) GlassRimDark else GlassRimLight
    val rimEnd = if (isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.14f)

    Box(
        modifier = modifier
            .shadow(8.dp, shape, ambientColor = Color.Black.copy(alpha = 0.14f), spotColor = Color.Black.copy(alpha = 0.18f))
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(topGlass, bottomGlass),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.09f else 0.35f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.18f, size.height * 0.15f),
                        radius = size.maxDimension * 0.65f
                    )
                )
            }
            .border(
                width = 1.25.dp,
                brush = Brush.linearGradient(
                    colors = listOf(rimStart, rimEnd),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * Refined Liquid Glass Editorial Masthead featuring translucent frosted glass,
 * multi-stop specular refraction rim, inner ambient light flare,
 * pure editorial typography without the logo, and an embedded glass progress badge.
 */
@Composable
fun LiquidGlassMasthead(
    progressPercentage: Int,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp)
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val topGlass = if (isDark) Color(0xD9252630) else Color(0xF2FFFFFF)
    val midGlass = if (isDark) Color(0xBF1B1B22) else Color(0xD8F8F4EA)
    val bottomGlass = if (isDark) Color(0xD4131318) else Color(0xC6ECE3D2)
    val rimStart = if (isDark) Color(0x80FFFFFF) else Color(0xFAFFFFFF)
    val rimMid = if (isDark) Color(0x2BFFFFFF) else Color(0x66FFFFFF)
    val rimEnd = if (isDark) Color(0x14FFFFFF) else Color(0x1F000000)

    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = if (isDark) 0.35f else 0.10f),
                spotColor = Color.Black.copy(alpha = if (isDark) 0.45f else 0.14f)
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(topGlass, midGlass, bottomGlass),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .drawBehind {
                // Top-left radial specular bloom/sheen
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.14f else 0.45f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.12f, size.height * 0.1f),
                        radius = size.maxDimension * 0.6f
                    )
                )
                // Crisp horizontal specular light rail along top edge
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.55f else 0.90f),
                            Color.White.copy(alpha = if (isDark) 0.20f else 0.40f),
                            Color.Transparent
                        )
                    ),
                    start = Offset(14.dp.toPx(), 1.5f),
                    end = Offset(size.width - 14.dp.toPx(), 1.5f),
                    strokeWidth = 2.dp.toPx()
                )
                // Subtle fluid gloss sheen across top half
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.08f else 0.22f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.45f
                    )
                )
            }
            .border(
                width = 1.25.dp,
                brush = Brush.linearGradient(
                    colors = listOf(rimStart, rimMid, rimEnd),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = shape
            )
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                            append(".")
                        }
                        append("CrediQuest")
                    },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "CAMPUS JOURNALISM COMPANION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                )
            }

            // Embedded Liquid Glass Status Capsule
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDark) Color(0x66FFFFFF) else Color(0x80FFFFFF),
                                if (isDark) Color(0x1FFFFFFF) else Color(0x24000000)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = if (isDark) 0.40f else 0.75f),
                                Color.White.copy(alpha = 0.10f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (progressPercentage == 100) GoldLeaf else EditorialCrimson
                            )
                    )
                    Text(
                        text = "$progressPercentage% READ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.6.sp
                        ),
                        color = if (progressPercentage == 100) GoldLeaf else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Tactile Soft 3D Bento Card with solid extruded 3D base, top-lit bevel gradient,
 * crisp editorial border, and interactive spring press-down physics.
 */
@Composable
fun Soft3DCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    extrudeColor: Color = if (MaterialTheme.colorScheme.background.red < 0.2f) Color(0xFF0A0A0C) else EditorialInk,
    borderColor: Color = if (MaterialTheme.colorScheme.background.red < 0.2f) DarkLine else EditorialInk,
    shape: Shape = RoundedCornerShape(14.dp),
    extrudeDepth: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && onClick != null) extrudeDepth else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "soft3d_card_press"
    )

    Box(
        modifier = modifier
            .padding(bottom = extrudeDepth, end = 1.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
    ) {
        // Solid 3D Extruded Base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 1.dp, y = extrudeDepth)
                .clip(shape)
                .background(extrudeColor)
        )

        // Top-Lit Tactile Face
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = pressOffset)
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            containerColor,
                            containerColor.copy(
                                red = (containerColor.red * 0.96f).coerceIn(0f, 1f),
                                green = (containerColor.green * 0.96f).coerceIn(0f, 1f),
                                blue = (containerColor.blue * 0.96f).coerceIn(0f, 1f)
                            )
                        )
                    )
                )
                .drawBehind {
                    // Subtle top specular highlight line
                    drawLine(
                        color = Color.White.copy(alpha = 0.35f),
                        start = Offset(0f, 1.5f),
                        end = Offset(size.width, 1.5f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                .border(1.5.dp, borderColor, shape),
            content = content
        )
    }
}

/**
 * Tactile Soft 3D Push-Button with extruded base, top bevel highlight,
 * and spring press-down animation.
 */
@Composable
fun Soft3DButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = EditorialCrimson,
    contentColor: Color = Color.White,
    extrudeColor: Color = EditorialCrimsonDark,
    shape: Shape = RoundedCornerShape(10.dp),
    extrudeDepth: Dp = 3.5.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) extrudeDepth else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "soft3d_btn_press"
    )

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .padding(bottom = extrudeDepth)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        // 3D Extruded Base
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = extrudeDepth)
                .clip(shape)
                .background(extrudeColor)
        )

        // Button Cap
        Row(
            modifier = Modifier
                .offset(y = pressOffset)
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            containerColor,
                            containerColor.copy(
                                red = (containerColor.red * 0.9f).coerceIn(0f, 1f),
                                green = (containerColor.green * 0.9f).coerceIn(0f, 1f),
                                blue = (containerColor.blue * 0.9f).coerceIn(0f, 1f)
                            )
                        )
                    )
                )
                .border(1.5.dp, EditorialInk, shape)
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

/**
 * Swiss-Editorial Section Header with leading crimson dot (e.g. ".READING RECORD")
 * and optional numbered index or trailing action.
 */
@Composable
fun EditorialSectionHeader(
    title: String,
    indexLabel: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (indexLabel != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = EditorialInk,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = indexLabel,
                        color = EditorialCream,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                        append(".")
                    }
                    append(title.uppercase())
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 0.8.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    letterSpacing = 0.6.sp
                ),
                color = EditorialCrimson,
                modifier = Modifier
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Stepped 3D Bento Progress Chart inspired by the editorial ".TECHNICAL SKILLS"
 * 40% / 60% / 80% stepped block chart in the reference design.
 */
@Composable
fun BentoSteppedChartTile(
    modulesPct: Int,
    quizzesPct: Int,
    accuracyPct: Int,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val cardBg = if (isDark) DarkSurface else Color(0xFFFAF6EE)

    Soft3DCard(
        modifier = modifier.fillMaxWidth(),
        containerColor = cardBg,
        shape = RoundedCornerShape(14.dp),
        extrudeDepth = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                            append(".")
                        }
                        append("MASTERY METRICS")
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "LIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    ),
                    color = EditorialCrimson
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Stepped3DBarColumn(
                    percentage = modulesPct,
                    label = "MODULES",
                    barColor = if (isDark) Color(0xFF32323A) else EditorialCream,
                    extrudeColor = if (isDark) Color(0xFF141418) else EditorialCreamExtrude,
                    textColor = if (isDark) Color.White else EditorialInk,
                    minHeightDp = 42.dp,
                    maxHeightDp = 76.dp,
                    modifier = Modifier.weight(1f)
                )

                Stepped3DBarColumn(
                    percentage = quizzesPct,
                    label = "QUIZZES",
                    barColor = EditorialCrimson,
                    extrudeColor = EditorialCrimsonDark,
                    textColor = Color.White,
                    minHeightDp = 48.dp,
                    maxHeightDp = 76.dp,
                    modifier = Modifier.weight(1f)
                )

                Stepped3DBarColumn(
                    percentage = accuracyPct,
                    label = "ACCURACY",
                    barColor = EditorialInk,
                    extrudeColor = EditorialInkExtrude,
                    textColor = EditorialCream,
                    minHeightDp = 54.dp,
                    maxHeightDp = 76.dp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun Stepped3DBarColumn(
    percentage: Int,
    label: String,
    barColor: Color,
    extrudeColor: Color,
    textColor: Color,
    minHeightDp: Dp,
    maxHeightDp: Dp,
    modifier: Modifier = Modifier
) {
    val clamped = percentage.coerceIn(0, 100)
    val barHeight = minHeightDp + ((maxHeightDp - minHeightDp) * (clamped / 100f))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "$clamped%",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp)
        ) {
            // 3D Extruded Base
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .offset(y = 3.dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                    .background(extrudeColor)
            )

            // 3D Bar Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                    .background(barColor)
                    .border(
                        1.5.dp,
                        EditorialInk,
                        RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    ),
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
