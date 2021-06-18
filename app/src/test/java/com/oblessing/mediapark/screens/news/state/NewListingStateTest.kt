package com.oblessing.mediapark.screens.news.state

import org.junit.Test

class NewListingStateTest {
    @Test
    fun `After fetching news content successfully, result should not be null`() {
        val state = NewListingState().reduce(NewListingState.Event.RequestWasSuccessful(listOf()))
            .reduce(NewListingState.Event.HandledEffect)

        assert(state.articles != null)
        assert(!state.showProgress)
        assert(state.effect == null)
    }

    @Test
    fun `After fetching news content failed, result should not be null`() {
        val state = NewListingState().reduce(NewListingState.Event.RequestFailed)
            .reduce(NewListingState.Event.HandledEffect)

        assert(state.articles == null)
        assert(!state.showProgress)
        assert(state.effect == null)
    }

    @Test
    fun `Show progress should be true if when we are fetching content`() {
        val state = NewListingState().reduce(NewListingState.Event.HandledEffect)

        assert(state.showProgress)
        assert(state.effect == null)
    }

    @Test
    fun `Show progress should be false and effect should be show error if request failed`() {
        var state = NewListingState().reduce(NewListingState.Event.HandledEffect)
            .reduce(NewListingState.Event.RequestFailed)

        assert(!state.showProgress)
        assert(state.effect == NewListingState.Effect.ShowError)

        state = state.reduce(NewListingState.Event.HandledEffect)
        assert(state.effect == null)
    }
}