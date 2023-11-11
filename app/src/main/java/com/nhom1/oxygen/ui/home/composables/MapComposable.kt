package com.nhom1.oxygen.ui.home.composables

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

@Composable
fun MapComposable() {
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
}