package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    accentHighlight: Boolean = false
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val containerColor = when {
        accentHighlight -> EditorialCrimson
        isDark -> MaterialTheme.colorScheme.surface
        else -> Color(0xFFFAF6EE)
    }
    val extrudeColor = when {
        accentHighlight -> EditorialCrimsonDark
        isDark -> Color(0xFF0A0A0C)
        else -> EditorialInk
    }
    val valueColor = if (accentHighlight) Color.White else MaterialTheme.colorScheme.onSurface
    val labelColor = if (accentHighlight) Color.White.copy(alpha = 0.88f) else MaterialTheme.colorScheme.onSurfaceVariant

    Soft3DCard(
        modifier = modifier,
        containerColor = containerColor,
        extrudeColor = extrudeColor,
        shape = RoundedCornerShape(12.dp),
        extrudeDepth = 3.5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(2.dp)
                    .background(if (accentHighlight) Color.White.copy(alpha = 0.7f) else EditorialCrimson)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 21.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = valueColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.7.sp
                ),
                color = labelColor,
                textAlign = TextAlign.Center,
                lineHeight = 11.sp
            )
        }
    }
}
