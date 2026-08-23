package com.polka.android.data.model

/**
 * This enumeration represents grouping options available to user.
 * [RANGEOFPLAYERCOUNTS] means that collection will be grouped by player counts
 * [SESSIONTIME] means that collection will be grouped by session time
 */
enum class GroupStatus{
    RANGEOFPLAYERCOUNTS,
    SESSIONTIME,
    // TODO: add more
}

/**
 * The [SortQuery] data class contains a grouping status component that defines the grouping request,
 * and a filtering component that identifies suitable games based on the selected option
 */
data class SortQuery (
    var groupStatus: GroupStatus? = null,
    var rangeOfPlayerCountsFilter : List<Int>? = null,
    var sessionTimeFilterLessTimeThan: Int? = null
)

/**
 * This function determines whether the sort request is empty.
 */
fun isSortQueryEmpty(query: SortQuery): Boolean{
    return query.groupStatus == null &&
    query.rangeOfPlayerCountsFilter == null &&
    query.sessionTimeFilterLessTimeThan == null
}