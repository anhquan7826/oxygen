package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF2F2F2),
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit),
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        modifier = if (onClick != null) modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick.invoke() } else modifier,
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}