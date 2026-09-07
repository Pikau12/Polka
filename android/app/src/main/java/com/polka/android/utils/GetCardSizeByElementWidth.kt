package com.polka.android.utils

import androidx.compose.ui.unit.Dp
import com.polka.android.presentation.common.tiles.BubbleType
import com.polka.android.presentation.common.tiles.SessionTileFormat

fun GetCardSizeByElementWidth (
    cardWidth: Dp,
    fieldWidth: Dp,
    elements: List<Pair<String, BubbleType>>
) : SessionTileFormat {
    val cardWidthInInt = cardWidth.value.toInt()
    val fieldWidthInInt = fieldWidth.value.toInt()

    var rowNow: Int = 1
    val firstRow: MutableList<Pair<String, BubbleType>> = emptyList<Pair<String, BubbleType>>().toMutableList()
    val secondRow: MutableList<Pair<String, BubbleType>> = emptyList<Pair<String, BubbleType>>().toMutableList()
    val thirdRow: MutableList<Pair<String, BubbleType>> = emptyList<Pair<String, BubbleType>>().toMutableList()

    var len: Int = 0

    for(str in elements) {
        if (rowNow == 1){
            if (len + str.first.length < fieldWidthInInt) {
                firstRow.add(str)
                len += str.first.length
            }
            else {
                len = 0
                rowNow++
            }
        }
        else if (rowNow == 2) {
            if (len + str.first.length < fieldWidthInInt) {
                secondRow.add(str)
                len += str.first.length
            }
            else {
                len = 0
                rowNow++
            }
        }
        else {
            if (len + str.first.length < cardWidthInInt) {
                thirdRow.add(str)
                len += str.first.length
            }
            else {
                break
            }
        }
    }

    return SessionTileFormat(
        numOfRows = rowNow,
        firstRow = firstRow.toList(),
        secondRow = secondRow.toList(),
        thirdRow = thirdRow.toList()
    )
}