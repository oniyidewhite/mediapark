package com.oblessing.mediapark.screens.search.state

import org.junit.Test

class SearchStateTest {
    @Test
    fun `showHistory should be true if we have history and input field is empty`() {
        val state = SearchState().reduce(SearchState.Event.UserEnteredKeyword("")).reduce(
            SearchState.Event.RetrievedSearchHistory(
                listOf("Welcome")
            )
        )

        assert(!state.showProgress)
        assert(state.showSearchHistory)
    }

    @Test
    fun `showHistory should be false if we have history but input field is not empty`() {
        val state = SearchState().reduce(SearchState.Event.UserEnteredKeyword("www")).reduce(
            SearchState.Event.RetrievedSearchHistory(
                listOf("Welcome")
            )
        )

        assert(state.showProgress)
        assert(!state.showSearchHistory)
    }

    @Test
    fun `search criteria should be updated with the correct information`() {
        var state = SearchState().reduce(SearchState.Event.UserEnteredKeyword("123"))
            .reduce(SearchState.Event.HandledEffect)
            .reduce(SearchState.Event.UpdatedSortBy("relevance"))
            .reduce(SearchState.Event.UpdatedSearchIn(listOf("title")))
            .reduce(SearchState.Event.UpdatedDateRange(from = "a", to = "b"))

        assert(state.showProgress)
        assert(!state.showSearchHistory)
        assert(state.effect == SearchState.Effect.SubmitRequest)
        state.criteria.let {
            assert(it.value == "123")
            assert(it.from == "a")
            assert(it.to == "b")
            assert(it.searchIn == "title")
            assert(it.sortedBy == "relevance")
        }

        state = state.reduce(SearchState.Event.HandledEffect)
        assert(state.effect == null)
        assert(state.showProgress)

        state = state.reduce(SearchState.Event.RequestFailed)
        assert(state.effect == SearchState.Effect.ShowError)
        assert(!state.showProgress)
    }

    @Test
    fun `dont search if query is empty and history is empty`() {
        val state = SearchState().reduce(SearchState.Event.UserEnteredKeyword(""))
            .reduce(SearchState.Event.HandledEffect)
            .reduce(SearchState.Event.UpdatedSortBy("relevance"))
            .reduce(SearchState.Event.UpdatedSearchIn(listOf("title")))
            .reduce(SearchState.Event.UpdatedDateRange(from = "a", to = "b"))

        assert(!state.showProgress)
        assert(!state.showSearchHistory)
        assert(state.effect == null)
        state.criteria.let {
            assert(it.value == "")
            assert(it.from == "a")
            assert(it.to == "b")
            assert(it.searchIn == "title")
            assert(it.sortedBy == "relevance")
        }
    }
}