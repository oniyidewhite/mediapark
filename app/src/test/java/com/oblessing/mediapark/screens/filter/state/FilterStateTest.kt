package com.oblessing.mediapark.screens.filter.state

import com.oblessing.mediapark.model.SearchCriteria
import org.junit.Test

class FilterStateTest {
    @Test
    fun `clear should restore the search Criteria to the last known Criteria`() {
        var state = FilterState().reduce(
            FilterState.Event.UserDefinedCriteria(
                SearchCriteria(
                    to = "a",
                    from = "b",
                    searchIn = "c",
                    value = "d",
                    sortedBy = "f"
                )
            )
        )

        assert(state.currentCriteria.value == "d")
        assert(state.currentCriteria.from == "b")
        assert(state.currentCriteria.searchIn == "c")
        assert(state.currentCriteria.to == "a")
        assert(state.currentCriteria.sortedBy == "f")

        state = state.reduce(FilterState.Event.UserChangeSearchIn(listOf("g")))
        assert(state.currentCriteria.searchIn != "c")
        assert(state.olderCriteria.searchIn == "c")

        state = state.reduce(FilterState.Event.UserTappedClearSearchIn)
        assert(state.currentCriteria.searchIn == "c")
        assert(state.olderCriteria.searchIn == "c")


        state = state.reduce(FilterState.Event.UserChangedDateFrom("k")).reduce(FilterState.Event.UserChangedDateTo("l"))
        assert(state.currentCriteria.from == "k")
        assert(state.olderCriteria.from == "b")
        assert(state.currentCriteria.to == "l")
        assert(state.olderCriteria.to == "a")

        state = state.reduce(FilterState.Event.UserTappedClearDate)
        assert(state.currentCriteria.from == "b")
        assert(state.olderCriteria.from == "b")
        assert(state.currentCriteria.to == "a")
        assert(state.olderCriteria.to == "a")
    }
}