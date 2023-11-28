package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.nhom1.oxygen.utils.extensions.oBorder

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

@Composable
fun <T> OTextSwitch(values: List<T>, initialValue: T, title: (T) -> String, onChange: (T) -> Unit) {
    var selected by rememberSaveable {
        mutableStateOf(initialValue)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .oBorder()
            .border(
                width = 1.5.dp,
                color = OxygenColors.mainColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = OxygenColors.containerColor)
            .height(36.dp)
            .width(intrinsicSize = IntrinsicSize.Max)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        for (value in values) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .oBorder()
                    .clickable {
                        selected = value
                        onChange(value)
                    }
            ) {
                Text(
                    text = title.invoke(value),
                    fontWeight = if (value == selected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (value == selected) OxygenColors.mainColor else Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (value != values.last()) Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
                    .width(1.dp)
                    .background(color = Color.Gray)
            )
        }
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

@Preview
@Composable
fun OTextSwitchPreview() {
    OTextSwitch(
        values = listOf("On", "Off", "null"),
        initialValue = "On",
        title = { it }
    ) {

    }
}