package com.polka.android.presentation.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.polka.android.R
import com.polka.android.presentation.common.UiConstants
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun GamePhotoBlock(
    images: List<ImageRequest>,
    onAddPhotoClick: () -> Unit,
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    borderWidth: Dp = 5.dp
) {
    val displayedImages = images.take(3)
    val blockHeight = 160.dp
    val itemSize = blockHeight - (borderWidth * 2)

    if (displayedImages.isEmpty()) {
        Row (
            modifier = modifier
                .height(blockHeight)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            AddPhotoButton(onClick = onAddPhotoClick)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No image loaded yet", // TODO: to str constants
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    else if (displayedImages.size == 1){
        Row (
            modifier = modifier
                .height(blockHeight)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            AddPhotoButton(onClick = onAddPhotoClick)

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = images[0],
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS))
                        .border(
                            borderWidth,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
                        )
                        .clickable(onClick = { onPhotoClick(0) }),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
    else {
        BoxWithConstraints(
            modifier = modifier
                .height(blockHeight)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            val imageSize = maxHeight
            val totalElements = displayedImages.size + 1
            val stepOffset = (maxWidth - imageSize) / (totalElements - 1).coerceAtLeast(1)

            for (index in displayedImages.indices.reversed()) {
                val xOffset = stepOffset * (index + 1)

                AsyncImage(
                    model = displayedImages[index],
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .offset(x = xOffset)
                        .size(imageSize)
                        .zIndex((displayedImages.size - index).toFloat())
                        .clip(RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = 5.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
                        )
                        .clickable(onClick = { onPhotoClick(index) })
                )
            }

            AddPhotoButton(
                onClick = onAddPhotoClick,
                modifier = Modifier.zIndex(10f)
            )
        }
    }
}

@Composable
fun AddPhotoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    borderWidth: Dp = 5.dp
){
    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
            )
            .border(
                borderWidth,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(UiConstants.MAIN_CORNER_RADIUS)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add photo button",
                modifier = Modifier.size(70.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Add photo",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(heightDp = 800, widthDp = 400)
@Composable
fun GamePhotoBlockPreview(){
    PolkaTheme{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val context = LocalContext.current
            val fakeImages = listOf(
                ImageRequest.Builder(context).data(R.drawable.ic_game_placeholder).build(),
                ImageRequest.Builder(context).data(R.drawable.ic_game_placeholder).build(),
                ImageRequest.Builder(context).data(R.drawable.ic_game_placeholder).build()
            )

            GamePhotoBlock(
                fakeImages,
                onAddPhotoClick = {},
                onPhotoClick = {},
            )
        }
    }
}