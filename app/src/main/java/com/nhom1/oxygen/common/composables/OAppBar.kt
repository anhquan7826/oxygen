@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.utils.extensions.oShadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OAppBar(
    title: String,
    leading: Painter,
    onLeadingPressed: (() -> Unit)? = null,
    actions: List<Painter> = listOf(),
    onActionPressed: List<() -> Unit> = listOf(),
    withShadow: Boolean = true,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        windowInsets = WindowInsets(left = 16.dp, right = 16.dp),
        navigationIcon = {
            IconButton(
                onClick = {
                    onLeadingPressed?.invoke()
                },
                enabled = onLeadingPressed != null
            ) {
                Icon(
                    painter = leading,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        actions = {
            for (i in actions.indices) {
                IconButton(
                    onClick = {
                        onActionPressed[i].invoke()
                    }
                ) {
                    Icon(
                        actions[i],
                        null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        modifier = if (withShadow) Modifier.oShadow() else Modifier
    )
}

@Preview
@Composable
fun OAppBarPreview() {
    OAppBar(
        title = "Title",
        leading = painterResource(id = R.drawable.house),
        actions = listOf(
            painterResource(id = R.drawable.bell)
        )
    )
}