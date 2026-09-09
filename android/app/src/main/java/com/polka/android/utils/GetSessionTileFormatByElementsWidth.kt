package com.polka.android.utils

import androidx.compose.ui.unit.Dp
import com.polka.android.presentation.common.tiles.BubbleType
import com.polka.android.presentation.common.tiles.SessionTileFormat

fun GetSessionTileFormatByElementsWidth(
    cardWidth: Dp,
    fieldWidthWithSmallPicture: Dp,
    fieldWidthWithMediumPicture: Dp,
    elements: List<Triple<String, BubbleType, Float>>
): SessionTileFormat {
    val cardWidthPx = cardWidth.value
    val fieldWidthWithSmallPicturePx = fieldWidthWithSmallPicture.value
    val fieldWidthWithMediumPicturePx = fieldWidthWithMediumPicture.value

    val bubbleHorizontalPadding = 16f
    val bubbleSpacing = 6f
    val minBubbleWidth = 40f

    var totalWidth = 0f
    var fitsInOneRow = true

    for ((index, element) in elements.withIndex()) {
        val textWidth = element.third
        val effectiveWidth = maxOf(textWidth, minBubbleWidth)
        val fullBubbleWidth = effectiveWidth + bubbleHorizontalPadding +
                if (index > 0) bubbleSpacing else 0f

        if (totalWidth + fullBubbleWidth <= fieldWidthWithSmallPicturePx) {
            totalWidth += fullBubbleWidth
        } else {
            fitsInOneRow = false
            break
        }
    }

    if (fitsInOneRow) {
        val firstRow = elements.map { Pair(it.first, it.second) }
        return SessionTileFormat(
            numOfRows = 1,
            firstRow = firstRow
        )
    }

    val firstRow: MutableList<Pair<String, BubbleType>> = mutableListOf()
    val secondRow: MutableList<Pair<String, BubbleType>> = mutableListOf()
    val thirdRow: MutableList<Pair<String, BubbleType>> = mutableListOf()

    var currentWidth = 0f
    var currentRow = 1
    var index = 0

    while (index < elements.size) {
        val element = elements[index]
        val text = element.first
        val type = element.second
        val textWidth = element.third

        val fullBubbleWidth = textWidth + bubbleHorizontalPadding +
                if (currentWidth > 0f) bubbleSpacing else 0f

        when (currentRow) {
            1 -> {
                if (currentWidth + fullBubbleWidth <= fieldWidthWithMediumPicturePx) {
                    firstRow.add(Pair(text, type))
                    currentWidth += fullBubbleWidth
                    index++
                } else {
                    currentRow = 2
                    currentWidth = 0f
                }
            }
            2 -> {
                if (currentWidth + fullBubbleWidth <= fieldWidthWithMediumPicturePx) {
                    secondRow.add(Pair(text, type))
                    currentWidth += fullBubbleWidth
                    index++
                } else {
                    currentRow = 3
                    currentWidth = 0f
                }
            }
            3 -> {
                if (currentWidth + fullBubbleWidth <= cardWidthPx) {
                    thirdRow.add(Pair(text, type))
                    currentWidth += fullBubbleWidth
                    index++
                } else {
                    break
                }
            }
        }
    }

    return SessionTileFormat(
        numOfRows = currentRow,
        firstRow = firstRow,
        secondRow = secondRow,
        thirdRow = thirdRow
    )
}