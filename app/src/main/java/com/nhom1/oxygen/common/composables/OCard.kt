package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF2F2F2),
    contentPadding: Dp = 16.dp,
    withShadow: Boolean = true,
    content: @Composable (BoxScope.() -> Unit),
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        modifier = if (withShadow) modifier
            .shadow(
                elevation = 16.dp,
                spotColor = Color(0xFF000000),
                ambientColor = Color(0xFF000000)
            ) else modifier,
    ) {
        Box(
            modifier = modifier.padding(contentPadding),
            content = content
        )
    }
}