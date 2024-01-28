package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LineSpacer() {
    Row(
        modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            color = Color.Gray,
            thickness = 1.dp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLineSpacer() {
    LineSpacer()
}
