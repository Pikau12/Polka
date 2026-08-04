package com.polka.android.data.model

enum class SortStatus{
    RANGEOFPLAYERCOUNTS,
    SESSIONTIME,
    // TODO: add more
}

data class SortQuery (
    var sortStatus: SortStatus?,
    var rangeOfPlayerCountsFilter : List<Int>,
    var sessionTimeFilterLessTimeThan: Int
)