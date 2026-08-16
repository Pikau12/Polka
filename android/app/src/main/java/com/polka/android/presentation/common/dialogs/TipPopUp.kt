package com.polka.android.presentation.common.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.theme.PolkaTipPopUpColors

@Composable
fun TipPopUp (
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    message: String,
){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .clickable{ onDismiss() }
    ){
        Card(
            modifier = modifier
                .align(Alignment.Center)
                .clickable { /* to not close when click inside */ },
            shape = RoundedCornerShape(8.dp),
            colors = PolkaTipPopUpColors
        ) {
            Column {
                Text(
                    text = message,
                    color = PolkaTipPopUpColors.contentColor
                )
            }
        }
    }
}