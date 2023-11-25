package com.nhom1.oxygen.utils.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nhom1.oxygen.common.theme.oxygenColor

fun Modifier.oBorder(): Modifier {
    return this
        .border(
            width = 1.dp,
            color = oxygenColor.copy(alpha = 0.25F),
            shape = RoundedCornerShape(12.dp)
        )
}

fun Modifier.oShadow(): Modifier {
    return this.shadow(
        elevation = 16.dp,
        spotColor = Color(0xFF000000),
        ambientColor = Color(0xFF000000)
    )
}

fun Modifier.oClip(): Modifier {
    return this.clip(RoundedCornerShape(12.dp))
}