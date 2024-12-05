package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.ui.components.user.ProfilePicture
import com.pwojtowicz.buybuddies.ui.components.user.UsernameText
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_light

@Composable
fun MenuDrawerProfile(onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 15.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(height = 40.dp, width = 40.dp)
        Spacer(modifier = Modifier.width(10.dp))
        UsernameText(fontSize = 20.sp, textColor = bb_theme_text_clr_light)
    }
}

@Preview
@Composable
fun MenuDrawerProfilePreview() {
    Box(){
        MenuDrawerProfile(onClick = {})
    }
}