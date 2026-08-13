package com.polka.android.data.model

enum class SortStatus{
    RANGEOFPLAYERCOUNTS,
    SESSIONTIME,
    // TODO: add more
}

data class SortQuery (
    var sortStatus: SortStatus? = null,
    var rangeOfPlayerCountsFilter : List<Int>? = null,
    var sessionTimeFilterLessTimeThan: Int? = null
)

fun isSortQueryEmpty(query: SortQuery): Boolean{
    if (
        query.sortStatus == null &&
        query.rangeOfPlayerCountsFilter == null &&
        query.sessionTimeFilterLessTimeThan == null
    ) return true
    else return false
}