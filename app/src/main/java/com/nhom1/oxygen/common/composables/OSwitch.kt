package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhom1.oxygen.common.constants.OxygenColors

@Composable
fun OSwitch(
    modifier: Modifier = Modifier,
    initialValue: Boolean = false,
    enabledTitle: String? = null,
    disabledTitle: String? = null,
    onChange: (Boolean) -> Unit
) {
    var value by rememberSaveable {
        mutableStateOf(initialValue)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (disabledTitle != null) Text(
            text = disabledTitle,
            fontSize = 12.sp,
            color = if (!value) Color.Black else Color.Gray,
            fontWeight = if (!value) FontWeight.SemiBold else FontWeight.Light,
            modifier = Modifier.padding(end = 4.dp)
        )
        Switch(
            checked = value,
            onCheckedChange = {
                value = it
                onChange(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = OxygenColors.mainColor,
                checkedTrackColor = OxygenColors.secondaryColor,
                checkedBorderColor = OxygenColors.mainColor,
                uncheckedThumbColor = OxygenColors.secondaryColor,
                uncheckedTrackColor = OxygenColors.containerColor,
                uncheckedBorderColor = OxygenColors.secondaryColor,
            ),
            modifier = modifier
        )
        if (enabledTitle != null) Text(
            text = enabledTitle,
            fontSize = 12.sp,
            color = if (value) Color.Black else Color.Gray,
            fontWeight = if (value) FontWeight.SemiBold else FontWeight.Light,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OSwitchPreview() {
    OSwitch() {

    }
}

@Preview(showBackground = true)
@Composable
fun OSwitchPreview2() {
    OSwitch(initialValue = true, disabledTitle = "Disable", enabledTitle = "Enable") {

    }
}