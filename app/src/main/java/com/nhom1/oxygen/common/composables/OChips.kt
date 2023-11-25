package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.theme.oxygenColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun <T : Any> OChips(modifier: Modifier = Modifier, entries: Map<T, String>, initialValue: T? = null, onSelected: (T) -> Unit) {
    var selected by rememberSaveable {
        mutableStateOf<T?>(initialValue)
    }
    FlowRow(
        modifier = modifier
    ) {
        for (entry in entries) {
            FilterChip(
                selected = selected == entry.key,
                leadingIcon = {
                    if (selected == entry.key) {
                        Icon(
                            painter = painterResource(id = R.drawable.done),
                            contentDescription = null
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFFD5E7BF),
                    selectedContainerColor = oxygenColor,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White,
                    labelColor = Color.Black,
                    iconColor = Color.Black,
                ),
                elevation = FilterChipDefaults.elevatedFilterChipElevation(
                    elevation = 0.dp
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = Color.Unspecified
                ),
                onClick = {
                    selected = entry.key
                    onSelected.invoke(entry.key)
                },
                label = {
                    Text(
                        text = entry.value,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.padding(horizontal = 4.dp).height(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun OChipsPreview() {
    OChips(
        entries = mapOf(
            0 to "Chip 1", 1 to "Chip 2", 2 to "Chip 3"
        )
    ) {}
}