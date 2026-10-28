package com.polka.android.presentation.common.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.polka.android.presentation.common.buttons.CancelButton
import com.polka.android.presentation.common.buttons.TipButton
import com.polka.android.presentation.theme.PolkaStar
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun GameRatingMenu(
    onCancelClick: () -> Unit,
    onTipClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onRatingListItemClick: (Int) -> Unit,
    expanded: Boolean,
    rating: Int?
) {
    if (!expanded) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            // Выравниваем элементы по верхнему краю
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Добавляем отступ сверху, чтобы кнопка встала вровень с текстом внутри меню
            CancelButton{ onCancelClick() }

            Surface(
                modifier = Modifier
                    .width(100.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column {
                    for (i in 10 downTo 1) {
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = "$i",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            },
                            onClick = { onRatingListItemClick(i) },
                            leadingIcon = {
                                if (rating == null || rating < i)
                                    Icon(
                                        Icons.Filled.StarBorder,
                                        contentDescription = "Unfilled star at $i",
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                else
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = "Star at $i",
                                        modifier = Modifier.size(24.dp),
                                        tint = PolkaStar
                                    )
                            }
                        )
                    }
                }
            }

            // Аналогичный отступ для правой кнопки
            TipButton{ onTipClick() }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun GameRatingMenuPreview() {
    PolkaTheme {
        GameRatingMenu(
            expanded = true,
            rating = 7,
            onCancelClick = { },
            onTipClick = { },
            onDismissRequest = { },
            onRatingListItemClick = { }
        )
    }
}