package com.oblessing.mediapark.screens.news.state

import com.airbnb.mvrx.MavericksState
import com.oblessing.mediapark.model.Article

data class NewListingState(
    val effect: Effect? = Effect.RequestPage,
    val event: Event? = null,
    val articles: List<Article>? = null,
    private val loading: Boolean = true,
) : MavericksState {
    val showProgress
        get() = loading

    fun reduce(event: Event): NewListingState {
        return when (event) {
            Event.HandledEffect -> copy(event = event, effect = null)
            Event.RequestFailed -> copy(event = event, effect = Effect.ShowError, loading = false)
            is Event.RequestWasSuccessful -> copy(
                event = event,
                articles = event.results,
                loading = false
            )
            Event.UserClickedContent -> copy(event = event, effect = Effect.OpenLink)
            Event.UserTappedRetry -> copy(
                event = event,
                loading = true,
                effect = Effect.RequestPage
            )
        }
    }

    sealed class Event {
        object HandledEffect : Event()
        object RequestFailed : Event()
        object UserTappedRetry : Event()
        object UserClickedContent : Event()
        data class RequestWasSuccessful(val results: List<Article>) : Event()
    }

    sealed class Effect {
        object RequestPage : Effect()
        object OpenLink : Effect()
        object ShowError : Effect()
    }
}