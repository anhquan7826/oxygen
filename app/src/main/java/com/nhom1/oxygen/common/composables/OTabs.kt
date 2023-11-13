package com.nhom1.oxygen.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhom1.oxygen.common.theme.oxygenColor
import com.nhom1.oxygen.utils.extensions.oShadow

@Composable
fun OTabRow(modifier: Modifier = Modifier, selectedTabIndex: Int, tabs: @Composable (() -> Unit)) {
    TabRow(
        modifier = modifier.oShadow(),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.White,
        contentColor = Color.Black,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex])
                    .padding(horizontal = it[selectedTabIndex].width / 4)
                    .height(3.dp)
                    .background(
                        color = oxygenColor,
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                        )
                    )
            )
        },
        tabs = tabs
    )
}

@Composable
fun OTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Tab(selected = selected, onClick = onClick) {
        Text(
            title,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
fun OTabsPreview() {
    OTabRow(selectedTabIndex = 0) {
        OTab(selected = true, onClick = { /*TODO*/ }, title = "Tab 1")
        OTab(selected = false, onClick = { /*TODO*/ }, title = "Tab 2")
    }
}