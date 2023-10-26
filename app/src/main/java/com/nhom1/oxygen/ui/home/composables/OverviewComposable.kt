package com.nhom1.oxygen.ui.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

@Composable
fun OverviewComposable() {
    Box(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        AndroidView(
            modifier = Modifier.height(
                height = 300.dp
            ),
            factory = {
                MapView(it).apply {
                    isTilesScaledToDpi = true
                    setTileSource(TileSourceFactory.MAPNIK)
                }
            }
        )
        Text(text = "alo")
    }
}