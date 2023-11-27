package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhom1.oxygen.R
import com.nhom1.oxygen.common.constants.OxygenColors
import com.nhom1.oxygen.utils.extensions.oBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> ODropdownMenu(
    modifier: Modifier = Modifier,
    entries: Map<T, String>,
    defaultSelected: T? = null,
    placeholder: String? = null,
    onSelected: (T) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selected by rememberSaveable {
        mutableStateOf(defaultSelected)
    }
    ExposedDropdownMenuBox(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor()
                .oBorder(),
            value = entries[selected] ?: placeholder ?: "",
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.expand_more),
                    contentDescription = null,
                )
            },
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = OxygenColors.containerColor,
                unfocusedContainerColor = OxygenColors.containerColor,
                focusedIndicatorColor = Color.Unspecified,
                unfocusedIndicatorColor = Color.Unspecified,
            )
        )
        ExposedDropdownMenu(
            expanded = entries.isNotEmpty() && expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (entry in entries) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = entry.value
                        )
                    },
                    onClick = {
                        expanded = false
                        selected = entry.key
                        onSelected.invoke(entry.key)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ODropdownMenuPreview() {
    ODropdownMenu(
        entries = mapOf(
            0 to "Menu 0",
            1 to "Menu 1",
            2 to "Menu 2"
        )
    ) {}
}